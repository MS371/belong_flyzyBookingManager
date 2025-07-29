import java.sql.*;
import java.util.Scanner;

public class BookingManager {
    private static final String URL = "jdbc:mysql://localhost:3306/travel";
    private static final String USER = "root";
    private static final String PASSWORD = "yourpassword";

    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static void addBooking(String name, String destination, String date) {
        String query = "INSERT INTO bookings (customer_name, destination, date, status) VALUES (?, ?, ?, 'pending')";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, name);
            stmt.setString(2, destination);
            stmt.setDate(3, Date.valueOf(date));
            stmt.executeUpdate();
            System.out.println("✅ Booking added successfully!");
        } catch (SQLException e) {
            System.out.println("❌ Error adding booking: " + e.getMessage());
        }
    }

    public static void viewBookings() {
        String query = "SELECT * FROM bookings";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("\n--- All Bookings ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Destination: %s | Date: %s | Status: %s%n",
                        rs.getInt("id"), rs.getString("customer_name"),
                        rs.getString("destination"), rs.getDate("date"), rs.getString("status"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error viewing bookings: " + e.getMessage());
        }
    }

    public static void confirmBooking(int id) {
        String query = "UPDATE bookings SET status = 'confirmed' WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0)
                System.out.println("✅ Booking confirmed!");
            else
                System.out.println("⚠️ Booking ID not found.");
        } catch (SQLException e) {
            System.out.println("❌ Error updating booking: " + e.getMessage());
        }
    }

    public static void deleteBooking(int id) {
        String query = "DELETE FROM bookings WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();
            if (rows > 0)
                System.out.println("✅ Booking deleted!");
            else
                System.out.println("⚠️ Booking ID not found.");
        } catch (SQLException e) {
            System.out.println("❌ Error deleting booking: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n===== Travel Booking System =====");
            System.out.println("1. Add Booking");
            System.out.println("2. View All Bookings");
            System.out.println("3. Confirm Booking");
            System.out.println("4. Delete Booking");
            System.out.println("5. Exit");
            System.out.print("Enter choice: ");
            choice = sc.nextInt();
            sc.nextLine();  // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Destination: ");
                    String dest = sc.nextLine();
                    System.out.print("Enter Date (YYYY-MM-DD): ");
                    String date = sc.nextLine();
                    addBooking(name, dest, date);
                    break;
                case 2:
                    viewBookings();
                    break;
                case 3:
                    System.out.print("Enter Booking ID to confirm: ");
                    int id = sc.nextInt();
                    confirmBooking(id);
                    break;
                case 4:
                    System.out.print("Enter Booking ID to delete: ");
                    int did = sc.nextInt();
                    deleteBooking(did);
                    break;
                case 5:
                    System.out.println("👋 Exiting...");
                    break;
                default:
                    System.out.println("⚠️ Invalid choice. Try again.");
            }
        } while (choice != 5);
    }
}
