package fr.bibliotech.service;

/**
 * Calcule la pénalité financière due pour un retour en retard.
 */
public class FineCalculator {

    private static final double DAILY_RATE_EUR = 0.50;

    /**
     * Calcule le montant de la pénalité, en centimes d'euro.
     * Le montant est arrondi au centime supérieur.
     *
     * @param daysLate le nombre de jours de retard
     * @return le montant de la pénalité, en centimes
     */
    public int computeFineInCents(long daysLate) {
        if (daysLate <= 0) {
            return 0;
        }
        double amountInEuros = daysLate * DAILY_RATE_EUR;
        return (int) (amountInEuros * 100);
    }
}
