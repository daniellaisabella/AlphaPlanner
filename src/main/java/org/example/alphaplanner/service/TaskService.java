package org.example.alphaplanner.service;

import org.example.alphaplanner.models.Task;
import org.example.alphaplanner.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;



@Service
public class TaskService {

    public TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository)
    {
        this.taskRepository = taskRepository;
    }

    public List<Task> showAllTasksFromSub(int sub_id) {
        return taskRepository.showAllTasksFromSub(sub_id);
    }

    public Task getTaskById(int task_id){
        return taskRepository.getTaskById(task_id);
    }

    public void createTask(int sub_id, String name, String desc, LocalDate deadline, double estimatedHours, List<Integer> labels_id){
        taskRepository.createTask(sub_id, name, desc, deadline, estimatedHours, labels_id);
    }

    public void editTask(int task_id, String name, String desc, LocalDate deadline, double estimatedHours, double dedicatedHours, boolean status){
        taskRepository.editTask(task_id, name, desc, deadline, estimatedHours, dedicatedHours, status);
    }

    public void deleteTask(int task_id){
        taskRepository.deleteTask(task_id);
    }

}
