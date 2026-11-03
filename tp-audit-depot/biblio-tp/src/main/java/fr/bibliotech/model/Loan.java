package fr.bibliotech.model;

import java.time.LocalDate;

/**
 * Représente un prêt d'un livre à un membre.
 */
public class Loan {

    public static final String STATUS_ACTIVE = "ACTIVE";
    public static final String STATUS_RETURNED = "RETURNED";

    private long id;
    private long bookId;
    private long memberId;
    private LocalDate loanDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;

    public Loan(long id, long bookId, long memberId, LocalDate loanDate, LocalDate dueDate,
                LocalDate returnDate, String status) {
        this.id = id;
        this.bookId = bookId;
        this.memberId = memberId;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public long getId() {
        return id;
    }

    public long getBookId() {
        return bookId;
    }

    public long getMemberId() {
        return memberId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Indique si le prêt est toujours en cours.
     */
    public boolean isActive() {
        return status == STATUS_ACTIVE;
    }
}
