package org.example.alphaplanner.models;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class SubProjectRowMApper implements RowMapper<SubProject> {
    @Override
    public SubProject mapRow(ResultSet rs, int rowNum) throws SQLException {
        int subID = rs.getInt("sub_id");
        String name = rs.getString("sub_name");
        String desc = rs.getString("sub_desc");
        LocalDate deadLine = rs.getDate("sub_deadline").toLocalDate();
        boolean projectStatus = rs.getBoolean("sub_status");
        double estimatedTime = rs.getDouble("sub_timeEstimate");
        double dedicatedTime = rs.getDouble("sub_dedicatedHours");
        int projectID = rs.getInt("project_id");
        return new SubProject(subID,name, desc, deadLine, projectStatus, estimatedTime, dedicatedTime, projectID);
    }
}
