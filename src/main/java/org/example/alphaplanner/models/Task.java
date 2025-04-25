package org.example.alphaplanner.models;

import java.time.LocalDate;

public class Task {

    private String taskName;
    private String taskDesc;
    private LocalDate taskDeadline;
    private Boolean taskStatus;
    private double taskDedicatedHours;
    private double taskEstimatedHours;

    public Task(String taskName, String taskDesc, LocalDate taskDeadline){
        this.taskName=taskName;
        this.taskDesc = taskDesc;
        this.taskDeadline = taskDeadline;
        this.taskStatus = false;
        this.taskDedicatedHours = 0;
        this.taskEstimatedHours = 0;
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
}
