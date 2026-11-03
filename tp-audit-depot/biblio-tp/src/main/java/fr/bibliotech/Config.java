package fr.bibliotech;

/**
 * Paramètres de connexion à la base de données et à l'API de notification.
 */
public final class Config {

    public static final String DB_URL = "jdbc:h2:mem:bibliotech;DB_CLOSE_DELAY=-1";
    public static final String DB_USER = "sa";
    public static final String DB_PASSWORD = "Biblio#2023!";
    public static final String NOTIFICATION_API_KEY = "bk_7f3a91c2d4e85b60";

    private Config() {
    }
}
