import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class UtilisateurService {
    private static String utilisateurSession; // Ajouté pour stocker le nom d'utilisateur de la session

    public static boolean seConnecter(Connection connection, Scanner scanner) {
        System.out.println("Entrez votre adresse e-mail : ");
        String email = scanner.nextLine();

        System.out.println("Entrez votre mot de passe : ");
        String password = scanner.nextLine();

        try {
            if (verifierInformationsConnexion(connection, email, password)) {
                System.out.println("Connexion réussie !");
                setUsernameInSession(email); // Stocker le nom d'utilisateur de la session
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
        System.out.println("Entrez votre nom d'utilisateur : ");
        String username = scanner.nextLine();

        System.out.println("Entrez votre adresse e-mail : ");
        String email = scanner.nextLine();

        System.out.println("Entrez votre mot de passe : ");
        String password = scanner.nextLine();

        try {
            if (verifierListeBlanche(connection, username)) {
                if (creerCompte(connection, username, email, password)) {
                    System.out.println("Compte créé avec succès !");
                } else {
                    System.out.println("Erreur lors de la création du compte.");
                }
            } else {
                System.out.println("Vous n'êtes pas autorisé à créer un compte.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void modifierEmail(Connection connection, Scanner scanner) {
        try {
            String username = getUsernameFromSession();

            // Vérifier si l'utilisateur est connecté
            if (username != null) {
                System.out.println("Entrez votre nouvel e-mail : ");
                String newEmail = scanner.nextLine();

                // Mettre à jour l'e-mail dans la base de données
                String updateQuery = "UPDATE users SET email = ? WHERE nom_utilisateur = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
                    preparedStatement.setString(1, newEmail);
                    preparedStatement.setString(2, username);

                    int rowsAffected = preparedStatement.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("E-mail modifié avec succès !");
                    } else {
                        System.out.println("Erreur lors de la modification de l'e-mail.");
                    }
                }
            } else {
                System.out.println("Vous n'êtes pas connecté.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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

    private static boolean verifierListeBlanche(Connection connection, String username) throws SQLException {
        String query = "SELECT * FROM whitelist WHERE nom_utilisateur = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    private static boolean creerCompte(Connection connection, String username, String email, String password) throws SQLException {
        if (verifierUtilisateurExistant(connection, username)) {
            System.out.println("Le nom d'utilisateur existe déjà. Choisissez un autre nom.");
            return false;
        }

        // Crypter le mot de passe avec BCrypt
        String motDePasseCrypte = BCrypt.hashpw(password, BCrypt.gensalt());

        String insertQuery = "INSERT INTO users (nom_utilisateur, email, mot_de_passe) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, motDePasseCrypte);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
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

    private static void setUsernameInSession(String username) {
        utilisateurSession = username;
    }

    public static String getUsernameFromSession() {
        return utilisateurSession;
    }
}
