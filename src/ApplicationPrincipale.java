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
<<<<<<< Updated upstream
        Connection connection = null;
=======
        GestionUtilisateurs gestionUtilisateurs = new GestionUtilisateurs(); // Ajoutez cette ligne
>>>>>>> Stashed changes

        try {
            connection = ConnexionMySQL.obtenirConnexion();

            while (!utilisateurConnecte) {
                Menu.afficherMenuNonConnecte();

<<<<<<< Updated upstream
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
=======
            try (Connection connection = ConnexionMySQL.obtenirConnexion()) {
                switch (choix) {
                    case 1:
                        utilisateurConnecte = gestionUtilisateurs.seConnecter(scanner, connection); // Modifiez cette ligne
                        break;
                    case 2:
                        gestionUtilisateurs.creerCompte(scanner, connection);
                        break;
                    case 3:
                        System.out.println("Au revoir !");
                        System.exit(0);
                    default:
                        System.out.println("Choix invalide. Veuillez sélectionner une option valide.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void afficherMenu() {
        System.out.println("1. Se connecter");
        System.out.println("2. Créer un compte");
        System.out.println("3. Quitter");
        System.out.println("Choisissez une option : ");
>>>>>>> Stashed changes
    }
}
