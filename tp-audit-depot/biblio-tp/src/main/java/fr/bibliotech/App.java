package fr.bibliotech;

import fr.bibliotech.model.Book;
import fr.bibliotech.repository.BookRepository;
import fr.bibliotech.service.BookService;
import fr.bibliotech.service.LoanService;
import fr.bibliotech.service.MemberService;
import fr.bibliotech.service.ReportService;

import java.time.YearMonth;
import java.util.List;
import java.util.Scanner;

/**
 * Point d'entrée de l'application console BiblioTech.
 */
public class App {

    public static void main(String[] args) throws Exception {
        BookService bookService = new BookService();
        MemberService memberService = new MemberService();
        LoanService loanService = new LoanService();
        ReportService reportService = new ReportService();

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n--- BiblioTech ---");
            System.out.println("1. Lister les livres");
            System.out.println("2. Rechercher un membre");
            System.out.println("3. Générer le rapport du mois");
            System.out.println("0. Quitter");
            System.out.print("Choix : ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> {
                    List<Book> books = bookService.list(BookRepository.SortField.TITLE);
                    books.forEach(System.out::println);
                }
                case "2" -> {
                    System.out.print("Nom recherché : ");
                    String name = scanner.nextLine();
                    memberService.search(name).forEach(System.out::println);
                }
                case "3" -> System.out.println(reportService.generateMonthlyReport(YearMonth.now()));
                case "0" -> running = false;
                default -> System.out.println("Choix invalide");
            }
        }

        scanner.close();
    }
}
