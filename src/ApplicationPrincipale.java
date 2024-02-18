import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class ApplicationPrincipale {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean utilisateurConnecte = false;

        while (!utilisateurConnecte) {
            afficherMenu();

            int choix = scanner.nextInt();
            scanner.nextLine(); // Consommer la nouvelle ligne restante après le choix numérique

            switch (choix) {
                case 1:
                    utilisateurConnecte = seConnecter(scanner);
                    break;
                case 2:
                    creerCompte(scanner);
                    break;
                case 3:
                    System.out.println("Au revoir !");
                    System.exit(0);
                default:
                    System.out.println("Choix invalide. Veuillez sélectionner une option valide.");
            }
        }
    }

    private static void afficherMenu() {
        System.out.println("1. Se connecter");
        System.out.println("2. Créer un compte");
        System.out.println("3. Quitter");
        System.out.println("Choisissez une option : ");
    }

    private static boolean seConnecter(Scanner scanner) {
        System.out.println("Entrez votre nom d'utilisateur : ");
        String username = scanner.nextLine();

        System.out.println("Entrez votre mot de passe : ");
        String password = scanner.nextLine();

        try (Connection connection = ConnexionMySQL.obtenirConnexion()) {
            if (verifierUtilisateur(connection, username, password)) {
                System.out.println("Connexion réussie !");
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

    private static void creerCompte(Scanner scanner) {
        System.out.println("Entrez votre nom d'utilisateur : ");
        String username = scanner.nextLine();

        System.out.println("Entrez votre mot de passe : ");
        String password = scanner.nextLine();

        try (Connection connection = ConnexionMySQL.obtenirConnexion()) {
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

    private static boolean verifierUtilisateur(Connection connection, String username, String password) throws SQLException {
        String query = "SELECT * FROM users WHERE nom_utilisateur = ? AND mot_de_passe = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
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

    private static boolean creerCompte(Connection connection, String username, String password) throws SQLException {
        if (verifierUtilisateurExistant(connection, username)) {
            System.out.println("Le nom d'utilisateur existe déjà. Choisissez un autre nom.");
            return false;
        }

        String insertQuery = "INSERT INTO users (nom_utilisateur, mot_de_passe) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);

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
}
