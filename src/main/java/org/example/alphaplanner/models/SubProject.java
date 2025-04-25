package org.example.alphaplanner.models;

import java.time.LocalDate;

public class SubProject {

    private String subProjectName;
    private String subProjectDesc;
    private LocalDate subProjectDeadline;
    private Boolean subProjectStatus;
    private double subDedicatedHours;
    private double subEstimatedHours;


    public SubProject(String subProjectName, String subProjectDesc, LocalDate subProjectDeadline) {
        this.subProjectName = subProjectName;
        this.subProjectDesc = subProjectDesc;
        this.subProjectDeadline = subProjectDeadline;
        this.subProjectStatus = false;
        this.subDedicatedHours = 0;
        this.subEstimatedHours = 0;

    }

    public String getSubProjectName() {
        return subProjectName;
    }

    public void setSubProjectName(String subProjectName) {
        this.subProjectName = subProjectName;
    }

    public String getSubProjectDesc() {
        return subProjectDesc;
    }

    public void setSubProjectDesc(String subProjectDesc) {
        this.subProjectDesc = subProjectDesc;
    }

    public LocalDate getSubProjectDeadline() {
        return subProjectDeadline;
    }

    public void setSubProjectDeadline(LocalDate subProjectDeadline) {
        this.subProjectDeadline = subProjectDeadline;
    }

    public Boolean getSubProjectStatus() {
        return subProjectStatus;
    }

    public void setSubProjectStatus(Boolean subProjectStatus) {
        this.subProjectStatus = subProjectStatus;
    }

    public double getSubDedicatedHours() {
        return subDedicatedHours;
    }

    public void setSubDedicatedHours(double subDedicatedHours) {
        this.subDedicatedHours = subDedicatedHours;
    }

    public double getSubEstimatedHours() {
        return subEstimatedHours;
    }

    public void setSubEstimatedHours(double subEstimatedHours) {
        this.subEstimatedHours = subEstimatedHours;
    }

}
