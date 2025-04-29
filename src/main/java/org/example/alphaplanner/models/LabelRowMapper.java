package org.example.alphaplanner.models;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LabelRowMapper implements RowMapper<Label> {

    @Override
    public Label mapRow(ResultSet rs, int rowNum) throws SQLException {
        String name = rs.getString("label_name");

        return new Label(name);
    }
}
