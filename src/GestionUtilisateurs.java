import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class GestionUtilisateurs {

    public boolean seConnecter(Scanner scanner, Connection connection) {
        System.out.println("Entrez votre nom d'utilisateur : ");
        String username = scanner.nextLine();

        System.out.println("Entrez votre mot de passe : ");
        String password = scanner.nextLine();

        try {
            if (verifierMotDePasse(connection, username, password)) {
                System.out.println("Connexion réussie !");
                menuUtilisateurConnecte(scanner, connection, username);
                return true;
            } else {
                System.out.println("Nom d'utilisateur ou mot de passe incorrect.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void creerCompte(Scanner scanner, Connection connection) {
        System.out.println("Entrez votre nom d'utilisateur : ");
        String username = scanner.nextLine();

        System.out.println("Entrez votre mot de passe : ");
        String password = scanner.nextLine();

        try {
            if (verifierListeBlanche(connection, username)) {
                if (creerCompte(connection, username, password)) {
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

    public void menuUtilisateurConnecte(Scanner scanner, Connection connection, String username) {
        boolean continuer = true;

        while (continuer) {
            afficherMenuUtilisateurConnecte();

            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne restante après le choix numérique

            switch (choix) {
                case 1:
                    afficherListeUtilisateurs(connection);
                    break;
                case 2:
                    modifierInformationsPersonnelles(connection, username, scanner);
                    break;
                case 3:
                    System.out.println("Déconnexion...");
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez sélectionner une option valide.");
            }
        }
    }

    private void afficherMenuUtilisateurConnecte() {
        System.out.println("1. Voir la liste des utilisateurs connectés");
        System.out.println("2. Modifier vos informations personnelles");
        System.out.println("3. Déconnexion");
        System.out.println("Choisissez une option : ");
    }

    private void afficherListeUtilisateurs(Connection connection) {
        try {
            String query = "SELECT * FROM users";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                ResultSet resultSet = preparedStatement.executeQuery();

                System.out.println("Liste des utilisateurs :");
                while (resultSet.next()) {
                    String username = resultSet.getString("nom_utilisateur");
                    System.out.println("- " + username);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void modifierInformationsPersonnelles(Connection connection, String username, Scanner scanner) {
        // Implémentez la logique pour modifier les informations personnelles ici
        // Vous pouvez demander à l'utilisateur de saisir de nouvelles informations et les mettre à jour dans la base de données
        System.out.println("Fonction de modification des informations personnelles à implémenter.");
    }

    private boolean creerCompte(Connection connection, String username, String password) throws SQLException {
        if (verifierUtilisateurExistant(connection, username)) {
            System.out.println("Le nom d'utilisateur existe déjà. Choisissez un autre nom.");
            return false;
        }

        // Crypter le mot de passe avec BCrypt
        String motDePasseCrypte = BCrypt.hashpw(password, BCrypt.gensalt());

        String insertQuery = "INSERT INTO users (nom_utilisateur, mot_de_passe) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, motDePasseCrypte);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    private boolean verifierMotDePasse(Connection connection, String username, String password) throws SQLException {
        String query = "SELECT mot_de_passe FROM users WHERE nom_utilisateur = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String motDePasseCrypte = resultSet.getString("mot_de_passe");
                return BCrypt.checkpw(password, motDePasseCrypte);
            }
            return false;
        }
    }

    private boolean verifierListeBlanche(Connection connection, String username) throws SQLException {
        String query = "SELECT * FROM whitelist WHERE nom_utilisateur = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }
    public void supprimerCompte(Connection connection, String username) {
        try {
            String deleteQuery = "DELETE FROM users WHERE nom_utilisateur = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setString(1, username);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Compte supprimé avec succès !");
                } else {
                    System.out.println("Erreur lors de la suppression du compte.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private boolean verifierUtilisateurExistant(Connection connection, String username) throws SQLException {
        String query = "SELECT * FROM users WHERE nom_utilisateur = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }
}
