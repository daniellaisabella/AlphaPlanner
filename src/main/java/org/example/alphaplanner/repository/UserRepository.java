package org.example.alphaplanner.repository;

import org.example.alphaplanner.models.Skill;
import org.example.alphaplanner.models.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

        for(User u : users){
            List<Skill> skills = getUserSKills(u.getUserId());
            u.setSkills(skills);
        }
        return users;

    }

    public List<Skill> getUserSKills (int user_id){

        String sql = "SELECT SKILL_NAME " +
                "FROM SKILLS S " +
                "JOIN USERS_SKILLS US ON S.SKILL_ID = US.SKILL_ID " +
                "WHERE US.USER_ID = ?";
        return jdbcTemplate.query(sql, new SkillRowMapper(), user_id);
    }

    public void saveUser(User user) {

    }

    public boolean checkForDup(String email) {

        return true;
    }


    public List<String> getRoles() {
        String sql = "SHOW COLUMNS FROM Users WHERE Field = 'role'";
        return jdbcTemplate.query(sql, rs -> {
            if (rs.next()) {
                String enumStr = rs.getString("Type"); // f.eks. enum('employee','project manager','admin')
                // Udtræk værdierne fra enum-strengen
                enumStr = enumStr.substring(enumStr.indexOf("(") + 1, enumStr.indexOf(")"));
                String[] values = enumStr.replace("'", "").split(",");
                return Arrays.asList(values);
            }
            return List.of();
        });
    }

    public List<String> getSkills() {
        String sql = "SELECT skill_id, skill_name FROM Skills";
        return jdbcTemplate.query(sql, (rs, rowNum) -> rs.getString("skill_name"));
    }
}
