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

public class LibraryApplication {
    private static final String DB_URL = "jdbc:sqlite:C:/Users/user/Desktop/software/sqlite_database/Library.db";

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            Statement stmt = conn.createStatement();

            // Create the Books table
            stmt.execute("CREATE TABLE IF NOT EXISTS Books ("
                    + "book_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "title TEXT, "
                    + "author TEXT, "
                    + "publisher TEXT, "
                    + "year_published INTEGER, "
                    + "isbn TEXT, "
                    + "available_copies INTEGER)");

            // Step 1: Insert two records
            createBook(conn, "The Catcher in the Rye", "J.D. Salinger", "Little, Brown and Company", 1951, "978-0-316-76948-0", 10);
            createBook(conn, "To Kill a Mockingbird", "Harper Lee", "J.B. Lippincott & Co.", 1960, "978-0-06-112008-4", 12);

            System.out.println("\n--- Table After Insertion ---");
            readAllBooks(conn);

            // Step 2: Update one record
            updateBook(conn, 1, "The Catcher in the Rye", "J.D. Salinger", "Little, Brown and Company", 1951, "978-0-316-76948-0", 15);

            System.out.println("\n--- Table After Updating Book ID 1 ---");
            readAllBooks(conn);

            // Step 3: Delete one record
            deleteBook(conn, 2);

            System.out.println("\n--- Table After Deleting Book ID 2 ---");
            readAllBooks(conn);

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // CREATE Book
    public static void createBook(Connection conn, String title, String author, String publisher, int yearPublished, String isbn, int availableCopies) {
        String sql = "INSERT INTO Books (title, author, publisher, year_published, isbn, available_copies) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, publisher);
            pstmt.setInt(4, yearPublished);
            pstmt.setString(5, isbn);
            pstmt.setInt(6, availableCopies);
            pstmt.executeUpdate();
            System.out.println("Book inserted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // READ All Books
    public static void readAllBooks(Connection conn) {
        String sql = "SELECT * FROM Books";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                System.out.println("Book ID: " + rs.getInt("book_id"));
                System.out.println("Title: " + rs.getString("title"));
                System.out.println("Author: " + rs.getString("author"));
                System.out.println("Publisher: " + rs.getString("publisher"));
                System.out.println("Year Published: " + rs.getInt("year_published"));
                System.out.println("ISBN: " + rs.getString("isbn"));
                System.out.println("Available Copies: " + rs.getInt("available_copies"));
                System.out.println("--------------------------");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // UPDATE Book
    public static void updateBook(Connection conn, int bookId, String title, String author, String publisher, int yearPublished, String isbn, int availableCopies) {
        String sql = "UPDATE Books SET title = ?, author = ?, publisher = ?, year_published = ?, isbn = ?, available_copies = ? WHERE book_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, author);
            pstmt.setString(3, publisher);
            pstmt.setInt(4, yearPublished);
            pstmt.setString(5, isbn);
            pstmt.setInt(6, availableCopies);
            pstmt.setInt(7, bookId);
            pstmt.executeUpdate();
            System.out.println("Book updated successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // DELETE Book
    public static void deleteBook(Connection conn, int bookId) {
        String sql = "DELETE FROM Books WHERE book_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
            System.out.println("Book deleted successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
