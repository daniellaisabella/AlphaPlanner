package org.example.alphaplanner.repository;

import org.example.alphaplanner.models.*;
import org.example.alphaplanner.repository.rowmappers.LabelRowMapper;
import org.example.alphaplanner.repository.rowmappers.TaskRowMapper;
import org.example.alphaplanner.repository.rowmappers.UserDtoRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    //dummy method to get a subproject DELETE LATER===============================================================================

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
        try {
            String sql = "SELECT sP.sub_id, sP.sub_name, sP.sub_desc, sP.sub_deadLine " +
                    "FROM SubProjects sP WHERE sP.sub_id = ? ";
            return jdbcTemplate.queryForObject(sql, subRowMapper, sub_id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Subproject not found with id: " + sub_id);
        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while retrieving subproject.", e);
        }
    }

//============================================TASK METHODS==============================================================

    public List<Task> showAllTasksFromSub(int sub_id) {
        try {
            String sql = "SELECT t.task_id, t.task_name, t.task_desc, t.task_deadline, t.task_timeEstimate, t.task_dedicatedHours, t.task_status " +
                    "FROM Tasks t WHERE t.sub_id = ?";

            List<Task> tasks = jdbcTemplate.query(sql, new TaskRowMapper(), sub_id);

            for (Task t : tasks) {
                List<Label> labels = getLabelsFromTask(t.getTaskId());
                t.setLabels(getLabelsInString(labels));
                List<UserDto> users = getAssigneesFromTask(t.getTaskId());
                t.setAssignees(getAssigneesInString(users));
            }
            return tasks;
        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while retrieving tasks for subproject.", e);
        }
    }

    public Task getTaskById(int task_id) {
        try {
            String sql = "SELECT t.task_name, t.task_desc, t.task_deadline, t.task_timeEstimate, t.task_dedicatedHours, t.task_status " +
                    "FROM Tasks t WHERE task_id = ?";
            return jdbcTemplate.queryForObject(sql, new TaskRowMapper(), task_id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Task not found with id: " + task_id);
        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while retrieving task by id.", e);
        }
    }

    public void createTask(int sub_id, String name, String desc, LocalDate deadline, double estimatedHours, List<Integer> labels_id) {
        try {
            Date sqlDeadline = Date.valueOf(deadline);
            String sql = "INSERT INTO Tasks (sub_id, task_name, task_desc, task_deadline, task_timeEstimate, task_dedicatedHours, task_status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

            jdbcTemplate.update(sql, sub_id, name, desc, sqlDeadline, estimatedHours, 0, false);

            String getTaskId = "SELECT t.task_id FROM Tasks t WHERE task_name = ? AND task_desc = ?";
            int task_id = jdbcTemplate.queryForObject(getTaskId, Integer.class, name, desc);

            String labelSql = "INSERT INTO tasks_labels (label_id, task_id) VALUES (?, ?)";
            for (Integer l : labels_id) {
                jdbcTemplate.update(labelSql, l, task_id);
            }
        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while creating task.", e);
        }
    }

    public void editTask(int task_id, String name, String desc, LocalDate deadline, double estimatedHours, double dedicatedHours, boolean status) {
        try {
            String sql = "UPDATE Tasks SET task_name = ?, task_desc = ?, task_deadline = ?, task_timeEstimate = ?,  task_dedicatedHours = ?, task_status = ? WHERE task_id = ?";
            jdbcTemplate.update(sql, name, desc, deadline, estimatedHours, dedicatedHours, status, task_id);
        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while editing task.", e);
        }
    }

    public void deleteTask(int task_id) {
        try {
            String sql = "DELETE FROM Tasks WHERE task_id = ?";
            jdbcTemplate.update(sql, task_id);
        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while deleting task.", e);
        }
    }

//======================================LABEL METHODS===================================================================

    public List<Label> getAllLabels() {
        try {
            String sql = "SELECT l.label_id, l.label_name FROM Labels l";
            return jdbcTemplate.query(sql, new LabelRowMapper());
        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while retrieving all labels.", e);
        }
    }

    public Label getLabelById(int label_id) {
        try {
            String sql = "SELECT l.label_id, l.label_name FROM Labels l WHERE l.label_id = ?";
            return jdbcTemplate.queryForObject(sql, new LabelRowMapper(), label_id);
        } catch (EmptyResultDataAccessException e) {
            throw new IllegalArgumentException("Label not found with id: " + label_id);
        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while retrieving label by id.", e);
        }
    }

    public boolean checkIfLabelNameExist(String name) {
        try {
            String sql = "SELECT l.label_id, l.label_name FROM Labels l WHERE l.label_name = ?";
            List<Label> labels = jdbcTemplate.query(sql, new LabelRowMapper(), name);
            return !labels.isEmpty();
        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while checking label name existence.", e);
        }
    }

//-------------------------INTERACTION BETWEEN LABELS AND TASKS---------------------------------------------------------

    public List<Label> getLabelsFromTask(int task_id) {
        try {
            String sql = "SELECT l.label_id, l.label_name FROM Labels l JOIN tasks_labels tl ON l.label_id = tl.label_id WHERE tl.task_id = ?";
            return jdbcTemplate.query(sql, new LabelRowMapper(), task_id);
        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while retrieving labels for task.", e);
        }
    }

    public List<String> getLabelsInString(List<Label> labels) {
        List<String> result = new ArrayList<>();
        for (Label l : labels) {
            result.add(l.getLabelName());
        }
        return result;
    }

    public void addLabelsToTask(int task_id, List<String> labels) {
        try {
            String refreshSql = "DELETE FROM tasks_labels WHERE task_id = ?";
            jdbcTemplate.update(refreshSql, task_id);

            String labelIdSql = "SELECT label_id FROM Labels WHERE label_name = ?";
            String sql = "INSERT INTO tasks_labels (task_id, label_id) VALUES (?, ?)";

            for (String l : labels) {
                try {
                    int labelId = jdbcTemplate.queryForObject(labelIdSql, Integer.class, l);
                    jdbcTemplate.update(sql, task_id, labelId);
                } catch (EmptyResultDataAccessException ex) {
                    throw new IllegalArgumentException("Label not found: " + l);
                }
            }
        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while adding labels to the task.", e);
        }
    }

    public void removeLabelFromTask(int task_id, int label_id) {
        try {
            String sql = "DELETE FROM tasks_labels WHERE task_id = ? AND label_id = ?";
            jdbcTemplate.update(sql, task_id, label_id);
        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while removing label from task.", e);
        }
    }

    //-------------------------------CRUD METHODS LABELS--------------------------------------------------------------------

    public void createLabel(String name) {
        try {
            String sql = "INSERT INTO Labels (label_name) VALUES (?)";
            jdbcTemplate.update(sql, name);
        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while creating label.", e);
        }
    }

    public void deleteLabel(int label_id) {
        try {
            String sql = "DELETE FROM Labels WHERE label_id = ?";
            jdbcTemplate.update(sql, label_id);
        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while deleting label.", e);
        }
    }

//==================================ASSIGNEES METHODS===================================================================

    public List<UserDto> getAssigneesFromTask(int task_id) {
        try {
            String sql = "SELECT u.user_id, u.user_name, u.role FROM Users u JOIN users_tasks uT ON u.user_id = uT.user_id WHERE uT.task_id = ?";
            return jdbcTemplate.query(sql, new UserDtoRowMapper(), task_id);
        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while retrieving assignees from task.", e);
        }
    }

    public List<String> getAssigneesInString(List<UserDto> users) {
        List<String> result = new ArrayList<>();
        for (UserDto u : users) {
            result.add(u.getName());
        }
        return result;
    }

    public void addAssigneesToTask(int task_id, int user_id) {
        try {
            String sql = "INSERT INTO users_tasks (user_id, task_id) VALUES (?, ?)";
            jdbcTemplate.update(sql, user_id, task_id);
        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while adding assignee to task.", e);
        }
    }

    public void removeAssigneesFromTask(int task_id, int user_id) {
        try {
            String sql = "DELETE FROM users_tasks WHERE task_id = ? AND user_id = ?";
            jdbcTemplate.update(sql, task_id, user_id);
        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while removing assignee from task.", e);
        }
    }
}
