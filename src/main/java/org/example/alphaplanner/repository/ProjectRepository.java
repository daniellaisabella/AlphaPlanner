package org.example.alphaplanner.repository;

import org.example.alphaplanner.models.Project;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectRepository {

    private JdbcTemplate jdbcTemplate;
    public ProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public Project AddProjectToSql(Project project)
    {
        String query = """
                INSERT INTO projects(pm_id, project_name, project_desc, project_deadline, project_status, project_dedicatedHours, project_timeEstimate)
                values(?,?)
                """;
        jdbcTemplate.update(query, project.getProjectName(), project.getProjectDesc());
        return project;
    }
}
