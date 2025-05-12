package org.example.alphaplanner.service;

import org.example.alphaplanner.models.Project;
import org.example.alphaplanner.models.User;
import org.example.alphaplanner.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProjectService {

    private final UserService userService;
    ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository, UserService userService)
    {
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    public List<Project> getProjects(int userid) {
        User user = userService.getUserById(userid);
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

        projectRepository.DeleteProjectSQL(project);
    }


    public Project getProject(int id) {
        return projectRepository.getProject(id);
    }

}
