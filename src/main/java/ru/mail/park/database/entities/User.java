package ru.mail.park.database.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;

/**
 * Created by farid on 14.11.16.
 */

@Entity
public class User {
    @Id
    @GeneratedValue
    @JsonProperty("id")
    private Integer userId;

    private String login;

    private String email;

    @JsonIgnore
    private String password;

    public User() {}

    public User(String login, String email, String password) {
        this.login = login;
        this.email = email;
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
        this.userId = 0;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public boolean matchPassword(String plainPassword) {
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(plainPassword, password);
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
    }

    public String toJSON() {
        String json;
        try {
            final ObjectMapper mapper = new ObjectMapper();
            json = mapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            json = "{}";
        }

        return json;
    }
}
