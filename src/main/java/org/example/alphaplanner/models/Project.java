package org.example.alphaplanner.models;

import java.time.LocalDate;

public class Project {
    private int id;
    private String projectName;
    private String projectDesc;
    private LocalDate projectDeadline;
    private Boolean projectStatus;
    private double dedicatedHours;
    private double estimatedHours;


    public Project(String projectName, String projectDesc, LocalDate projectDeadline, double estimatedHours) {
        this.projectName = projectName;
        this.projectDesc = projectDesc;
        this.projectDeadline = projectDeadline;
        this.projectStatus = false;
        this.estimatedHours = estimatedHours;
        this.dedicatedHours = 0;
    }
    //for sql
    public Project(int id, String projectName, String projectDesc, LocalDate projectDeadline, boolean projectStatus, double estimatedHours, double dedicatedHours) {
        this.id = id;
        this.projectName = projectName;
        this.projectDesc = projectDesc;
        this.projectDeadline = projectDeadline;
        this.projectStatus = projectStatus;
        this.estimatedHours = estimatedHours;
        this.dedicatedHours = dedicatedHours;

    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectDesc() {
        return projectDesc;
    }

    public void setProjectDesc(String projectDesc) {
        this.projectDesc = projectDesc;
    }

    public LocalDate getProjectDeadline() {
        return projectDeadline;
    }

    public void setProjectDeadline(LocalDate projectDeadline) {
        this.projectDeadline = projectDeadline;
    }

    public Boolean getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(Boolean projectStatus) {
        this.projectStatus = projectStatus;
    }

    public double getDedicatedHours() {
        return dedicatedHours;
    }

    public void setDedicatedHours(double dedicatedHours) {
        this.dedicatedHours = dedicatedHours;
    }

    public double getEstimatedHours() {
        return estimatedHours;
    }

    public void setEstimatedHours(double estimatedHours) {
        this.estimatedHours = estimatedHours;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
