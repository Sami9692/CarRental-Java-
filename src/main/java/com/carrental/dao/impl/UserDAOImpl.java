package com.carrental.dao.impl;

import com.carrental.dao.UserDAO;
import com.carrental.model.User;
import com.carrental.model.UserCredential;
import com.carrental.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAOImpl implements UserDAO {

    private Connection getConn() {
        return DBConnection.getInstance().getConnection();
    }

    private User mapUser(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("user_id"),
            rs.getString("first_name"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getDate("date_of_birth") != null ? rs.getDate("date_of_birth").toLocalDate() : null,
            rs.getString("license_number"),
            rs.getBoolean("is_admin")
        );
    }

    @Override
    public void registerUser(User user, UserCredential credential) {
        Connection conn = getConn();
        String insertUser = "INSERT INTO User (first_name, last_name, email, date_of_birth, license_number, is_admin) "
                          + "VALUES (?, ?, ?, ?, ?, ?)";
        String insertCred = "INSERT INTO User_Credential (user_id, username, password_hash) VALUES (?, ?, ?)";
        try {
            conn.setAutoCommit(false);

            // 1. Insert User
            try (PreparedStatement ps = conn.prepareStatement(insertUser, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, user.getFirstName());
                ps.setString(2, user.getLastName());
                ps.setString(3, user.getEmail());
                ps.setDate(4, user.getDateOfBirth() != null ? Date.valueOf(user.getDateOfBirth()) : null);
                ps.setString(5, user.getLicenseNumber());
                ps.setBoolean(6, user.isAdmin());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        int genId = keys.getInt(1);
                        user.setUserId(genId);
                        credential.setUserId(genId);
                    }
                }
            }

            // 2. Insert Credential
            try (PreparedStatement ps = conn.prepareStatement(insertCred)) {
                ps.setInt(1, credential.getUserId());
                ps.setString(2, credential.getUsername());
                ps.setString(3, credential.getPasswordHash());
                ps.executeUpdate();
            }

            conn.commit();
            System.out.println("[UserDAO] Registered user: " + user.getEmail());
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { /* ignore */ }
            throw new RuntimeException("Error registering user: " + e.getMessage(), e);
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { /* ignore */ }
        }
    }

    @Override
    public Optional<User> findById(int userId) {
        String sql = "SELECT * FROM User WHERE user_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by id: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM User WHERE email=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding user by email: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserCredential> findCredentialByUsername(String username) {
        String sql = "SELECT * FROM User_Credential WHERE username=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    UserCredential uc = new UserCredential(
                        rs.getInt("credential_id"),
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password_hash")
                    );
                    return Optional.of(uc);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding credential: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM User ORDER BY user_id";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapUser(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error listing users: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE User SET first_name=?, last_name=?, email=?, license_number=? WHERE user_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getLicenseNumber());
            ps.setInt(5, user.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating user: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteUser(int userId) {
        String sql = "DELETE FROM User WHERE user_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }
}
