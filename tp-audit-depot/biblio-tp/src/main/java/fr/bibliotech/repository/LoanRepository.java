package fr.bibliotech.repository;

import fr.bibliotech.model.Loan;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Accès aux prêts de livres.
 */
public class LoanRepository {

    public long save(long bookId, long memberId, LocalDate loanDate, LocalDate dueDate, String status)
            throws SQLException {
        String sql = "INSERT INTO loan (book_id, member_id, loan_date, due_date, status) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, bookId);
            ps.setLong(2, memberId);
            ps.setDate(3, Date.valueOf(loanDate));
            ps.setDate(4, Date.valueOf(dueDate));
            ps.setString(5, status);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getLong(1) : -1;
            }
        }
    }

    public Loan findById(long id) throws SQLException {
        String sql = "SELECT id, book_id, member_id, loan_date, due_date, return_date, status "
                + "FROM loan WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public List<Loan> findAll() throws SQLException {
        String sql = "SELECT id, book_id, member_id, loan_date, due_date, return_date, status FROM loan";
        List<Loan> loans = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                loans.add(mapRow(rs));
            }
        }
        return loans;
    }

    public void markReturned(long loanId, LocalDate returnDate) throws SQLException {
        String sql = "UPDATE loan SET return_date = ?, status = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(returnDate));
            ps.setString(2, Loan.STATUS_RETURNED);
            ps.setLong(3, loanId);
            ps.executeUpdate();
        }
    }

    private Loan mapRow(ResultSet rs) throws SQLException {
        Date returnDate = rs.getDate("return_date");
        return new Loan(
                rs.getLong("id"),
                rs.getLong("book_id"),
                rs.getLong("member_id"),
                rs.getDate("loan_date").toLocalDate(),
                rs.getDate("due_date").toLocalDate(),
                returnDate == null ? null : returnDate.toLocalDate(),
                rs.getString("status"));
    }
}
