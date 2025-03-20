import java.sql.*;
import java.util.Scanner;

public class ProductCRUD {
    static final String URL = "jdbc:mysql://localhost:3306/your_database";
    static final String USER = "your_username";
    static final String PASSWORD = "your_password";
    static Connection conn;

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.println("1. Add Product\n2. View Products\n3. Update Product\n4. Delete Product\n5. Exit");
                int choice = scanner.nextInt();

                switch (choice) {
                    case 1 -> addProduct(scanner);
                    case 2 -> viewProducts();
                    case 3 -> updateProduct(scanner);
                    case 4 -> deleteProduct(scanner);
                    case 5 -> {
                        conn.close();
                        return;
                    }
                    default -> System.out.println("Invalid choice!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addProduct(Scanner scanner) throws SQLException {
        System.out.print("Enter Product Name: ");
        String name = scanner.next();
        System.out.print("Enter Price: ");
        double price = scanner.nextDouble();
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();

        String sql = "INSERT INTO Product (ProductName, Price, Quantity) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, name);
        pstmt.setDouble(2, price);
        pstmt.setInt(3, quantity);
        pstmt.executeUpdate();
        System.out.println("Product added successfully!");
    }

    public static void viewProducts() throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Product");

        while (rs.next()) {
            System.out.println(rs.getInt("ProductID") + " | " + rs.getString("ProductName") + " | " +
                    rs.getDouble("Price") + " | " + rs.getInt("Quantity"));
        }
    }

    public static void updateProduct(Scanner scanner) throws SQLException {
        System.out.print("Enter Product ID to update: ");
        int id = scanner.nextInt();
        System.out.print("Enter new Price: ");
        double price = scanner.nextDouble();

        String sql = "UPDATE Product SET Price = ? WHERE ProductID = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setDouble(1, price);
        pstmt.setInt(2, id);
        pstmt.executeUpdate();
        System.out.println("Product updated successfully!");
    }

    public static void deleteProduct(Scanner scanner) throws SQLException {
        System.out.print("Enter Product ID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM Product WHERE ProductID = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        pstmt.executeUpdate();
        System.out.println("Product deleted successfully!");
    }
}
