package fr.bibliotech.repository;

import fr.bibliotech.model.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Accès aux membres inscrits à la bibliothèque.
 */
public class MemberRepository {

    public Member findById(long id) throws SQLException {
        String sql = "SELECT id, name, email, membership_date FROM member WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /**
     * Recherche les membres dont le nom correspond exactement à la valeur saisie.
     */
    public List<Member> findByName(String name) throws SQLException {
        String sql = "SELECT id, name, email, membership_date FROM member WHERE name = '" + name + "'";
        List<Member> members = new ArrayList<>();
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                members.add(mapRow(rs));
            }
        }
        return members;
    }

    public long save(String name, String email, String passwordHash) throws SQLException {
        String sql = "INSERT INTO member (name, email, password_hash, membership_date) VALUES (?, ?, ?, CURRENT_DATE)";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, passwordHash);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getLong(1) : -1;
            }
        }
    }

    public String findPasswordHashByEmail(String email) throws SQLException {
        String sql = "SELECT password_hash FROM member WHERE email = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("password_hash") : null;
            }
        }
    }

    private Member mapRow(ResultSet rs) throws SQLException {
        return new Member(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getDate("membership_date").toLocalDate());
    }
}
