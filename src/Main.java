import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bienvenue dans l'application!");

        while (true) {
            System.out.println("1. Créer un compte");
            System.out.println("2. Se connecter");
            System.out.println("3. Quitter");
            System.out.print("Choisissez une option : ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    createUserMenu();
                    break;

                case 2:
                    authenticateUserMenu();
                    break;

                case 3:
                    System.out.println("Merci d'avoir utilisé l'application. Au revoir!");
                    System.exit(0);

                default:
                    System.out.println("Option invalide. Veuillez choisir une option valide.");
            }
        }
    }

    private static void createUserMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Entrez votre email : ");
            String email = scanner.nextLine();
            System.out.print("Entrez votre mot de passe : ");
            String password = scanner.nextLine();

            if (Authenticator.createUser(email, password)) {
                break; // Sortir de la boucle si le compte est créé avec succès
            } else {
                System.out.println("Veuillez réessayer.");
            }
        }
    }

    private static void authenticateUserMenu() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Entrez votre email : ");
            String loginEmail = scanner.nextLine();
            System.out.print("Entrez votre mot de passe : ");
            String loginPassword = scanner.nextLine();

            if (Authenticator.authenticate(loginEmail, loginPassword)) {
                break; // Sortir de la boucle si la connexion réussit
            } else {
                System.out.println("Veuillez réessayer.");
            }
        }
    }
}
