package fr.bibliotech.repository;

import fr.bibliotech.model.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Accès aux livres du catalogue.
 */
public class BookRepository {

    /**
     * Champ de tri disponible pour la liste des livres.
     */
    public enum SortField {
        TITLE, AUTHOR, YEAR
    }

    public List<Book> findAll(SortField sortField) throws SQLException {
        String column = switch (sortField) {
            case TITLE -> "title";
            case AUTHOR -> "author";
            case YEAR -> "year";
        };
        String sql = "SELECT id, title, author, year, available FROM book ORDER BY " + column;
        List<Book> books = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                books.add(mapRow(rs));
            }
        }
        return books;
    }

    public long insert(String title, String author, int year) throws SQLException {
        String sql = "INSERT INTO book (title, author, year, available) VALUES (?, ?, ?, TRUE)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setInt(3, year);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getLong(1) : -1;
            }
        }
    }

    public Book findById(long id) throws SQLException {
        String sql = "SELECT id, title, author, year, available FROM book WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public void updateAvailability(long bookId, boolean available) throws SQLException {
        String sql = "UPDATE book SET available = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, available);
            ps.setLong(2, bookId);
            ps.executeUpdate();
        }
    }

    private Book mapRow(ResultSet rs) throws SQLException {
        return new Book(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getInt("year"),
                rs.getBoolean("available"));
    }
}
