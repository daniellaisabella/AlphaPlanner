package org.example.alphaplanner.models;

import java.time.LocalDate;

public class SubProject {


    private int subId;
    private String subProjectName;
    private String subProjectDesc;
    private LocalDate subProjectDeadline;
    private Boolean subProjectStatus;
    private double subDedicatedHours;
    private double subEstimatedHours;
    private int projectId;


    public SubProject() {
        subId= 0;
        subProjectName = "";
        subProjectDesc = "";
        subProjectDeadline = LocalDate.now();
        subProjectStatus = false;
        subEstimatedHours = 0;
        subDedicatedHours = 0;
        projectId = 0;
    }

    public SubProject(int subId, String subProjectName, String subProjectDesc, LocalDate subProjectDeadline, int projectId) {
        this.subId = subId;
        this.subProjectName = subProjectName;
        this.subProjectDesc = subProjectDesc;
        this.subProjectDeadline = subProjectDeadline;
        this.subProjectStatus = false;
        this.subDedicatedHours = 0;
        this.subEstimatedHours = 0;
        this.projectId = projectId;

    }
//for sql retrieval
    public SubProject(int subId, String subProjectName, String subProjectDesc, LocalDate subProjectDeadline, boolean subProjectStatus, double subEstimatedHours, double subDedicatedHours, int projectId) {
        this.subId = subId;
        this.subProjectName = subProjectName;
        this.subProjectDesc = subProjectDesc;
        this.subProjectDeadline = subProjectDeadline;
        this.subProjectStatus = subProjectStatus;
        this.subDedicatedHours = subDedicatedHours;
        this.subEstimatedHours = subEstimatedHours;
        this.projectId = projectId;

    }
    public int getSubId() {
        return subId;
    }

    public void setSubId(int subId) {
        this.subId = subId;
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

    public Object getprojectid() {
        return projectId;
    }
    public void setProjectId(int id) {
        this.projectId = id;
    }

    public int getProjectId() {
        return projectId;
    }
}
