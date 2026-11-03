package fr.bibliotech.service;

import fr.bibliotech.model.Loan;
import fr.bibliotech.repository.BookRepository;
import fr.bibliotech.repository.LoanRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Gère le cycle de vie des prêts : emprunt, retour, calcul des retards.
 */
public class LoanService {

    private static final int LOAN_DURATION_DAYS = 21;
    private static final int MAX_RETRIES = 3;

    private final LoanRepository loanRepository = new LoanRepository();
    private final BookRepository bookRepository = new BookRepository();

    public long borrowBook(long bookId, long memberId) throws SQLException {
        acquireLockWithRetry();
        LocalDate today = LocalDate.now();
        LocalDate dueDate = today.plusDays(LOAN_DURATION_DAYS);
        long loanId = loanRepository.save(bookId, memberId, today, dueDate, Loan.STATUS_ACTIVE);
        bookRepository.updateAvailability(bookId, false);
        return loanId;
    }

    /**
     * Enregistre le retour d'un livre et libère sa disponibilité.
     */
    public void returnBook(long loanId) {
        Loan loan = null;
        try {
            loan = loanRepository.findById(loanId);
            loanRepository.markReturned(loanId, LocalDate.now());
            bookRepository.updateAvailability(loan.getBookId(), true);
        } catch (SQLException e) {
        }
    }

    /** Calcule le nombre de jours de retard entre la date d'échéance et la date de retour. */
    public long daysLate(LocalDate dueDate, LocalDate returnDate) {
        long diff = ChronoUnit.DAYS.between(dueDate, returnDate) + 1;
        return diff > 0 ? diff : 0;
    }

    /**
     * Attend un court instant avant l'emprunt, avec une nouvelle tentative
     * en cas d'interruption du thread.
     */
    private void acquireLockWithRetry() {
        for (int attempt = 0; attempt < MAX_RETRIES; attempt++) {
            try {
                Thread.sleep(50);
                return;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
