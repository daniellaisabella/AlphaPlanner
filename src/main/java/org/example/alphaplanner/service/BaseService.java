package org.example.alphaplanner.service;

import org.example.alphaplanner.models.*;
import org.example.alphaplanner.repository.ProjectRepository;
import org.example.alphaplanner.repository.SubProjectRepository;
import org.example.alphaplanner.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.example.alphaplanner.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class BaseService {

    private final ProjectRepository projectRepository;
    private UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final SubProjectRepository subProjectRepository;

    public BaseService(UserRepository userRepository, TaskRepository taskRepository, ProjectRepository projectRepository, SubProjectRepository subProjectRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.subProjectRepository = subProjectRepository;
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

    public String getLabelsInString(List<Label> labels){
        return taskRepository.getLabelsInString(labels);
    }

    public Label getLabelById(int label_id){
        return taskRepository.getLabelById(label_id);
    }

    public List<Label>  getLabelsFromTask(int task_id){
        return taskRepository.getLabelsFromTask(task_id);
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }


//------------------------------------------------------------------------------------
    public void addLabelsToTask(int task_id, List<Integer> labels){
        taskRepository.addLabelsToTask(task_id, labels);
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

    public boolean checkIfLabelNameExist(String name){
        return taskRepository.checkIfLabelNameExist(name);
    }

//-------------------------------------Assignees-----------------------------------------------

    public List<UserDto> getAssigneesFromTask(int task_id){
        return getAssigneesFromTask(task_id);
    }

    public String getAssigneesInString(List<UserDto> users) {
        return taskRepository.getAssigneesInString(users);
    }

    public void addAssigneesToTask(int task_id, int user_id){
        taskRepository.addAssigneesToTask(task_id, user_id);
    }

    public void removeAssigneesFromTask(int task_id, int user_id){
        taskRepository.removeAssigneesFromTask(task_id, user_id);
    }



//----------------------------------Projects--------------------------------------------------


    public List<Project> getProjects(int userid) {
        User user = getUserById(userid);
        if (Objects.equals(user.getRole(), "project manager"))
        {
            return projectRepository.getProjectsAttachedToManager(userid);
        }else return projectRepository.getProjectsAttachedToEmployee(userid);
    }

    public void updateProject(Project freshProject) {
        projectRepository.UpdateSQL(freshProject);
    }

    public void newProject(Project freshProject) {
        projectRepository.addProjectSql(freshProject);
    }

    public void deleteProject(int userId, int project) {

        if(authProjectManager(userId, project))projectRepository.DeleteProjectSQL(project);
    }

    public boolean authProjectManager(int userId, int project)
    {
        projectRepository.getProject(project);
        return ( projectRepository.getProject(project).getPm_id() == userId);
    }


    public Project getProject(int id) {
        return projectRepository.getProject(id);
    }

//----------------------------------Projects--------------------------------------------------

    public void updateSubProject(SubProject freshSubProject) {
        subProjectRepository.UpdateSQL(freshSubProject);
    }

    public void newSubProject(SubProject freshSubProject) {
        subProjectRepository.addSubProjectToSql(freshSubProject);
    }

    public void deleteSubProject(int id) {
        subProjectRepository.DeleteProjectSQL(id);
    }

    public List<SubProject> getSubProjects(int projectId) {
            return subProjectRepository.getSubProjectAttachedToProject(projectId);
    }
    public SubProject getSubProject(int id) {
        return subProjectRepository.getSubProject(id);
    }
}
