import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UtilisateurService {

    public static boolean seConnecter(Connection connection, Scanner scanner) {
        try {
            System.out.println("Entrez votre adresse e-mail : ");
            String email = scanner.nextLine();

            System.out.println("Entrez votre mot de passe : ");
            String password = scanner.nextLine();

            // Vérifier les informations de connexion
            if (verifierInformationsConnexion(connection, email, password)) {
                System.out.println("Connexion réussie !");
                return true;
            } else {
                System.out.println("Adresse e-mail ou mot de passe incorrect.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void creerCompte(Connection connection, Scanner scanner) {
        try {
            System.out.println("Entrez votre nom d'utilisateur : ");
            String username = scanner.nextLine();

            // Vérifier si le nom d'utilisateur existe déjà
            if (verifierUtilisateurExistant(connection, username)) {
                System.out.println("Le nom d'utilisateur existe déjà. Choisissez un autre nom.");
            } else {
                System.out.println("Entrez votre adresse e-mail : ");
                String email = scanner.nextLine();

                System.out.println("Entrez votre mot de passe : ");
                String password = scanner.nextLine();

                // Crypter le mot de passe avec BCrypt
                String motDePasseCrypte = BCrypt.hashpw(password, BCrypt.gensalt());

                // Insérer le nouvel utilisateur dans la base de données
                String insertQuery = "INSERT INTO users (nom_utilisateur, email, mot_de_passe) VALUES (?, ?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, email);
                    preparedStatement.setString(3, motDePasseCrypte);

                    int rowsAffected = preparedStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Compte créé avec succès !");
                    } else {
                        System.out.println("Erreur lors de la création du compte.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void voirUtilisateurs(Connection connection) {
        try {
            String query = "SELECT nom_utilisateur, email FROM users";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                System.out.println("Liste des utilisateurs :");
                while (resultSet.next()) {
                    String username = resultSet.getString("nom_utilisateur");
                    String email = resultSet.getString("email");

                    System.out.println("Nom d'utilisateur : " + username + ", Email : " + email);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Autres méthodes de la classe

    private static boolean verifierInformationsConnexion(Connection connection, String email, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String motDePasseCrypte = resultSet.getString("mot_de_passe");
                return BCrypt.checkpw(password, motDePasseCrypte);
            }
            return false;
        }
    }


    private static boolean verifierUtilisateurExistant(Connection connection, String username) throws SQLException {
        String query = "SELECT * FROM users WHERE nom_utilisateur = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }
}
