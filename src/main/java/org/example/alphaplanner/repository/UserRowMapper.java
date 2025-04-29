package org.example.alphaplanner.repository;

import org.example.alphaplanner.models.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {

        int id = rs.getInt("user_id");
        String userName = rs.getString("user_name");
        String email = rs.getString("email");
        String role = rs.getString("role");

        return new User(id,userName,email,role);
    }
}
