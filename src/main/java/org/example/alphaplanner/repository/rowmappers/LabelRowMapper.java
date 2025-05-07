package org.example.alphaplanner.repository.rowmappers;

import org.example.alphaplanner.models.Label;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LabelRowMapper implements RowMapper<Label> {

    @Override
    public Label mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("label_id");
        String name = rs.getString("label_name");

        return new Label(id, name);
    }
}
