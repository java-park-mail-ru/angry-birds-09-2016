package ru.mail.park.main.requests;

@SuppressWarnings("unused")
public final class RegistrationRequest {
    private String login;
    private String password;
    private String email;

    public RegistrationRequest() {

    }

    public RegistrationRequest(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
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
}