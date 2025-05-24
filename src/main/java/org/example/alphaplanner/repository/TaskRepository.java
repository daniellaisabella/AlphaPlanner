package org.example.alphaplanner.repository;

import org.example.alphaplanner.models.*;
import org.example.alphaplanner.models.Dto.UserDto;
import org.example.alphaplanner.repository.rowmappers.TaskRowMapper;
import org.example.alphaplanner.repository.rowmappers.UserDtoRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;


//---------------------------------CONSTRUCTOR--------------------------------------------------------------------------

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

//--------------------------------TASK METHODS--------------------------------------------------------------------------



//============================================TASK METHODS==============================================================

    public List<Task> showAllTasksFromSub(int sub_id) {
        try {
            String sql = "SELECT t.task_id, t.task_name, t.task_desc, t.task_deadline, t.task_timeEstimate, t.task_dedicatedHours, t.task_status, t.sub_id " +
                    "FROM Tasks t WHERE t.sub_id = ?";

            List<Task> tasks = jdbcTemplate.query(sql, new TaskRowMapper(), sub_id);

            for (Task t : tasks) {
                String labels = getLabelsInString(t.getTaskId());
                t.setLabels(labels);
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
            String sql = "SELECT t.task_id, t.task_name, t.task_desc, t.task_deadline, t.task_timeEstimate, t.task_dedicatedHours, t.task_status, t.sub_id " +
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

            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(1, sub_id);
                ps.setString(2, name);
                ps.setString(3, desc);
                ps.setDate(4, sqlDeadline);
                ps.setDouble(5, estimatedHours);
                ps.setDouble(6, 0); // task_dedicatedHours
                ps.setBoolean(7, false); // task_status
                return ps;
            }, keyHolder);

            // Retrieve the generated task_id
            Number key = keyHolder.getKey();
            if (key == null) {
                throw new IllegalStateException("Failed to retrieve generated task ID.");
            }

            int task_id = key.intValue();

            // Insert into tasks_labels
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

    public int getSumDedicatedHours(int subId)
    {
        String query = """
                SELECT SUM(tasks.task_dedicatedHours) FROM tasks WHERE tasks.sub_id = ?;
                """;
            Integer i = jdbcTemplate.queryForObject(query, Integer.class, subId);
        return Objects.requireNonNullElse(i, 0);
    }

    public int getSumEstimatedHours(int subId)
    {
        String query = """
                SELECT SUM(tasks.task_timeEstimate) FROM tasks WHERE tasks.sub_id = ?;
                """;
        Integer i = jdbcTemplate.queryForObject(query, Integer.class, subId);
        return Objects.requireNonNullElse(i, 0);
    }

//======================================LABEL METHODS===================================================================

    public String getAllLabels() {
        try {
            String sql = "SELECT l.label_id, l.label_name FROM Labels l";
            List<String> labelStrings = jdbcTemplate.query(sql, (rs, rowNum) ->
                    rs.getInt("label_id") + ":" + rs.getString("label_name"));

            return String.join(",", labelStrings);

        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while retrieving all labels.", e);
        }
    }



    public boolean checkIfLabelNameExist(String name) {
        try {
            String sql = "SELECT COUNT(*) FROM Labels WHERE label_name = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, name);
            return count != null && count > 0;
        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while checking label name existence.", e);
        }
    }

//-------------------------INTERACTION BETWEEN LABELS AND TASKS---------------------------------------------------------

    public String getLabelsInString(int task_id) {
        try {
            String sql = "SELECT l.label_id, l.label_name FROM Labels l JOIN tasks_labels tl ON l.label_id = tl.label_id WHERE tl.task_id = ?";

            List<String> labelStrings = jdbcTemplate.query(sql, (rs, rowNum) ->
                    rs.getInt("label_id") + ":" + rs.getString("label_name"), task_id);

            return String.join(",", labelStrings);

        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while retrieving labels for task.", e);
        }
    }

    public void addLabelsToTask(int task_id, List<Integer> labels) {
        try {
            String refreshSql = "DELETE FROM tasks_labels WHERE task_id = ?";
            jdbcTemplate.update(refreshSql, task_id);

            String labelIdSql = "SELECT label_id FROM Labels WHERE label_id = ?";
            String sql = "INSERT INTO tasks_labels (task_id, label_id) VALUES (?, ?)";

            for (Integer l : labels) {
                try {
                    Integer labelId = jdbcTemplate.queryForObject(labelIdSql, Integer.class, l);
                    assert (labelId != null);
                    jdbcTemplate.update(sql, task_id, labelId);
                } catch (EmptyResultDataAccessException ex) {
                    throw new IllegalArgumentException("Label not found: " + l);
                }
            }
        } catch (AssertionError e) {
            throw new IllegalStateException("Database error while adding labels to the task.", e);
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
        List<UserDto> users = new ArrayList<>();
        try {
            String sql = "SELECT u.user_id, u.user_name, u.role FROM Users u JOIN users_tasks uT ON u.user_id = uT.user_id WHERE uT.task_id = ?";
             users = jdbcTemplate.query(sql, new UserDtoRowMapper(), task_id);
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

    public String getAssigneesInString(List<UserDto> assignees) {
        if (assignees == null || assignees.isEmpty()) {
            return "";
        }

        return assignees.stream()
                .filter(a -> a.getName() != null && a.getSkills() != null)
                .map(a -> a.getId() + ":" + a.getName().trim() + ":" + a.getSkills().trim())
                .collect(Collectors.joining(", "));
    }

    public void addAssigneesToTask(int task_id, List<Integer> assignees) {
        try {
            String refreshSql = "DELETE FROM users_tasks WHERE task_id = ?";
            jdbcTemplate.update(refreshSql, task_id);

            String labelIdSql = "SELECT user_id FROM users WHERE user_id = ?";
            String sql = "INSERT INTO users_tasks (task_id, user_id) VALUES (?, ?)";

            for (Integer a : assignees) {
                try {
                    int assigneeId = jdbcTemplate.queryForObject(labelIdSql, Integer.class, a);
                    jdbcTemplate.update(sql, task_id, assigneeId);
                } catch (EmptyResultDataAccessException ex) {
                    throw new IllegalArgumentException("assignee not found: " + a);
                }
            }
        } catch (DataAccessException e) {
            throw new IllegalStateException("Database error while adding assignee to the task.", e);
        }
    }


}
