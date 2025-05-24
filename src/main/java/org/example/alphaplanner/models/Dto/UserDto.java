package org.example.alphaplanner.models.Dto;

public class UserDto {
    private int id;
    private String name;
    private String role;
    private String skills;



    public UserDto(int id, String name, String role, String skills) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.skills = skills;
    }

    public UserDto(int id, String name, String role){
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSkills() {
        return skills;
    }

    public void setSkills(String skills) {
        this.skills = skills;
    }
}
