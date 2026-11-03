package fr.bibliotech.model;

/**
 * Représente un livre du catalogue de la bibliothèque.
 */
public class Book {

    private long id;
    private String title;
    private String author;
    private int year;
    private boolean available;

    public Book(long id, String title, String author, int year, boolean available) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.available = available;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getYear() {
        return year;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString() {
        return "#" + id + " - " + title + " (" + author + ", " + year + ")"
                + (available ? " [disponible]" : " [emprunté]");
    }
}
