import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ConnexionMySQL {

    private static final String URL = "jdbc:mysql://localhost:3306/users";
    private static final String UTILISATEUR = "root";
    private static final String MOT_DE_PASSE = "test";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Entrez votre nom d'utilisateur : ");
        String username = scanner.nextLine();

        System.out.println("Entrez votre mot de passe : ");
        String password = scanner.nextLine();

        try {
            // Charger le driver JDBC
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Établir la connexion à la base de données
            Connection connection = DriverManager.getConnection(URL, UTILISATEUR, MOT_DE_PASSE);

            // Vérifier les informations de connexion
            if (verifierUtilisateur(connection, username, password)) {
                System.out.println("Connexion réussie !");
            } else {
                System.out.println("Nom d'utilisateur ou mot de passe incorrect.");
            }

            // Fermer la connexion
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean verifierUtilisateur(Connection connection, String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE nom_utilisateur = ? AND mot_de_passe = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }
}
