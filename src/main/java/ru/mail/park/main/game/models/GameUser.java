package ru.mail.park.main.game.models;

/**
 * Created by farid on 12.12.16.
 */
public class GameUser {

    private Integer userId;
    private String userName;

    public GameUser() {

    }

    public GameUser(Integer userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
