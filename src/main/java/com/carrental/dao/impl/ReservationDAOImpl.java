package com.carrental.dao.impl;

import com.carrental.dao.ReservationDAO;
import com.carrental.model.Reservation;
import com.carrental.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ReservationDAOImpl implements ReservationDAO {

    private Connection getConn() {
        return DBConnection.getInstance().getConnection();
    }

    private Reservation mapRow(ResultSet rs) throws SQLException {
        return new Reservation(
            rs.getInt("reservation_id"),
            rs.getInt("user_id"),
            rs.getInt("car_id"),
            rs.getInt("location_id"),
            rs.getDate("start_date").toLocalDate(),
            rs.getDate("end_date").toLocalDate(),
            rs.getString("status"),
            rs.getInt("insurance_type_id")
        );
    }

    @Override
    public int createReservation(Reservation reservation) {
        String sql = "INSERT INTO Reservation (user_id, car_id, location_id, start_date, end_date, status, insurance_type_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, reservation.getUserId());
            ps.setInt(2, reservation.getCarId());
            ps.setInt(3, reservation.getLocationId());
            ps.setDate(4, Date.valueOf(reservation.getStartDate()));
            ps.setDate(5, Date.valueOf(reservation.getEndDate()));
            ps.setString(6, reservation.getStatus());
            ps.setInt(7, reservation.getInsuranceTypeId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    reservation.setReservationId(id);
                    return id;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error creating reservation: " + e.getMessage(), e);
        }
        return -1;
    }

    @Override
    public Optional<Reservation> findById(int reservationId) {
        String sql = "SELECT * FROM Reservation WHERE reservation_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, reservationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding reservation: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Reservation> findByUserId(int userId) {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM Reservation WHERE user_id=? ORDER BY start_date DESC";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding reservations by user: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public List<Reservation> findAll() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT * FROM Reservation ORDER BY reservation_id DESC";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error listing reservations: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void updateStatus(int reservationId, String status) {
        String sql = "UPDATE Reservation SET status=? WHERE reservation_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, reservationId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating reservation status: " + e.getMessage(), e);
        }
    }

    @Override
    public void cancelReservation(int reservationId) {
        updateStatus(reservationId, "cancelled");
    }

    /**
     * Overlap check: a new booking [start, end) conflicts if:
     *   existing.start_date < end  AND  existing.end_date > start
     */
    @Override
    public boolean isCarAvailable(int carId, LocalDate startDate, LocalDate endDate) {
        String sql = "SELECT COUNT(*) FROM Reservation "
                   + "WHERE car_id=? AND status NOT IN ('cancelled') "
                   + "AND start_date < ? AND end_date > ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, carId);
            ps.setDate(2, Date.valueOf(endDate));
            ps.setDate(3, Date.valueOf(startDate));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking car availability: " + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean isCarAvailableExcluding(int carId, LocalDate startDate, LocalDate endDate, int excludeReservationId) {
        String sql = "SELECT COUNT(*) FROM Reservation "
                   + "WHERE car_id=? AND status NOT IN ('cancelled') "
                   + "AND reservation_id != ? "
                   + "AND start_date < ? AND end_date > ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, carId);
            ps.setInt(2, excludeReservationId);
            ps.setDate(3, Date.valueOf(endDate));
            ps.setDate(4, Date.valueOf(startDate));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) == 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking availability (excluding): " + e.getMessage(), e);
        }
        return false;
    }
}
