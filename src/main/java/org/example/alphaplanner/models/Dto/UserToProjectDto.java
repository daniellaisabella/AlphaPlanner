package org.example.alphaplanner.models.Dto;

public class UserToProjectDto {

    private int userId;
    private int projectId;

    public UserToProjectDto()
    {

    }

    public UserToProjectDto(int userId, int projectId)
    {
        this.userId = userId;
                this.projectId = projectId;
    }

    public int getUserId() {
        return userId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
