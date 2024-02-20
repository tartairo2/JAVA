import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ApplicationPrincipale {

    public static void voirUtilisateurs(Connection connection) {
        String query = "SELECT * FROM users";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String username = resultSet.getString("nom_utilisateur");
                String email = resultSet.getString("email");
                System.out.println("Utilisateur : " + username + ", Email : " + email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean utilisateurConnecte = false;
        Connection connection = null;

        try {
            connection = ConnexionMySQL.obtenirConnexion();

            while (!utilisateurConnecte) {
                Menu.afficherMenuNonConnecte();

                int choix = scanner.nextInt();
                scanner.nextLine(); // Consommer la nouvelle ligne restante après le choix numérique

                switch (choix) {
                    case 1:
                        utilisateurConnecte = UtilisateurService.seConnecter(connection, scanner);
                        break;
                    case 2:
                        UtilisateurService.creerCompte(connection, scanner);
                        break;
                    case 3:
                        System.out.println("Au revoir !");
                        System.exit(0);
                    default:
                        System.out.println("Choix invalide. Veuillez sélectionner une option valide.");
                }
            }

            // Maintenant que l'utilisateur est connecté, afficher le menu correspondant
            while (utilisateurConnecte) {
                Menu.afficherMenuConnecte();

                int choix = scanner.nextInt();
                scanner.nextLine(); // Consommer la nouvelle ligne restante après le choix numérique

                switch (choix) {
                    case 1:
                        UtilisateurService.voirUtilisateurs(connection);
                        break;
                    case 2:
                        UtilisateurService.modifierEmail(connection, scanner);
                        break;
                    case 3:
                        System.out.println("Au revoir !");
                        System.exit(0);
                    default:
                        System.out.println("Choix invalide. Veuillez sélectionner une option valide.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
