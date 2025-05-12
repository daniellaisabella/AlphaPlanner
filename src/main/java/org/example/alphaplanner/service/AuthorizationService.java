package org.example.alphaplanner.service;

import org.example.alphaplanner.repository.ProjectRepository;

public class AuthorizationService {


    private final ProjectRepository projectRepository;

    public AuthorizationService(ProjectRepository projectRepository)
    {
        this.projectRepository = projectRepository;
    }

    public boolean authProjectManager(int userId, int project)
    {
        projectRepository.getProject(project);
        return ( projectRepository.getProject(project).getPm_id() == userId);
    }
}
