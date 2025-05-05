package org.example.alphaplanner.repository;

import org.example.alphaplanner.models.Project;
import org.example.alphaplanner.models.ProjectRowMapper;
import org.example.alphaplanner.models.SubProject;
import org.springframework.jdbc.core.JdbcTemplate;

public class SubProjectRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ProjectRowMapper rowMapper = new ProjectRowMapper();

    public SubProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void AddProjectSql(SubProject project) {
        String query = """
                INSERT INTO subprojects(sub_name, sub_desc, sub_deadline, sub_status, sub_dedicatedHours, sub_timeEstimate, project_id)
                values(?,?,?,?,?,?,?)
                """;
        jdbcTemplate.update(query, project.getSubProjectName(), project.getSubProjectDesc(), project.getSubProjectDeadline(), project.getSubProjectStatus(), project.getSubDedicatedHours(), project.getSubEstimatedHours(), project.getprojectID());
    }

    public void DeleteProjectSQL(int id) {
        String query = """
                sql = "DELETE FROM subprojects WHERE sub_id = ?";
                """;
        jdbcTemplate.update(query, id);
    }

    public void UpdateSQL(SubProject project) {
        String query = """
                        UPDATE projects
                        SET sub_name = ?, sub_desc = ?, sub_deadline = ?, sub_status = ?, sub_dedicatedHours = ?, sub_timeEstimate = ?, project_id = ?
                        WHERE sub_id = ?
                """;
        jdbcTemplate.update(query, project.getSubProjectName(), project.getSubProjectDesc(), project.getSubProjectDeadline(), project.getSubProjectStatus(), project.getSubDedicatedHours(), project.getSubEstimatedHours(), project.getprojectID(), project.getSubId());
    }

    public void getProject(int id)
    {
        String query = """
                SELECT * FROM subprojects WHERE sub_id = ?
                """;
        jdbcTemplate.queryForObject(query, rowMapper, id);
    }
}
