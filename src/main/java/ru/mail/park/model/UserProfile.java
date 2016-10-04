package ru.mail.park.model;

@SuppressWarnings("unused")
public class UserProfile {
    private String login;
    private String password;
    private String email;
    private long id;

    public UserProfile(long id, String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public long getId() {
        return id;
    }

    public String getUserInfoJSON() {
        return "{\"login\": " + '\"' + login + "\"," + "\"email\": " + '\"' + email + "\"}";
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
