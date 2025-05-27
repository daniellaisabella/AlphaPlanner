package org.example.alphaplanner.repository;

import org.example.alphaplanner.models.Dto.UserDto;
import org.example.alphaplanner.models.Dto.UserToProjectDto;
import org.example.alphaplanner.models.Project;
import org.example.alphaplanner.repository.rowmappers.ProjectRowMapper;
import org.example.alphaplanner.repository.rowmappers.UserDtoRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class ProjectRepository {

    private final JdbcTemplate jdbcTemplate;
    private final ProjectRowMapper rowMapper = new ProjectRowMapper();

    public ProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addProjectSql(Project project) {
        String query = """
                INSERT INTO projects(project_name, project_desc, project_deadline, project_status, project_dedicatedHours, project_timeEstimate, pm_id)
                values(?,?,?,?,?,?,?)
                """;
        jdbcTemplate.update(query, project.getProjectName(), project.getProjectDesc(), project.getProjectDeadline(), project.getProjectStatus(), project.getDedicatedHours(), project.getEstimatedHours(), project.getPm_id());
    }

    public void DeleteProjectSQL(int id) {
        String query = """
                DELETE FROM PROJECTS WHERE project_id = ?;
                """;
        jdbcTemplate.update(query, id);
    }

    public void UpdateSQL(Project project) {
        String query = """
                        UPDATE projects
                        SET project_name = ?, project_desc = ?, project_deadline = ?, project_status = ?, project_dedicatedHours = ?, project_timeEstimate = ?
                        WHERE project_id = ?
                """;
        jdbcTemplate.update(query, project.getProjectName(), project.getProjectDesc(), project.getProjectDeadline(), project.getProjectStatus(), project.getDedicatedHours(), project.getEstimatedHours(), project.getId());
    }

    public Project getProject(int id) {
        String query = """
                SELECT * FROM projects WHERE project_id = ?
                """;
        return jdbcTemplate.queryForObject(query, rowMapper, id);
    }

    public List<Project> getProjectsAttachedToManager(Object userid) {

        String query = """
                SELECT * FROM projects WHERE pm_id = ?
                """;
        return jdbcTemplate.query(query, rowMapper, userid);
    }

    public List<Project> getProjectsAttachedToEmployee(int userid) {
        String query = """
                SELECT p.*
                FROM projects p
                JOIN users_projects up ON p.project_id = up.project_id
                WHERE up.user_id = ?
                """;
        return jdbcTemplate.query(query, rowMapper, userid);
    }

    public int getPm_id(int projectId) {
        String query = """
                SELECT projects.pm_id FROM projects WHERE project_Id = ?
                """;
        Integer i = jdbcTemplate.queryForObject(query, Integer.class, projectId);
        if (i != null) {
            return i;
        }
        return 0;
    }

    public void assignUser(UserToProjectDto newJunction) {
        String query = """
                INSERT INTO users_projects(user_id, project_id)
                values(?,?)
                """;
        jdbcTemplate.update(query, newJunction.getUserId(), newJunction.getProjectId());
    }
    public void UnassignUser(UserToProjectDto newJunction) {
        String query = """
                DELETE FROM users_projects WHERE user_id = ? AND project_id = ? ;
                """;
        jdbcTemplate.update(query, newJunction.getUserId(), newJunction.getProjectId());
    }

    public List<UserDto> getEmployeesFromProject(int projectId){
        List<UserDto> users;
        try {
            String sql = "SELECT u.user_id, u.user_name, u.role FROM Users u JOIN users_projects uP ON u.user_id = uP.user_id WHERE uP.project_id = ?";
            users = jdbcTemplate.query(sql, new UserDtoRowMapper(), projectId);
        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while retrieving assignees from task.", e);
        }
        try {
            for (UserDto u : users) {
                String sql = "SELECT s.skill_name FROM skills s JOIN users_skills uS ON s.skill_id = uS.skill_id WHERE uS.user_id = ?";
                List<String> skillNames = jdbcTemplate.queryForList(sql, String.class, u.getId());
                String skills = String.join(",", skillNames); // Combine into single string
                u.setSkills(skills);

            }
        } catch (DataAccessException e) {
            throw new IllegalStateException("Error retrieving task names.", e);
        }

        return users;


    }

}
