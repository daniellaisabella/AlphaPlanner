package org.example.alphaplanner.service;

import org.example.alphaplanner.models.Dto.UserDto;
import org.example.alphaplanner.repository.ProjectRepository;
import org.example.alphaplanner.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AssigneesService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    private AssigneesService(TaskRepository taskRepository, ProjectRepository projectRepository)
    {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }


    public void addAssigneesToTask(int task_id, List<Integer> assignees){
        taskRepository.addAssigneesToTask(task_id, assignees);
    }


    public List<UserDto> getEmployeesFromProject (int projectId){
        return projectRepository.getEmployeesFromProject(projectId);
    }

    public String getEmployeesFromProjectInString(List<UserDto> users){
        return projectRepository.getEmployeesFromProjectInString(users);
    }



}
