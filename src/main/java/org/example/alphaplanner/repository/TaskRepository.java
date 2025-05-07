package org.example.alphaplanner.repository;

import org.example.alphaplanner.models.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
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

//--------------------------------TASK METHODS--------------------------------------------------------------------------

//dummy method to get a subprojec DELETE LATER===============================================================================

    public class SubRowMapper implements RowMapper<SubProject> {

        @Override
        public SubProject mapRow(ResultSet rs, int rowNum) throws SQLException {
            int id = rs.getInt("sub_id");
            String name = rs.getString("sub_name");
            String desc = rs.getString("sub_desc");
            LocalDate deadline = rs.getDate("sub_deadLine").toLocalDate();
            int projectID = rs.getInt("project_id");


            return new SubProject(id, name, desc, deadline, projectID);
        }
    }

    public SubProject getSubdummy(int sub_id) {
        String sql = "SELECT sP.sub_id, sP.sub_name, sP.sub_desc, sP.sub_deadLine " +
                "FROM SubProjects sP " +
                "WHERE sP.sub_id = ? ";

        return jdbcTemplate.queryForObject(sql, subRowMapper, sub_id);
    }
//===========================DELETE LATER================================================================


    public List<Task> showAllTasksFromSub(int sub_id) {

        String sql = "SELECT t.task_id, t.task_name, t.task_desc, t.task_deadline, t.task_timeEstimate, t.task_dedicatedHours, t.task_status " +
                "FROM Tasks t " +
                "WHERE t.sub_id = ?";

        List<Task> tasks =  jdbcTemplate.query(sql, new TaskRowMapper(), sub_id);

        for (Task t : tasks){
            List<Label> labels = getLabelsFromTask(t.getTaskId());
            t.setLabels(getLabelsInString(labels));
        }

        return tasks;

    }



    public Task getTask(int task_id) {

        String sql = "SELECT t.task_name, t.task_desc, t.task_deadline, t.task_timeEstimate, t.task_dedicatedHours, t.task_status " +
                " FROM Tasks t WHERE task_id = ?";

        return jdbcTemplate.queryForObject(sql, new TaskRowMapper(), task_id);
    }

    public void createTask(int sub_id, String name, String desc, LocalDate deadline, double estimatedHours, List<Label> labels){

        // creates a new task
        Date sqlDeadline = Date.valueOf(deadline); // changes localDate to java.sql.date so it can be used in the sql entry
        String sql = "INSERT INTO Tasks (sub_id, task_name, task_desc, task_timeEstimate, task_dedicatedHours)";

        jdbcTemplate.update(sql, sub_id, name, desc, sqlDeadline, estimatedHours, 0);

        // links the task with the labels (not ready yet)


    }

    public void editTask(){

    }

    public void deleteTask(){

    }



//--------------------------------------Label Methods-------------------------------------------------------------------

    public List<Label> getLabelsFromTask(int task_id){

        String sql = "SELECT l.label_name " +
                "FROM Labels l " +
                "JOIN tasks_labels tl ON l.label_id = tl.label_id " +
                "WHERE tl.task_id = ?";

        return jdbcTemplate.query(sql, new LabelRowMapper(), task_id);
    }

    public List<String> getLabelsInString(List<Label> labels){
        List<String> result = new ArrayList<>();
        for (Label l : labels){
            result.add(l.getLabelName());
        }
        return result;
    }

}
