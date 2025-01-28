/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javaapplication1;

/**
 *
 * @author Piyanshu Mishra
 */



import java.sql.*;

public class Member {
    private final Connection conn;

    public Member(Connection conn) {
        this.conn = conn;
    }

    // CREATE Member
    public void createMember(String name, String email, String phone) {
        String sql = "INSERT INTO Members (name, email, phone) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, phone);
            pstmt.executeUpdate();
            System.out.println("Member inserted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ All Members
    public void readAllMembers() {
        String sql = "SELECT * FROM Members";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println("Member ID: " + rs.getInt("member_id"));
                System.out.println("Name: " + rs.getString("name"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Phone: " + rs.getString("phone"));
                System.out.println("--------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String dbUrl = "jdbc:sqlite:C:/Users/user/Desktop/software/sqlite_database/Library.db";
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            // Create Members table
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS Members ("
                    + "member_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "name TEXT, "
                    + "email TEXT UNIQUE, "
                    + "phone TEXT)");

            // Member operations
            Member memberManager = new Member(conn);
            memberManager.createMember("Mrinal", "mrinal@example.com", "1234567890");
            memberManager.createMember("Bob", "bob@example.com", "0987654321");

            System.out.println("\n--- Members Table ---");
            memberManager.readAllMembers();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
