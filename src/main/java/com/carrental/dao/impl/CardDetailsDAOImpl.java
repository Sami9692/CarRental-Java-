package com.carrental.dao.impl;

import com.carrental.dao.CardDetailsDAO;
import com.carrental.model.CardDetails;
import com.carrental.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardDetailsDAOImpl implements CardDetailsDAO {

    private Connection getConn() {
        return DBConnection.getInstance().getConnection();
    }

    private CardDetails mapRow(ResultSet rs) throws SQLException {
        return new CardDetails(
            rs.getInt("card_id"),
            rs.getInt("user_id"),
            rs.getString("card_number"),
            rs.getString("card_holder"),
            rs.getString("expiry_date"),
            rs.getString("card_type")
        );
    }

    @Override
    public int addCard(CardDetails card) {
        String sql = "INSERT INTO Card_Details (user_id, card_number, card_holder, expiry_date, card_type) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, card.getUserId());
            ps.setString(2, card.getCardNumber());
            ps.setString(3, card.getCardHolder());
            ps.setString(4, card.getExpiryDate());
            ps.setString(5, card.getCardType());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) { int id = keys.getInt(1); card.setCardId(id); return id; }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding card: " + e.getMessage(), e);
        }
        return -1;
    }

    @Override
    public Optional<CardDetails> findById(int cardId) {
        String sql = "SELECT * FROM Card_Details WHERE card_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, cardId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding card: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<CardDetails> findByUserId(int userId) {
        List<CardDetails> list = new ArrayList<>();
        String sql = "SELECT * FROM Card_Details WHERE user_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing cards: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void deleteCard(int cardId) {
        String sql = "DELETE FROM Card_Details WHERE card_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, cardId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting card: " + e.getMessage(), e);
        }
    }
}
