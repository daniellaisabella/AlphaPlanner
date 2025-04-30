package org.example.alphaplanner.service;

import org.example.alphaplanner.models.*;
import org.example.alphaplanner.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.example.alphaplanner.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class BaseService {

    private UserRepository userRepository;
    private final TaskRepository taskRepository;

    public BaseService(UserRepository userRepository, TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    //------------ USER METHODS-----------//

    public boolean login(User user) {
        return userRepository.login(user);
    }

    public int getUserId(User user) {
        return userRepository.getUserId(user);
    }

    public String getUserRole(Object userId) {
        return userRepository.getUserRole(userId);
    }

    public List<User> getUsersByRole(String role) {
        return userRepository.getUsersByRole(role);
    }


//----------------------Tasks and labels----------------------------------------------

//================DELETE LATER JUST FOR TEST=====================================
    public SubProject getSubdummy(int sub_id) {
        return taskRepository.getSubdummy(sub_id);
    }
//================================================================================
//------------------------------Tasks----------------------------------------------------------

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

//-------------------------------------Labels------------------------------------------------------------

    public List<Label> getAllLabels(){
        return taskRepository.getAllLabels();
    }

    public Label getLabelById(int label_id){
        return taskRepository.getLabelById(label_id);
    }

    public List<Label>  getLabelsFromTask(int task_id){
        return taskRepository.getLabelsFromTask(task_id);
    }

    public void addLabelToTask(int task_id, int label_id){
        taskRepository.addLabelToTask(task_id, label_id);
    }

    public void removeLabelFromTask(int task_id, int label_id){
        taskRepository.removeLabelFromTask(task_id, label_id);
    }

    public void createLabel(String name){
        taskRepository.createLabel(name);
    }

    public void deleteLabel(int label_id){
        taskRepository.deleteLabel(label_id);
    }

//-------------------------------------Assignees-----------------------------------------------

    public List<UserDto> getAssigneesFromTask(int task_id){
        return getAssigneesFromTask(task_id);
    }

    public List<String> getAssigneesInString(List<UserDto> users) {
        return taskRepository.getAssigneesInString(users);
    }

    public void addAssigneesToTask(int task_id, int user_id){
        taskRepository.addAssigneesToTask(task_id, user_id);
    }

    public void removeAssigneesFromTask(int task_id, int user_id){
        taskRepository.removeAssigneesFromTask(task_id, user_id);
    }


}
