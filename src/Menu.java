import java.util.Scanner;

public class Menu {

    public static void afficherMenuNonConnecte() {
        System.out.println("1. Se connecter");
        System.out.println("2. Créer un compte");
        System.out.println("3. Quitter");
        System.out.println("Choisissez une option : ");
    }

    public static void afficherMenuConnecte() {
        System.out.println("1. Voir les utilisateurs");
        System.out.println("2. Modifier le mail");
        System.out.println("3. Quitter");
        System.out.println("Choisissez une option : ");
    }
}
