public class User {
    private String email;
    private String pseudo;
    private String password;

    public User(String email, String pseudo, String password) {
        this.email = email;
        this.pseudo = pseudo;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPseudo() {
        return pseudo;
    }
}
