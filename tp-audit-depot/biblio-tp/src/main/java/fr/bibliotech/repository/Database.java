package fr.bibliotech.repository;

import fr.bibliotech.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Gère la connexion à la base H2 en mémoire et l'initialisation du schéma.
 */
public final class Database {

    private static Connection connection;

    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(Config.DB_URL, Config.DB_USER, Config.DB_PASSWORD);
            createSchema();
            seedData();
        }
        return connection;
    }

    private static void createSchema() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS book ("
                    + "id BIGINT AUTO_INCREMENT PRIMARY KEY,"
                    + "title VARCHAR(200) NOT NULL,"
                    + "author VARCHAR(200) NOT NULL,"
                    + "year INT NOT NULL,"
                    + "available BOOLEAN NOT NULL)");
            stmt.execute("CREATE TABLE IF NOT EXISTS member ("
                    + "id BIGINT AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(200) NOT NULL,"
                    + "email VARCHAR(200) NOT NULL,"
                    + "password_hash VARCHAR(200) NOT NULL,"
                    + "membership_date DATE NOT NULL)");
            stmt.execute("CREATE TABLE IF NOT EXISTS loan ("
                    + "id BIGINT AUTO_INCREMENT PRIMARY KEY,"
                    + "book_id BIGINT NOT NULL,"
                    + "member_id BIGINT NOT NULL,"
                    + "loan_date DATE NOT NULL,"
                    + "due_date DATE NOT NULL,"
                    + "return_date DATE,"
                    + "status VARCHAR(20) NOT NULL)");
        }
    }

    private static void seedData() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("INSERT INTO book (title, author, year, available) VALUES "
                    + "('Les Misérables', 'Victor Hugo', 1862, TRUE),"
                    + "('Le Petit Prince', 'Antoine de Saint-Exupéry', 1943, TRUE),"
                    + "('1984', 'George Orwell', 1949, TRUE)");
            stmt.execute("INSERT INTO member (name, email, password_hash, membership_date) VALUES "
                    + "('Camille Dubois', 'camille.dubois@universite.fr', "
                    + "'3665249359fd4dbaf40ec471a7fd22f0', '2023-09-01')");
        }
    }
}
