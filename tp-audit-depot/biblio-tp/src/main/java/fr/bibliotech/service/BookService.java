package fr.bibliotech.service;

import fr.bibliotech.model.Book;
import fr.bibliotech.repository.BookRepository;

import java.sql.SQLException;
import java.util.List;

/**
 * Règles métier liées aux livres du catalogue.
 */
public class BookService {

    private final BookRepository bookRepository = new BookRepository();

    public List<Book> list(BookRepository.SortField sortField) throws SQLException {
        return bookRepository.findAll(sortField);
    }

    public long addBook(String title, String author, int year) throws SQLException {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Le titre est obligatoire");
        }
        if (title.length() > 200) {
            throw new IllegalArgumentException("Le titre ne doit pas dépasser 200 caractères");
        }
        if (author == null || author.isBlank()) {
            throw new IllegalArgumentException("L'auteur est obligatoire");
        }
        if (author.length() > 200) {
            throw new IllegalArgumentException("L'auteur ne doit pas dépasser 200 caractères");
        }
        if (year < 1450 || year > 2100) {
            throw new IllegalArgumentException("L'année de publication est invalide");
        }
        return bookRepository.insert(title, author, year);
    }
}
