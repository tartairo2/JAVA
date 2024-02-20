import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionMySQL {

    private static final String URL = "jdbc:mysql://localhost:3306/users";
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "test";

    public static Connection obtenirConnexion() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Erreur lors du chargement du driver JDBC", e);
        }
    }
}
