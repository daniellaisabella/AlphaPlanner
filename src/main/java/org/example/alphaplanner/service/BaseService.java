package org.example.alphaplanner.service;

import org.example.alphaplanner.models.*;
import org.example.alphaplanner.repository.ProjectRepository;
import org.example.alphaplanner.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.example.alphaplanner.repository.TaskRepository;

import java.util.List;

@Service
public class BaseService {

    private final ProjectRepository projectRepository;
    private UserRepository userRepository;
    private final TaskRepository taskRepository;

    public BaseService(UserRepository userRepository, TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    //------------ USER METHODS-----------//

    public boolean login(User user) {
        return userRepository.login(user);
    }

    public User getUserById(int userId){
        return userRepository.getUserById(userId);
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


    public boolean checkForDup(String email) {
        return userRepository.checkForDup(email);
    }

    public void saveUser(User user) {
        userRepository.saveUser(user);
    }

    public List<String> getRoles() {
        return userRepository.getRoles();
    }

    public List<String> getSkills() {
        return userRepository.getSkills();
    }

    public void deleteUser(int userId) {
        userRepository.deleteUser(userId);
    }


//----------------------Tasks and labels----------------------------------------------

    //================DELETE LATER JUST FOR TEST=====================================
    public SubProject getSubdummy(int sub_id) {
        return taskRepository.getSubdummy(sub_id);
    }
//================================================================================

    public List<Task> showAllTasksFromSub(int sub_id) {
        return taskRepository.showAllTasksFromSub(sub_id);
    }

    public List<Label> getLabelsFromTask(int task_id) {
        return taskRepository.getLabelsFromTask(task_id);
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    public List<Project> getProjects(int userid) {
        return projectRepository.getAttachedProjects(userid);
    }


//------------------------------------------------------------------------------------
}
