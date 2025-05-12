package org.example.alphaplanner.repository;

import org.example.alphaplanner.controller.AlphaPlannerExceptionHandler;
import org.example.alphaplanner.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Sql(
        executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
        scripts = {"classpath:h2init.sql"}
)

@Transactional
@Rollback(true)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testUserLogin(){
        User userTrueLogin = new User("alice@alpha.com","123");
        User userFalseLogin = new User("fail@alpha.com","fail");

        assertTrue(userRepository.login(userTrueLogin));
        assertFalse(userRepository.login(userFalseLogin));
    }

    @Test
    public void testGetUserById_ReturnsCorrectUser(){
        User user = userRepository.getUserById(1);
        assertNotNull(user);
        assertEquals("Alice Johnson", user.getName());
    }

    @Test
    public void testGetUsersByRole_ReturnsCorrectUsers() {
        List<User> admins = userRepository.getUsersByRole("Admin");
        assertEquals(1, admins.size());
        assertEquals("Alice Johnson", admins.get(0).getName());
    }

    @Test
    public void testgetUserSKills() {
        List<String> skills = userRepository.getUserSKills(1);
        assertEquals(skills.size(),1);
        assertTrue(skills.contains("SQL"));
    }

    @Test
    public void testSaveUser() {
        User user = new User("Test Testegaard","test@aplha.com","123","admin");
        List<String> skills = new ArrayList<>();
        skills.add("SQL");
        user.setSkills(skills);
        userRepository.saveUser(user);
        int userId = userRepository.getUserId(user);
        assertEquals("admin",userRepository.getUserRole(userId));
        assertNotNull(userRepository.getUserSKills(userId));
        assertTrue(userRepository.getUserSKills(userId).contains("SQL"));
    }

    @Test
    public void testDeleteUser() {
        userRepository.deleteUser(1);
        assertThrows(RuntimeException.class, () -> userRepository.getUserById(1));
    }

}
