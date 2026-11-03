package fr.bibliotech.service;

import fr.bibliotech.model.Member;
import fr.bibliotech.repository.MemberRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * Règles métier liées aux membres de la bibliothèque.
 */
public class MemberService {

    private final MemberRepository memberRepository = new MemberRepository();
    private final AuthService authService = new AuthService();

    public List<Member> search(String name) throws SQLException {
        return memberRepository.findByName(name);
    }

    public long registerMember(String name, String email, String password) throws SQLException {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Le nom est obligatoire");
        }
        if (name.length() > 200) {
            throw new IllegalArgumentException("Le nom ne doit pas dépasser 200 caractères");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("L'email est obligatoire");
        }
        if (email.length() > 200) {
            throw new IllegalArgumentException("L'email ne doit pas dépasser 200 caractères");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("L'email est invalide");
        }
        return authService.register(name, email, password);
    }
}
