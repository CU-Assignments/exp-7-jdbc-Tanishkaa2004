public class Student {
    private int studentID;
    private String name;
    private String department;
    private double marks;

    public Student(int studentID, String name, String department, double marks) {
        this.studentID = studentID;
        this.name = name;
        this.department = department;
        this.marks = marks;
    }

    public int getStudentID() { return studentID; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getMarks() { return marks; }
}


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentController {
    static final String URL = "jdbc:mysql://localhost:3306/your_database";
    static final String USER = "your_username";
    static final String PASSWORD = "your_password";
    private Connection conn;

    public StudentController() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO Students (StudentID, Name, Department, Marks) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, student.getStudentID());
        pstmt.setString(2, student.getName());
        pstmt.setString(3, student.getDepartment());
        pstmt.setDouble(4, student.getMarks());
        pstmt.executeUpdate();
    }

    public List<Student> getAllStudents() throws SQLException {
        List<Student> students = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Students");

        while (rs.next()) {
            students.add(new Student(rs.getInt("StudentID"), rs.getString("Name"),
                    rs.getString("Department"), rs.getDouble("Marks")));
        }
        return students;
    }
}

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class StudentManagement {
    public static void main(String[] args) {
        StudentController controller = new StudentController();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Add Student\n2. View Students\n3. Exit");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Department: ");
                    String department = scanner.nextLine();
                    System.out.print("Enter Marks: ");
                    double marks = scanner.nextDouble();

                    try {
                        controller.addStudent(new Student(id, name, department, marks));
                        System.out.println("Student added successfully!");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                case 2 -> {
                    try {
                        List<Student> students = controller.getAllStudents();
                        students.forEach(s -> System.out.println(s.getStudentID() + " | " + s.getName() + " | " +
                                s.getDepartment() + " | " + s.getMarks()));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                case 3 -> System.exit(0);
                default -> System.out.println("Invalid choice!");
            }
        }
    }
}

