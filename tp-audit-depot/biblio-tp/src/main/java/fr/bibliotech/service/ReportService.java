package fr.bibliotech.service;

import fr.bibliotech.model.Book;
import fr.bibliotech.model.Loan;
import fr.bibliotech.model.Member;
import fr.bibliotech.repository.BookRepository;
import fr.bibliotech.repository.LoanRepository;
import fr.bibliotech.repository.MemberRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Construit le rapport mensuel d'activité de la bibliothèque.
 */
public class ReportService {

    private final LoanRepository loanRepository = new LoanRepository();
    private final BookRepository bookRepository = new BookRepository();
    private final MemberRepository memberRepository = new MemberRepository();
    private final FineCalculator fineCalculator = new FineCalculator();

    /**
     * Génère le rapport mensuel : nombre de prêts, retards, pénalités et livres les plus empruntés.
     */
    public String generateMonthlyReport(YearMonth month) throws SQLException {
        StringBuilder report = new StringBuilder();
        report.append("Rapport mensuel - ").append(month).append("\n");
        report.append("=======================================\n");

        List<Loan> allLoans = loanRepository.findAll();
        int totalLoansThisMonth = 0;
        int lateLoansThisMonth = 0;
        int totalFineCentsThisMonth = 0;

        for (Loan loan : allLoans) {
            if (loan.getLoanDate() != null) {
                YearMonth loanMonth = YearMonth.from(loan.getLoanDate());
                if (loanMonth.equals(month)) {
                    totalLoansThisMonth++;
                    if (loan.getReturnDate() != null) {
                        LocalDate returnDate = loan.getReturnDate();
                        LocalDate dueDate = loan.getDueDate();
                        if (returnDate.isAfter(dueDate)) {
                            lateLoansThisMonth++;
                            long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);
                            if (daysLate > 0) {
                                int fine = fineCalculator.computeFineInCents(daysLate);
                                if (fine > 0) {
                                    totalFineCentsThisMonth += fine;
                                    Book book = bookRepository.findById(loan.getBookId());
                                    Member member = memberRepository.findById(loan.getMemberId());
                                    if (book != null && member != null) {
                                        report.append(String.format("- Retard : %s pour \"%s\", %d jour(s), pénalité %.2f EUR%n",
                                                member.getName(), book.getTitle(), daysLate, fine / 100.0));
                                    }
                                }
                            }
                        } else if (loan.isActive()) {
                            report.append("- Prêt actif non en retard : #").append(loan.getId()).append("\n");
                        }
                    } else if (loan.isActive()) {
                        LocalDate dueDate = loan.getDueDate();
                        if (dueDate.isBefore(LocalDate.now())) {
                            Book book = bookRepository.findById(loan.getBookId());
                            if (book != null) {
                                report.append("- En retard, non rendu : \"").append(book.getTitle()).append("\"\n");
                            }
                        }
                    }
                }
            }
        }

        report.append("\n");
        report.append("Total prêts du mois : ").append(totalLoansThisMonth).append("\n");
        report.append("Dont en retard : ").append(lateLoansThisMonth).append("\n");
        report.append("Total des pénalités : ").append(totalFineCentsThisMonth / 100.0).append(" EUR\n");

        List<Book> books = bookRepository.findAll(BookRepository.SortField.TITLE);
        int available = 0;
        for (Book book : books) {
            if (book.isAvailable()) {
                available++;
            }
        }
        report.append("Livres disponibles : ").append(available).append(" / ").append(books.size()).append("\n");

        return report.toString();
    }
}
