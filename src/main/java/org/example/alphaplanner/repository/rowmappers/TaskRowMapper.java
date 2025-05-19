package org.example.alphaplanner.repository.rowmappers;

import org.example.alphaplanner.models.Task;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class TaskRowMapper implements RowMapper<Task> {

    @Override
    public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("task_id");
        String name = rs.getString("task_name");
        String desc = rs.getString("task_desc");
        LocalDate deadLine = rs.getDate("task_deadLine").toLocalDate();
        double estimatedHours = rs.getDouble("task_timeEstimate");
        double dedicatedHours = rs.getDouble("task_dedicatedHours");
        int subId = rs.getInt("sub_id");
        boolean status = rs.getBoolean("task_status");

        return new Task(id, name, desc, deadLine, estimatedHours, dedicatedHours, subId, status);
    }
}
