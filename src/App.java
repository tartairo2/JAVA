import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    private List<User> users = new ArrayList<>();
    private boolean isLoggedIn = false;

    public void run() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            displayMainMenu();
            int option = scanner.nextInt();
            scanner.nextLine(); // pour consommer la fin de ligne après la saisie de l'option

            switch (option) {
                case 1:
                    if (!isLoggedIn) {
                        createUser();
                    } else {
                        System.out.println("Erreur : Vous êtes déjà connecté.");
                    }
                    break;
                case 2:
                    if (!isLoggedIn) {
                        login();
                    } else {
                        System.out.println("Erreur : Vous êtes déjà connecté.");
                    }
                    break;
                case 3:
                    if (isLoggedIn) {
                        displayUsersWithoutPasswords();
                    } else {
                        System.out.println("Erreur : Vous devez d'abord vous connecter.");
                    }
                    break;
                case 4:
                    System.out.println("Au revoir !");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Option invalide. Veuillez choisir une option valide.");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("Menu principal :");

        // Affiche la ligne seulement si l'utilisateur n'est pas connecté
        if (!isLoggedIn) {
            System.out.println("1. Créer un compte");
            System.out.println("2. Se connecter");
        }

        // Affiche la ligne seulement si l'utilisateur est connecté
        if (isLoggedIn) {
            System.out.println("3. Afficher la liste des users");
        }

        System.out.println("4. Quitter");
        System.out.print("Choisissez une option : ");
    }

    private void createUser() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Entrez votre email : ");
        String email = scanner.nextLine();

        System.out.print("Entrez votre mot de passe : ");
        String plainPassword = scanner.nextLine();

        // Hachez le mot de passe avant de le stocker
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        users.add(new User(email, hashedPassword));
        System.out.println("Compte créé avec succès !");
    }

    private void login() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Entrez votre email : ");
        String email = scanner.nextLine();

        System.out.print("Entrez votre mot de passe : ");
        String plainPassword = scanner.nextLine();

        // Recherche de l'utilisateur correspondant à l'e-mail
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                // Vérification du mot de passe
                if (BCrypt.checkpw(plainPassword, user.getPassword())) {
                    isLoggedIn = true;
                    System.out.println("Connecté avec succès !");
                    return;
                }
            }
        }

        // Si les informations de connexion sont incorrectes
        System.out.println("Erreur : Les informations de connexion sont incorrectes.");
    }

    private void displayUsersWithoutPasswords() {
        System.out.println("\nListe des utilisateurs sans mots de passe :");
        for (User user : users) {
            System.out.println("Email : " + user.getEmail());
        }

        // Attente de l'entrée pour revenir au menu principal
        System.out.println("\nAppuyez sur Entrée pour revenir au menu principal.");
        new Scanner(System.in).nextLine();
    }

    private static class User {
        private String email;
        private String password;

        public User(String email, String password) {
            this.email = email;
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
}
