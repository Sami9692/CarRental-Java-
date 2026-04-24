package com.carrental.dao.impl;

import com.carrental.dao.InsuranceDAO;
import com.carrental.model.InsurancePrice;
import com.carrental.model.InsuranceType;
import com.carrental.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InsuranceDAOImpl implements InsuranceDAO {

    private Connection getConn() {
        return DBConnection.getInstance().getConnection();
    }

    @Override
    public List<InsuranceType> findAllTypes() {
        List<InsuranceType> list = new ArrayList<>();
        String sql = "SELECT * FROM Insurance_Type ORDER BY insurance_type_id";
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new InsuranceType(
                    rs.getInt("insurance_type_id"),
                    rs.getString("type_name"),
                    rs.getString("coverage")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing insurance types: " + e.getMessage(), e);
        }
        return list;
    }

    @Override
    public Optional<InsuranceType> findTypeById(int typeId) {
        String sql = "SELECT * FROM Insurance_Type WHERE insurance_type_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, typeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(new InsuranceType(
                    rs.getInt("insurance_type_id"),
                    rs.getString("type_name"),
                    rs.getString("coverage")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding insurance type: " + e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<InsurancePrice> findPriceByTypeId(int typeId) {
        String sql = "SELECT * FROM Insurance_Price WHERE insurance_type_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setInt(1, typeId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(new InsurancePrice(
                    rs.getInt("price_id"),
                    rs.getInt("insurance_type_id"),
                    rs.getBigDecimal("daily_price")
                ));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error finding insurance price: " + e.getMessage(), e);
        }
        return Optional.empty();
    }
}
