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

public class TransactionManager {
    private final Connection conn;

    public TransactionManager(Connection conn) {
        this.conn = conn;
    }

    // ISSUE Book (Create Transaction)
    public void issueBook(int bookId, int memberId, String issueDate) {
        String sql = "INSERT INTO Transactions (book_id, member_id, issue_date) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, memberId);
            pstmt.setString(3, issueDate);

            pstmt.executeUpdate();
            System.out.println("Book issued successfully");

            // Decrease the available copies
            String updateSql = "UPDATE Books SET available_copies = available_copies - 1 WHERE book_id = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setInt(1, bookId);
                updateStmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // RETURN Book (Update Transaction)
    public void returnBook(int transactionId, String returnDate) {
        String sql = "UPDATE Transactions SET return_date = ? WHERE transaction_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, returnDate);
            pstmt.setInt(2, transactionId);
            pstmt.executeUpdate();
            System.out.println("Book returned successfully");

            // Increase the available copies
            String getBookIdSql = "SELECT book_id FROM Transactions WHERE transaction_id = ?";
            try (PreparedStatement bookStmt = conn.prepareStatement(getBookIdSql)) {
                bookStmt.setInt(1, transactionId);
                ResultSet rs = bookStmt.executeQuery();
                if (rs.next()) {
                    int bookId = rs.getInt("book_id");
                    String updateSql = "UPDATE Books SET available_copies = available_copies + 1 WHERE book_id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setInt(1, bookId);
                        updateStmt.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ All Transactions
    public void readAllTransactions() {
        String sql = "SELECT * FROM Transactions";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println("Transaction ID: " + rs.getInt("transaction_id"));
                System.out.println("Book ID: " + rs.getInt("book_id"));
                System.out.println("Member ID: " + rs.getInt("member_id"));
                System.out.println("Issue Date: " + rs.getString("issue_date"));
                System.out.println("Return Date: " + rs.getString("return_date"));
                System.out.println("--------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String dbUrl = "jdbc:sqlite:C:/Users/user/Desktop/software/sqlite_database/Library.db";
        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            // Create Transactions table
            Statement stmt = conn.createStatement();
            stmt.execute("CREATE TABLE IF NOT EXISTS Transactions ("
                    + "transaction_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "book_id INTEGER, "
                    + "member_id INTEGER, "
                    + "issue_date DATE, "
                    + "return_date DATE, "
                    + "FOREIGN KEY(book_id) REFERENCES Books(book_id), "
                    + "FOREIGN KEY(member_id) REFERENCES Members(member_id))");

            // Transaction operations
            TransactionManager transactionManager = new TransactionManager(conn);
            transactionManager.issueBook(1, 1, "2025-01-28");

            System.out.println("\n--- Transactions Table After Issuing a Book ---");
            transactionManager.readAllTransactions();

            transactionManager.returnBook(1, "2025-02-10");

            System.out.println("\n--- Transactions Table After Returning the Book ---");
            transactionManager.readAllTransactions();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

