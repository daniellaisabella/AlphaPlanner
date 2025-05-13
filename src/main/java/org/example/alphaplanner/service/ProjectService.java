package org.example.alphaplanner.service;

import org.example.alphaplanner.models.Project;
import org.example.alphaplanner.models.User;
import org.example.alphaplanner.repository.ProjectRepository;
import org.example.alphaplanner.repository.SubProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProjectService {

    private final UserService userService;
    private final SubProjectRepository subProjectRepository;
    ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository, UserService userService, SubProjectRepository subProjectRepository)
    {
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.subProjectRepository = subProjectRepository;
    }

    public Project getProject(int id) {

        return projectRepository.getProject(id);
    }

    public List<Project> getProjects(int userid) {
        User user = userService.getUserById(userid);
        if (Objects.equals(user.getRole(), "project manager"))
        {
            return projectRepository.getProjectsAttachedToManager(userid);

        }else return projectRepository.getProjectsAttachedToEmployee(userid);
    }



    public void updateHours(int id)
    {
        Project project = projectRepository.getProject(id);
        project.setEstimatedHours(subProjectRepository.getSumEstimatedHours(id));
        project.setDedicatedHours(subProjectRepository.getSumDedicatedHours(id));
        updateProject(project);
    }

    public void updateProject(Project freshProject) {
        projectRepository.UpdateSQL(freshProject);
    }

    public void newProject(Project freshProject) {
        projectRepository.addProjectSql(freshProject);
    }

    public void deleteProject(int project) {

        projectRepository.DeleteProjectSQL(project);
    }




}
