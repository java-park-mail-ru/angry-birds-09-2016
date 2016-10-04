package ru.mail.park.model;

import com.fasterxml.jackson.databind.ObjectMapper;

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
        StringBuilder jsonString = new StringBuilder();

        jsonString.append("{\"login\": " + "\"" + login + "\",");
        jsonString.append("\"email\": " + "\"" + email + "\"}");

        return jsonString.toString();
    }
}
