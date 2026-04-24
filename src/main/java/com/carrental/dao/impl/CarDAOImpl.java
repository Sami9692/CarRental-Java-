package com.carrental.dao.impl;

import com.carrental.dao.CarDAO;
import com.carrental.model.Car;
import com.carrental.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CarDAOImpl implements CarDAO {

    private Connection getConn() {
        return DBConnection.getInstance().getConnection();
    }

    private Car mapRow(ResultSet rs) throws SQLException {
        return new Car(
            rs.getInt("car_id"),
            rs.getString("make"),
            rs.getString("model"),
            rs.getInt("year"),
            rs.getString("license_plate"),
            rs.getString("status"),
            rs.getBigDecimal("daily_rate"),
            rs.getInt("type_id"),
            rs.getInt("location_id"),
            rs.getString("image_url")
        );
    }

    @Override
    public void addCar(Car car) {
        String sql = "INSERT INTO Car (make, model, year, license_plate, status, daily_rate, type_id, location_id, image_url) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, car.getMake());
            ps.setString(2, car.getModel());
            ps.setInt(3, car.getYear());
            ps.setString(4, car.getLicensePlate());
            ps.setString(5, car.getStatus());
            ps.setBigDecimal(6, car.getDailyRate());
            ps.setInt(7, car.getTypeId());
            ps.setInt(8, car.getLocationId());
            ps.setString(9, car.getImageUrl());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) car.setCarId(keys.getInt(1));
            }
            System.out.println("[CarDAO] Car added: " + car);
        } catch (SQLException e) {
            throw new RuntimeException("Error adding car: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateCar(Car car) {
        String sql = "UPDATE Car SET make=?, model=?, year=?, license_plate=?, status=?, "
                   + "daily_rate=?, type_id=?, location_id=?, image_url=? WHERE car_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, car.getMake());
            ps.setString(2, car.getModel());
            ps.setInt(3, car.getYear());
            ps.setString(4, car.getLicensePlate());
            ps.setString(5, car.getStatus());
            ps.setBigDecimal(6, car.getDailyRate());
            ps.setInt(7, car.getTypeId());
            ps.setInt(8, car.getLocationId());
            ps.setString(9, car.getImageUrl());
            ps.setInt(10, car.getCarId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating car: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteCar(int carId) {
        String sql = "DELETE FROM Car WHERE car_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, carId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting car: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Car> findById(int carId) {
        String sql = "SELECT * FROM Car WHERE car_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, carId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding car by id: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Car> findAll() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM Car ORDER BY car_id";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) cars.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error listing cars: " + e.getMessage(), e);
        }
        return cars;
    }

    @Override
    public List<Car> findAvailableCars() {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT * FROM Car WHERE status='available' ORDER BY car_id";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) cars.add(mapRow(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error listing available cars: " + e.getMessage(), e);
        }
        return cars;
    }

    /**
     * Returns cars that are not involved in any overlapping reservation
     * for the given date range. Overlap condition:
     *   existing.start_date < requested.end_date
     *   AND existing.end_date > requested.start_date
     */
    @Override
    public List<Car> findAvailableCarsByDates(LocalDate startDate, LocalDate endDate) {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT c.* FROM Car c "
                   + "WHERE c.status != 'maintenance' "
                   + "AND c.car_id NOT IN ("
                   + "  SELECT r.car_id FROM Reservation r "
                   + "  WHERE r.status NOT IN ('cancelled') "
                   + "  AND r.start_date < ? AND r.end_date > ?"
                   + ") ORDER BY c.car_id";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(endDate));
            ps.setDate(2, Date.valueOf(startDate));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) cars.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding available cars by dates: " + e.getMessage(), e);
        }
        return cars;
    }

    @Override
    public void updateCarStatus(int carId, String status) {
        String sql = "UPDATE Car SET status=? WHERE car_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, carId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating car status: " + e.getMessage(), e);
        }
    }
}
