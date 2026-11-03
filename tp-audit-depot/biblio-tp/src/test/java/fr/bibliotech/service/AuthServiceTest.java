package fr.bibliotech.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AuthServiceTest {

    private static final String SAMPLE_PASSWORD = "test-password-not-real";

    @Test
    void hashProduitToujoursLaMemeEmpreinte() {
        AuthService authService = new AuthService();

        String firstHash = authService.hash(SAMPLE_PASSWORD);
        String secondHash = authService.hash(SAMPLE_PASSWORD);

        assertNotNull(firstHash);
        assertEquals(firstHash, secondHash);
    }
}
