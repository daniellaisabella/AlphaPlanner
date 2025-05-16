package org.example.alphaplanner.repository.rowmappers;

import org.example.alphaplanner.models.Project;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ProjectRowMapper implements RowMapper<Project> {

    @Override
    public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("project_id");
        String name = rs.getString("project_name");
        String desc = rs.getString("project_desc");
        LocalDate deadLine = rs.getDate("project_deadline").toLocalDate();
        boolean projectStatus = rs.getBoolean("project_status");
        double estimatedTime = rs.getDouble("project_timeEstimate");
        double dedicatedTime = rs.getDouble("project_dedicatedHours");
        int pmId = rs.getInt("pm_id");
        return new Project(id, name, desc, deadLine, projectStatus, estimatedTime, dedicatedTime, pmId );
    }
}
