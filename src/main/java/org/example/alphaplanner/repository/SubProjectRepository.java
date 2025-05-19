package org.example.alphaplanner.repository;

import org.example.alphaplanner.models.SubProject;
import org.example.alphaplanner.repository.rowmappers.SubProjectRowMApper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SubProjectRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SubProjectRowMApper rowMapper = new SubProjectRowMApper();

    public SubProjectRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void addSubProjectToSql(SubProject project) {
        String query = """
                INSERT INTO subprojects(sub_name, sub_desc, sub_deadline, sub_status, sub_dedicatedHours, sub_timeEstimate, project_id)
                values(?,?,?,?,?,?,?)
                """;
        jdbcTemplate.update(query, project.getSubProjectName(), project.getSubProjectDesc(), project.getSubProjectDeadline(), project.getSubProjectStatus(), project.getSubDedicatedHours(), project.getSubEstimatedHours(), project.getprojectid());
    }

    public void DeleteProjectSQL(int id) {
        String query = """
                DELETE FROM subprojects WHERE sub_id = ?;
                """;
        jdbcTemplate.update(query, id);
    }

    public void UpdateSQL(SubProject project) {
        String query = """
                        UPDATE subprojects
                        SET sub_name = ?, sub_desc = ?, sub_deadline = ?, sub_status = ?, sub_dedicatedHours = ?, sub_timeEstimate = ?, project_id = ?
                        WHERE sub_id = ?
                """;
        jdbcTemplate.update(query, project.getSubProjectName(), project.getSubProjectDesc(), project.getSubProjectDeadline(), project.getSubProjectStatus(), project.getSubDedicatedHours(), project.getSubEstimatedHours(), project.getprojectid(), project.getSubId());
    }

    public SubProject getSubProject(int id) {
        String query = """
                SELECT * FROM subprojects WHERE sub_id = ?
                """;
        return jdbcTemplate.queryForObject(query, rowMapper, id);
    }

    public List<SubProject> getSubProjectAttachedToProject(int id) {
        String query = """
                SELECT * FROM subprojects WHERE project_id = ?
                """;
        return jdbcTemplate.query(query, rowMapper, id);
    }

    public int getSumDedicatedHours(int projectID)
    {
        String query = """
                SELECT SUM(sub_dedicatedHours) FROM subprojects WHERE project_id = ?;
                """;
        Integer i = jdbcTemplate.queryForObject(query, Integer.class, projectID);
        if(i == null){
            return 0;
        }return i;
    }

    public int getSumEstimatedHours(int projectID)
    {
        String query = """
                SELECT SUM(sub_timeEstimate) FROM subprojects WHERE project_id = ?;
                """;
        Integer i = jdbcTemplate.queryForObject(query, Integer.class, projectID);
        if(i == null){
            return 0;
        }return i;
    }
}
