package org.example.alphaplanner.service;

import org.example.alphaplanner.repository.ProjectRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService {


    private final ProjectRepository projectRepository;

    public AuthorizationService(ProjectRepository projectRepository)
    {
        this.projectRepository = projectRepository;
    }

    public boolean authProjectManager(int userId, int project)
    {
        return ( projectRepository.getPm_id(project) == userId);
    }
}
