package org.example.alphaplanner.repository;

import org.example.alphaplanner.models.Skill;
import org.example.alphaplanner.models.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
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
        } catch (DataAccessException d) {
            throw new RuntimeException("Error during login ", d);
        }
    }

    public User getUserById(int userId) {
        String sql = "SELECT * FROM USERS WHERE USER_ID = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new UserRowMapper(), userId);
            user.setSkills(getUserSKills(userId));
            return user;
        } catch (DataAccessException d){
            throw new RuntimeException("Error getting user ", d);
        }
    }

    public int getUserId(User user) {
        String sql = "SELECT USER_ID FROM USERS WHERE EMAIL = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, user.getEmail());
        } catch (DataAccessException d) {
            throw new RuntimeException("Error getting user_id ", d);
        }
    }

    public String getUserRole(Object userId) {
        String sql = "SELECT ROLE FROM USERS WHERE USER_ID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, userId);
        } catch (DataAccessException d) {
            throw new RuntimeException("Error getting user role ", d);
        }
    }

    public List<User> getUsersByRole(String role) {
        String userSql = "SELECT * FROM USERS WHERE ROLE = ?";
        List<User> users = jdbcTemplate.query(userSql, new UserRowMapper(), role);

        for (User u : users) {
            List<Skill> skills = getUserSKills(u.getUserId());
            u.setSkills(skills);
        }
        return users;
    }

    public List<Skill> getUserSKills(int user_id) {

        String sql = "SELECT SKILL_NAME " +
                "FROM SKILLS S " +
                "JOIN USERS_SKILLS US ON S.SKILL_ID = US.SKILL_ID " +
                "WHERE US.USER_ID = ?";
        return jdbcTemplate.query(sql, new SkillRowMapper(), user_id);
    }

    public void saveUser(User user) {
        String insertUserSql = """
                     INSERT INTO USERS (USER_NAME, EMAIL, ROLE, PASSWORD)
                     VALUES(?, ?, ?, ?)
                """;
        String getUserIdSql = "SELECT USER_ID FROM USERS WHERE EMAIL = ?";

        try {
            jdbcTemplate.update(insertUserSql, user.getName(), user.getEmail(), user.getRole(), user.getPassword());
            int userId = jdbcTemplate.queryForObject(getUserIdSql, Integer.class,user.getEmail());
            saveSkills(user,userId);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Fejl ved oprettelse af bruger" + e);
        }

    }

    public void saveSkills(User user,int userId){
        for (Skill s : user.getSkills()){
            String getSkillIdSql = "SELECT SKILL_ID FROM SKILLS WHERE SKILL_NAME = ?";
            String insertUserSkill = """
                     INSERT INTO USERS_SKILLS (USER_ID,SKILL_ID)
                     VALUES(?, ?)
                """;
            try{
                int skillId = jdbcTemplate.queryForObject(getSkillIdSql, Integer.class,s.getSkillName());
                jdbcTemplate.update(insertUserSkill,userId,skillId);
            } catch (DataIntegrityViolationException e){
                throw new IllegalArgumentException("Fejl ved oprettelse af skills" + e);
            }
        }
    }


    public boolean checkForDup(String email) {
        String sql = """
                SELECT COUNT(*) > 0 FROM USERS WHERE BINARY EMAIL = ?;
                
                
                
                """;
        try {
            Boolean userExist = jdbcTemplate.queryForObject(sql, Boolean.class, email);
            return Boolean.TRUE.equals(userExist);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Fejl ved oprettelse af bruger" + e);
        }

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
        return jdbcTemplate.query(sql, (rs, rowNum) ->
                rs.getString("skill_name")
        );
    }

    //---------------- DELETE METHOD --------------------------

    public void deleteUser(int userId) {

        String deleteSkills ="DELETE FROM USERS_SKILLS WHERE USER_ID = ?";
        String deleteTasks = "DELETE FROM USERS_TASKS WHERE USER_ID = ?";
        String deleteProjects = "DELETE FROM USERS_PROJECTS WHERE USER_ID = ?";
        String deleteUserSql ="DELETE FROM USERS WHERE USER_ID = ?";

        try {
            jdbcTemplate.update(deleteSkills, userId);
            jdbcTemplate.update(deleteTasks, userId);
            jdbcTemplate.update(deleteProjects, userId);
            jdbcTemplate.update(deleteUserSql, userId);
        }catch (DataIntegrityViolationException e){
            throw new IllegalArgumentException("Fejl ved sletning af bruger" + e);
        }

    }


    public void updateUser(User user) {
        User existingUser = getUserById(user.getUserId());
        if (user.getPassword() == null || user.getPassword().isBlank()) {

            user.setPassword(existingUser.getPassword());
        }

        if (user.getSkills() == null || user.getSkills().isEmpty()) {
            user.setSkills(existingUser.getSkills());
        }

        String updateUserSql = """
                UPDATE USERS
                SET USER_NAME = ?, EMAIL = ?, ROLE = ?, PASSWORD = ?
                WHERE USER_ID = ?
                """;

        try {
            jdbcTemplate.update(updateUserSql, user.getName(), user.getEmail(), user.getRole(), user.getPassword(),user.getUserId() );
            updateSkills(user);
        }catch (DataIntegrityViolationException e){
            throw new IllegalArgumentException("Fejl ved opdatering af bruger" + e.getMessage());
        }
    }

    public void updateSkills(User user) {
        String deleteSkills ="DELETE FROM USERS_SKILLS WHERE USER_ID = ?";

        try{
            jdbcTemplate.update(deleteSkills, user.getUserId());
        } catch (DataIntegrityViolationException d) {
            throw new IllegalArgumentException("Fejl ved opdatering af skills (slet)" + d);
        }

        for (Skill s : user.getSkills()){
            String getSkillIdSql = "SELECT SKILL_ID FROM SKILLS WHERE SKILL_NAME = ?";
            String insertUserSkill = """
                     INSERT INTO USERS_SKILLS (USER_ID,SKILL_ID)
                     VALUES(?, ?)
                """;
            try{
                int skillId = jdbcTemplate.queryForObject(getSkillIdSql, Integer.class,s.getSkillName());
                jdbcTemplate.update(insertUserSkill,user.getUserId(),skillId);
            } catch (DataIntegrityViolationException e){
                throw new IllegalArgumentException("Fejl ved opdatering af skills (indsæt)" + e);
            }
        }
    }


}
