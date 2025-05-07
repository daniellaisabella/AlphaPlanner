package org.example.alphaplanner.repository.rowmappers;

import org.example.alphaplanner.models.UserDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDtoRowMapper implements RowMapper<UserDto> {

    @Override
    public UserDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new UserDto(
                rs.getInt("user_id"),
                rs.getString("user_name"),
                rs.getString("role")
        );
    }
}
