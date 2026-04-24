import com.carrental.util.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;

public class UpdateDb {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 1. Add image_url column
            try {
                stmt.execute("ALTER TABLE Car ADD COLUMN image_url VARCHAR(500)");
                System.out.println("Added image_url column to Car table.");
            } catch (SQLException e) {
                System.out.println("Column image_url might already exist: " + e.getMessage());
            }

            // 2. Update existing cars with realistic images
            stmt.executeUpdate("UPDATE Car SET image_url='https://images.unsplash.com/photo-1621007947382-bb3c3994e3fd?auto=format&fit=crop&q=80&w=800' WHERE make='Toyota' AND model='Camry'");
            stmt.executeUpdate("UPDATE Car SET image_url='https://images.unsplash.com/photo-1568844293986-8d0400ba4792?auto=format&fit=crop&q=80&w=800' WHERE make='Honda' AND model='CR-V'");
            stmt.executeUpdate("UPDATE Car SET image_url='https://images.unsplash.com/photo-1549399542-7e3f8b79c341?auto=format&fit=crop&q=80&w=800' WHERE make='Ford' AND model='Focus'");
            stmt.executeUpdate("UPDATE Car SET image_url='https://images.unsplash.com/photo-1555215695-3004980ad54e?auto=format&fit=crop&q=80&w=800' WHERE make='BMW' AND model='5 Series'");
            stmt.executeUpdate("UPDATE Car SET image_url='https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&q=80&w=800' WHERE make='Maruti' AND model='Swift'"); // Used a generic nice car for swift

            // 3. Insert new premium cars
            String insertSql = "INSERT INTO Car (make, model, year, license_plate, status, daily_rate, type_id, location_id, image_url) VALUES (?, ?, ?, ?, 'available', ?, ?, 1, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                // Porsche 911
                ps.setString(1, "Porsche"); ps.setString(2, "911 Carrera"); ps.setInt(3, 2023); ps.setString(4, "POR-9111"); ps.setDouble(5, 250.00); ps.setInt(6, 4); ps.setString(7, "https://images.unsplash.com/photo-1503376710341-032ce9204cd1?auto=format&fit=crop&q=80&w=800");
                ps.addBatch();
                
                // Tesla Model 3
                ps.setString(1, "Tesla"); ps.setString(2, "Model 3"); ps.setInt(3, 2024); ps.setString(4, "TSL-3333"); ps.setDouble(5, 90.00); ps.setInt(6, 1); ps.setString(7, "https://images.unsplash.com/photo-1560958089-b8a1929cea89?auto=format&fit=crop&q=80&w=800");
                ps.addBatch();

                // Audi R8
                ps.setString(1, "Audi"); ps.setString(2, "R8 V10"); ps.setInt(3, 2022); ps.setString(4, "AUD-8888"); ps.setDouble(5, 300.00); ps.setInt(6, 4); ps.setString(7, "https://images.unsplash.com/photo-1603584173870-7f23fdae1b7a?auto=format&fit=crop&q=80&w=800");
                ps.addBatch();

                // Mercedes G-Class
                ps.setString(1, "Mercedes-Benz"); ps.setString(2, "G-Class"); ps.setInt(3, 2023); ps.setString(4, "MBZ-5555"); ps.setDouble(5, 200.00); ps.setInt(6, 2); ps.setString(7, "https://images.unsplash.com/photo-1520031441872-265e4ff70366?auto=format&fit=crop&q=80&w=800");
                ps.addBatch();

                int[] results = ps.executeBatch();
                System.out.println("Inserted " + results.length + " new premium cars.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
