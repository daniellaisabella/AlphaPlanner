package org.example.alphaplanner.repository;

import org.example.alphaplanner.models.*;
import org.example.alphaplanner.repository.rowmappers.LabelRowMapper;
import org.example.alphaplanner.repository.rowmappers.TaskRowMapper;
import org.example.alphaplanner.repository.rowmappers.UserDtoRowMapper;
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

            return new SubProject(id, name, desc, deadline);
        }
    }

    public SubProject getSubdummy(int sub_id) {
        String sql = "SELECT sP.sub_id, sP.sub_name, sP.sub_desc, sP.sub_deadLine " +
                "FROM SubProjects sP " +
                "WHERE sP.sub_id = ? ";

        return jdbcTemplate.queryForObject(sql, subRowMapper, sub_id);
    }
//===========================DELETE LATER================================================================

//============================================TASK METHODS==============================================================
    public List<Task> showAllTasksFromSub(int sub_id) {

        String sql = "SELECT t.task_id, t.task_name, t.task_desc, t.task_deadline, t.task_timeEstimate, t.task_dedicatedHours, t.task_status " +
                "FROM Tasks t " +
                "WHERE t.sub_id = ?";

        List<Task> tasks = jdbcTemplate.query(sql, new TaskRowMapper(), sub_id);

        for (Task t : tasks) {
            List<Label> labels = getLabelsFromTask(t.getTaskId());
            t.setLabels(getLabelsInString(labels));
            List<UserDto> users = getAssigneesFromTask(t.getTaskId());
            t.setAssignees(getAssigneesInString(users));
        }
        return tasks;
    }


    public Task getTaskById(int task_id) {

        String sql = "SELECT t.task_name, t.task_desc, t.task_deadline, t.task_timeEstimate, t.task_dedicatedHours, t.task_status " +
                " FROM Tasks t WHERE task_id = ?";

        return jdbcTemplate.queryForObject(sql, new TaskRowMapper(), task_id);
    }

    public void createTask(int sub_id, String name, String desc, LocalDate deadline, double estimatedHours, List<Integer> labels_id) {

        // creates a new task
        Date sqlDeadline = Date.valueOf(deadline); // changes localDate to java.sql.date so it can be used in the sql entry
        String sql = "INSERT INTO Tasks (sub_id, task_name, task_desc, task_deadline, task_timeEstimate, task_dedicatedHours, task_status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        jdbcTemplate.update(sql, sub_id, name, desc, sqlDeadline, estimatedHours, 0, false);

        // get the task id
        String getTaskId = "SELECT t.task_id " +
                "FROM Tasks t " +
                "WHERE task_name = ? AND task_desc = ?";
        int task_id = jdbcTemplate.queryForObject(getTaskId, Integer.class, name, desc);

        // links the task with the labels
        String labelSql = "INSERT INTO tasks_labels (label_id, task_id) VALUES (?, ?)";

        for (Integer l : labels_id) {
            jdbcTemplate.update(labelSql, l, task_id);
        }

    }

    public void editTask(int task_id, String name, String desc, LocalDate deadline, double estimatedHours, double dedicatedHours, boolean status) {

        String sql = "UPDATE Tasks " +
                "SET task_name = ?, task_desc = ?, task_deadline = ?, task_timeEstimate = ?,  task_dedicatedHours = ?, task_status = ? " +
                "WHERE task_id = ? ";

        jdbcTemplate.update(sql, name, desc, deadline, estimatedHours, dedicatedHours, status, task_id);
    }

    public void deleteTask(int task_id) {

        String sql = "DELETE FROM Tasks " +
                "WHERE task_id = ?";

        jdbcTemplate.update(sql, task_id);
    }


//======================================LABEL METHODS===================================================================

    public List<Label> getAllLabels() {
        String sql = "SELECT l.label_id, l.label_name " +
                "FROM Labels l";

        return jdbcTemplate.query(sql, new LabelRowMapper());
    }

    public Label getLabelById(int label_id) {
        String sql = "SELECT l.label_id, l.label_name " +
                "FROM Labels l " +
                "WHERE l.label_id = ? ";

        return jdbcTemplate.queryForObject(sql, new LabelRowMapper(), label_id);
    }
//-------------------------INTERACTION BETWEEN LABELS AND TASKS---------------------------------------------------------

    public List<Label> getLabelsFromTask(int task_id) {

        String sql = "SELECT l.label_id, l.label_name " +
                "FROM Labels l " +
                "JOIN tasks_labels tl ON l.label_id = tl.label_id " +
                "WHERE tl.task_id = ?";

        return jdbcTemplate.query(sql, new LabelRowMapper(), task_id);
    }



    public List<String> getLabelsInString(List<Label> labels) { // This method is only used in this repository so its mark as private
        List<String> result = new ArrayList<>();
        for (Label l : labels) {
            result.add(l.getLabelName());
        }
        return result;

    }

    public void addLabelsToTask(int task_id, List<String> labels) {

        //deletes the previous labels so only the new ones exist
        String refreshSql = "DELETE FROM tasks_labels WHERE task_id = ?";
        jdbcTemplate.update(refreshSql, task_id);

        // get label id
        String labelIdSql = "SELECT label_id FROM Labels WHERE label_name = ?";
        String sql = "INSERT INTO tasks_labels (task_id, label_id) VALUES (?, ?)";

        for (String l : labels){
            int labelId = jdbcTemplate.queryForObject(labelIdSql, Integer.class, l);
            jdbcTemplate.update(sql, task_id, labelId);
        }
    }


    public void removeLabelFromTask(int task_id, int label_id) {

        String sql = "DELETE FROM tasks_labels WHERE task_id = ? AND label_id = ?";
        jdbcTemplate.update(sql, task_id, label_id);
    }

    //-------------------------------CRUD METHODS LABELS--------------------------------------------------------------------
    public void createLabel(String name) {

        String sql = "INSERT INTO Labels (label_name) VALUES (?)";

        jdbcTemplate.update(sql, name);

    }

    // There is no need to edit the labels, if you want a different one or you dont like it, you can create/delete it

    public void deleteLabel(int label_id) {

        String sql = "DELETE FROM Labels " +
                "WHERE label_id = ?";

        jdbcTemplate.update(sql, label_id);
    }

//==================================ASSIGNEES METHODS===================================================================


    public List<UserDto> getAssigneesFromTask(int task_id) {
        String sql = "SELECT u.user_id, u.user_name, u.role " +
                "FROM Users u " +
                "JOIN users_tasks uT ON u.user_id = uT.user_id " +
                "WHERE uT.task_id = ?";

        return jdbcTemplate.query(sql, new UserDtoRowMapper(), task_id);
    }

    public List<String> getAssigneesInString(List<UserDto> users) {
        List<String> result = new ArrayList<>();

        for (UserDto u : users) {
            result.add(u.getName());
        }
        return result;
    }

    public void addAssigneesToTask(int task_id, int user_id) {

        String sql = "INSERT INTO users_tasks (user_id, task_id) VALUES (?, ?)";

        jdbcTemplate.update(sql, user_id, task_id);
    }

    public void removeAssigneesFromTask(int task_id, int user_id) {

        String sql = "DELETE FROM users_tasks " +
                "WHERE task_id = ? AND user_id = ?";

        jdbcTemplate.update(sql, task_id, user_id);

    }
}
