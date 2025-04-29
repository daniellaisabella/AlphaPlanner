package org.example.alphaplanner.repository.rowmappers;

import org.example.alphaplanner.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(
                rs.getString("user_name"),
                rs.getString("email"),
                rs.getString("role")
        );
    }
}
