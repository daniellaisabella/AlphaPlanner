package org.example.alphaplanner.repository;

import org.example.alphaplanner.models.Project;
import org.example.alphaplanner.models.ProjectRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProjectRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ProjectRowMapper rowMapper = new ProjectRowMapper();

    public ProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void AddProjectSql(Project project) {
        String query = """
                INSERT INTO projects(project_name, project_desc, project_deadline, project_status, project_dedicatedHours, project_timeEstimate, pm_id)
                values(?,?,?,?,?,?,?)
                """;
        jdbcTemplate.update(query, project.getProjectName(), project.getProjectDesc(), project.getProjectDeadline(), project.getProjectStatus(), project.getDedicatedHours(), project.getEstimatedHours(), project.getPm_id());
    }

    public void DeleteProjectSQL(int id) {
        String query = """
                sql = "DELETE FROM PROJECTS WHERE project_id = ?";
                """;
        jdbcTemplate.update(query, id);
    }

    public void UpdateSQL(Project project) {
        String query = """
                        UPDATE projects
                        SET project_name = ?, project_desc = ?, project_deadline = ?, project_status = ?, project_dedicatedHours = ?, project_timeEstimate = ?, pm_id = ?
                        WHERE project_id = ?
                """;
        jdbcTemplate.update(query, project.getProjectName(), project.getProjectDesc(), project.getProjectDeadline(), project.getProjectStatus(), project.getDedicatedHours(), project.getEstimatedHours(), project.getPm_id(), project.getId());
    }

    public void getProject(int id)
    {
        String query = """
                SELECT * FROM projects WHERE project_id = ?
                """;
        jdbcTemplate.queryForObject(query, rowMapper, id);
    }
}
