package com.findme.user.dao.request;

public class NewUserDao {
    private String email;
    private String username;
    private String phoneNumber;
    private String password;

    // Constructors
    public NewUserDao(String email, String username, String phoneNumber, String password, String confirmedPassword) {
        if (!password.equals(confirmedPassword)) {
            throw new IllegalArgumentException("The passwords must be same!");
        }
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

}
