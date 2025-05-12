package org.example.alphaplanner.service;

import org.example.alphaplanner.models.SubProject;
import org.example.alphaplanner.repository.SubProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SubProjectService {

    SubProjectRepository subProjectRepository;

    public SubProjectService(SubProjectRepository subProjectRepository)
    {
        this.subProjectRepository = subProjectRepository;
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
}
