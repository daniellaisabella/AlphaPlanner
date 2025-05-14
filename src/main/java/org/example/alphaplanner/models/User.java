package org.example.alphaplanner.models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int userId;
    private String role;
    private String name;
    private String email;
    private String password;
    private List<String> skills;


    public User(String name, String email, String password, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;

        this.skills = new ArrayList<>();
    }

    // CONSTRUCTOR FOR LOG IN
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // EMPTY CONSTRUCTOR
    public User() {
    }

    public User(int id, String userName, String email, String password, String role) {
        this.userId = id;
        name = userName;
        this.email = email;
        this.role = role;
        this.password=password;
        this.skills = new ArrayList<>();
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}
