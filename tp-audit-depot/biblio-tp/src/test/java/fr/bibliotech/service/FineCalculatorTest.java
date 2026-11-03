package fr.bibliotech.service;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FineCalculatorTest {

    @Disabled
    @Test
    void computeFineInCentsCalculeLaPenaliteAttendue() {
        FineCalculator calculator = new FineCalculator();

        assertEquals(150, calculator.computeFineInCents(3));
    }
}
