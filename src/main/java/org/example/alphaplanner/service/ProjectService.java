package org.example.alphaplanner.service;

import org.example.alphaplanner.models.Dto.UserToProjectDto;
import org.example.alphaplanner.models.Project;
import org.example.alphaplanner.models.SubProject;
import org.example.alphaplanner.models.Task;
import org.example.alphaplanner.models.User;
import org.example.alphaplanner.repository.ProjectRepository;
import org.example.alphaplanner.repository.SubProjectRepository;
import org.example.alphaplanner.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class ProjectService {

    private final UserService userService;
    private final SubProjectRepository subProjectRepository;
    private final UserRepository userRepository;
    ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository, UserService userService, SubProjectRepository subProjectRepository, UserRepository userRepository)
    {
        this.projectRepository = projectRepository;
        this.userService = userService;
        this.subProjectRepository = subProjectRepository;
        this.userRepository = userRepository;
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
        List<SubProject> subProjects = subProjectRepository.getSubProjectAttachedToProject(id);
        boolean isFullyDone =true;
        for (SubProject subProject : subProjects)
        {
            if (!subProject.getSubProjectStatus()) {
                isFullyDone = false;
                break;
            }
        }
        project.setProjectStatus(isFullyDone);
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


    public List<User> getUsersByProjectId(int id) {
        return userRepository.getUsersByProjectId(id);
    }

    public void assignUserToProject(UserToProjectDto newJunction) {
        if (Objects.equals(userRepository.getUserRole(newJunction.getUserId()), "employee")) {
            projectRepository.assignUser(newJunction);
        }
    }

    public void unassignUserFromProject(UserToProjectDto oldJunction)
    {
        projectRepository.UnassignUser(oldJunction);
    }
}
