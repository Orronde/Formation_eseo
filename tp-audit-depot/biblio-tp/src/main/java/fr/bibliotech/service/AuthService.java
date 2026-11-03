package fr.bibliotech.service;

import fr.bibliotech.repository.MemberRepository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HexFormat;

/**
 * Gère l'inscription et la connexion des membres.
 */
public class AuthService {

    private final MemberRepository memberRepository = new MemberRepository();

    public long register(String name, String email, String password) throws SQLException {
        return memberRepository.save(name, email, hash(password));
    }

    public boolean login(String email, String password) throws SQLException {
        String storedHash = memberRepository.findPasswordHashByEmail(email);
        if (storedHash == null) {
            return false;
        }
        return storedHash.equals(hash(password));
    }

    /**
     * Calcule l'empreinte du mot de passe avant stockage en base.
     */
    public String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Algorithme de hachage indisponible", e);
        }
    }
}
