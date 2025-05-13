package org.example.alphaplanner.service;

import org.example.alphaplanner.models.SubProject;
import org.example.alphaplanner.repository.SubProjectRepository;
import org.example.alphaplanner.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SubProjectService {

    private final TaskRepository taskRepository;
    private final ProjectService projectService;
    SubProjectRepository subProjectRepository;

    public SubProjectService(SubProjectRepository subProjectRepository, TaskRepository taskRepository, ProjectService projectService)
    {
        this.subProjectRepository = subProjectRepository;
        this.taskRepository = taskRepository;
        this.projectService = projectService;
    }

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

    public void updateHours(int id)
    {

        SubProject subProject = getSubProject(id);
        subProject.setSubEstimatedHours(taskRepository.getSumEstimatedHours(id));
        subProject.setSubDedicatedHours(taskRepository.getSumDedicatedHours(id));
        updateSubProject(subProject);
        projectService.updateHours(subProject.getProjectId());
    }
}
