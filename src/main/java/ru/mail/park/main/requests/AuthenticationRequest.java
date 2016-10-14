package ru.mail.park.main.requests;

@SuppressWarnings("unused")
public final class AuthenticationRequest {
    private String login;
    private String password;

    public AuthenticationRequest() {
    }

    public AuthenticationRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}