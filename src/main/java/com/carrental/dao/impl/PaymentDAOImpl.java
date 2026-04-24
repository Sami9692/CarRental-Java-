package com.carrental.dao.impl;

import com.carrental.dao.PaymentDAO;
import com.carrental.model.Payment;
import com.carrental.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentDAOImpl implements PaymentDAO {

    private Connection getConn() {
        return DBConnection.getInstance().getConnection();
    }

    private Payment mapRow(ResultSet rs) throws SQLException {
        return new Payment(
            rs.getInt("payment_id"),
            rs.getInt("reservation_id"),
            rs.getInt("card_id"),
            rs.getBigDecimal("amount"),
            rs.getTimestamp("payment_date") != null
                ? rs.getTimestamp("payment_date").toLocalDateTime() : null,
            rs.getString("payment_status"),
            rs.getString("payment_method")
        );
    }

    @Override
    public int processPayment(Payment payment) {
        String sql = "INSERT INTO Payment (reservation_id, card_id, amount, payment_date, payment_status, payment_method) "
                   + "VALUES (?, ?, ?, NOW(), ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, payment.getReservationId());
            ps.setInt(2, payment.getCardId());
            ps.setBigDecimal(3, payment.getAmount());
            ps.setString(4, payment.getPaymentStatus());
            ps.setString(5, payment.getPaymentMethod());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int id = keys.getInt(1);
                    payment.setPaymentId(id);
                    return id;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error processing payment: " + e.getMessage(), e);
        }
        return -1;
    }

    @Override
    public Optional<Payment> findById(int paymentId) {
        String sql = "SELECT * FROM Payment WHERE payment_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding payment: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Payment> findByReservationId(int reservationId) {
        String sql = "SELECT * FROM Payment WHERE reservation_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, reservationId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding payment by reservation: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<Payment> findByUserId(int userId) {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT p.* FROM Payment p "
                   + "JOIN Reservation r ON p.reservation_id = r.reservation_id "
                   + "WHERE r.user_id=? ORDER BY p.payment_date DESC";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing payments by user: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void updatePaymentStatus(int paymentId, String status) {
        String sql = "UPDATE Payment SET payment_status=? WHERE payment_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, paymentId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating payment status: " + e.getMessage(), e);
        }
    }
}
