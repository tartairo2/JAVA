import java.sql.*;

public class Authenticator {

    public static boolean createUser(String email, String password) {
        if (!isEmailWhitelisted(email)) {
            System.out.println("Erreur: L'email n'est pas sur liste blanche.");
            return false;
        }

        if (isEmailUsed(email)) {
            System.out.println("Erreur: L'email est déjà utilisé.");
            return false;
        }

        try (Connection connection = DatabaseUtils.getConnection()) {
            String query = "INSERT INTO Users (email, password, role) VALUES (?, ?, 'normal')";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, hashPassword(password));
                preparedStatement.executeUpdate();
            }
            System.out.println("Compte créé avec succès.");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean authenticate(String email, String password) {
        try (Connection connection = DatabaseUtils.getConnection()) {
            String query = "SELECT * FROM Users WHERE email = ? AND password = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, hashPassword(password));
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    System.out.println("Connexion réussie. Role: " + resultSet.getString("role"));
                    return true;
                } else {
                    System.out.println("Erreur: Identifiants incorrects.");
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static boolean isEmailUsed(String email) {
        try (Connection connection = DatabaseUtils.getConnection()) {
            String query = "SELECT * FROM Users WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    private static boolean isEmailWhitelisted(String email) {
        try (Connection connection = DatabaseUtils.getConnection()) {
            String query = "SELECT * FROM WhitelistedEmails WHERE email = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                preparedStatement.setString(1, email);
                ResultSet resultSet = preparedStatement.executeQuery();
                return resultSet.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String hashPassword(String password) {
        // Implement password hashing logic (e.g., using BCrypt)
        return password;
    }
}
