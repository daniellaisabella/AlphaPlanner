package org.example.alphaplanner.models;

import java.time.LocalDate;

public class Task {

    private int taskId;
    private String taskName;
    private String taskDesc;
    private LocalDate taskDeadline;
    private Boolean taskStatus;
    private double taskDedicatedHours;
    private double taskEstimatedHours;
    private String labels;
    private String assignees; //might change to a list of users later
    private final int subId;


//-----------------------------CONSTRUCTORS--------------------------------------------------------------------

    // to retrieve from sql
    public Task(int taskId, String taskName, String taskDesc, LocalDate taskDeadline, double taskEstimatedHours, int subId){
        this.taskId=taskId;
        this.taskName=taskName;
        this.taskDesc = taskDesc;
        this.taskDeadline = taskDeadline;
        this.subId = subId;
        this.taskStatus = false;
        this.taskDedicatedHours = 0;
        this.taskEstimatedHours = taskEstimatedHours;
        this.labels = "";
    }

    // To create new task

    public Task(String taskName, String taskDesc, LocalDate taskDeadline, double taskEstimatedHours, int subId){
        this.taskName=taskName;
        this.taskDesc = taskDesc;
        this.taskDeadline = taskDeadline;
        this.subId = subId;
        this.taskStatus = false;
        this.taskDedicatedHours = 0;
        this.taskEstimatedHours = taskEstimatedHours;
        this.labels = "";
    }
//-------------------------------------GETTER METHODS---------------------------------------------------------

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getTaskDesc() {
        return taskDesc;
    }

    public void setTaskDesc(String taskDesc) {
        this.taskDesc = taskDesc;
    }

    public LocalDate getTaskDeadline() {
        return taskDeadline;
    }

    public void setTaskDeadline(LocalDate taskDeadline) {
        this.taskDeadline = taskDeadline;
    }

    public Boolean getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Boolean taskStatus) {
        this.taskStatus = taskStatus;
    }

    public double getTaskDedicatedHours() {
        return taskDedicatedHours;
    }

    public void setTaskDedicatedHours(double taskDedicatedHours) {
        this.taskDedicatedHours = taskDedicatedHours;
    }

    public double getTaskEstimatedHours() {
        return taskEstimatedHours;
    }

    public void setTaskEstimatedHours(double taskEstimatedHours) {
        this.taskEstimatedHours = taskEstimatedHours;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public String getAssignees() {
        return assignees;
    }

    public void setAssignees(String assignees) {
        this.assignees = assignees;
    }

    public int getSubprojectId() {
        return subId;
    }
}
