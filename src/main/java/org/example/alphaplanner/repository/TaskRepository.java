package org.example.alphaplanner.repository;

import org.example.alphaplanner.models.SubProject;
import org.example.alphaplanner.models.Task;
import org.example.alphaplanner.models.TaskRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//
@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    //DUMMY REMOVE LATER
    private final SubRowMapper subRowMapper = new SubRowMapper();

//---------------------------------CONSTRUCTOR--------------------------------------------------------------------------

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//------------------------------ROW MAPPERS-----------------------------------------------------------------------------


//--------------------------------TASK METHODS--------------------------------------------------------------------------

//dummy method to get a subproject

    public class SubRowMapper implements RowMapper<SubProject> {

        @Override
        public SubProject mapRow(ResultSet rs, int rowNum) throws SQLException {
            String name = rs.getString("sub_name");
            String desc = rs.getString("sub_desc");
            LocalDate deadline = rs.getDate("sub_deadLine").toLocalDate();

            return new SubProject(name, desc, deadline);
        }
    }

    public SubProject getSubdummy(int sub_id) {
        String sql = "SELECT sP.sub_name, sP,sub_desc, sB.sub_deadLine " +
                "FROM SubProjects sP" +
                "WHERE sP.sub_id = ? ";

        return jdbcTemplate.queryForObject(sql, subRowMapper, sub_id);
    }
//===========================================================================================


    public List<Task> showAllTasksFromSub(int sub_id) {

        String sql = "SELECT t.task_name, t.task_desc, t.task_deadline, t.task_timeEstimate, t.task_dedicatedHours, t.task_status" +
                "FROM Tasks t " +
                "JOIN SubProjects sP ON t.sub_id = sP_id" +
                "WHERE sP.sub_id = ?";

        return jdbcTemplate.query(sql, new TaskRowMapper(), sub_id);

    }

    public Task getTask(int task_id) {
        String sql = "SELECT t.task_name, t.task_desc, t.task_deadline, t.task_timeEstimate, t.task_dedicatedHours, t.task_status " +
                " FROM Tasks t WHERE task_id = ?";

        return jdbcTemplate.queryForObject(sql, new TaskRowMapper(), task_id);
    }

}
