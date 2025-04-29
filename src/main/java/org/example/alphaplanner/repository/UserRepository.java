package org.example.alphaplanner.repository;

import org.example.alphaplanner.models.User;
import org.example.alphaplanner.repository.rowmappers.UserRowMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean login(User user) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE EMAIL = ? AND PASSWORD = ?";
        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, user.getEmail(), user.getPassword());
            return count != null && count > 0;
        } catch (DataAccessException d){
            throw new RuntimeException("Error during login ", d);
        }
    }

    public int getUserId(User user) {
        String sql = "SELECT USER_ID FROM USERS WHERE EMAIL = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, user.getEmail());
        } catch (DataAccessException d){
            throw new RuntimeException("Error getting user_id ", d);
        }
    }

    public String getUserRole(Object userId) {
        String sql = "SELECT ROLE FROM USERS WHERE USER_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, userId);
        } catch (DataAccessException d){
            throw new RuntimeException("Error getting user role ", d);
        }
    }

    public List<User> getUsersByRole(String role) {
        String userSql = "SELECT * FROM USERS WHERE ROLE = ?";
        List<User> users = jdbcTemplate.query(userSql, new UserRowMapper(), role);
        return users;

    }
}
