package org.example.alphaplanner.controller;

import org.example.alphaplanner.models.User;
import org.example.alphaplanner.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    private User user;
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        user = new User("Alice", "alice@alpha.com", "123", "admin");
        session = new MockHttpSession();
        session.setAttribute("userId", 1);
    }

    @Test
    void testShowLogin() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("userLogin"));
    }

    @Test
    void testLoginSuccess() throws Exception {
        Mockito.when(userService.login(any(User.class))).thenReturn(true);
        Mockito.when(userService.getUserId(any(User.class))).thenReturn(1);

        mockMvc.perform(post("/")
                        .param("email", "alice@alpha.com")
                        .param("password", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));
    }

    @Test
    void testLoginFailure() throws Exception {
        Mockito.when(userService.login(any(User.class))).thenReturn(false);

        mockMvc.perform(post("/")
                        .param("email", "wrong@alpha.com")
                        .param("password", "wrong"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("wrongInput"));
    }

    @Test
    void testLogout() throws Exception {
        mockMvc.perform(get("/logout").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void testProfileRedirectAdmin() throws Exception {
        Mockito.when(userService.getUserRole(1)).thenReturn("admin");

        mockMvc.perform(get("/profile").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin1"));
    }

    @Test
    void testProfileRedirectEmployee() throws Exception {
        Mockito.when(userService.getUserRole(1)).thenReturn("employee");

        mockMvc.perform(get("/profile").session(session))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/projects"));
    }

    @Test
    void testCreateUser() throws Exception {
        Mockito.when(userService.getRoles()).thenReturn(Collections.singletonList("employee"));
        Mockito.when(userService.getSkills()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/create").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("createUser"))
                .andExpect(model().attributeExists("user", "roles", "enumSkills"));
    }

    @Test
    void testSaveUserDuplicate() throws Exception {
        Mockito.when(userService.checkForDup("alice@alpha.com")).thenReturn(true);

        mockMvc.perform(post("/save")
                        .param("email", "alice@alpha.com")
                        .param("role", "admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("/createUser"))
                .andExpect(model().attributeExists("duplicate"));
    }

    @Test
    void testSaveUserSuccess() throws Exception {
        Mockito.when(userService.checkForDup("bob@alpha.com")).thenReturn(false);

        mockMvc.perform(post("/save")
                        .param("email", "bob@alpha.com")
                        .param("role", "employee"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin2"));
    }

//    @Test
//    void testEditUser() throws Exception {
//
//    }

    @Test
    void testUpdateUser() throws Exception {
        mockMvc.perform(post("/update")
                        .param("emailPrefix", "bob")
                        .param("role", "employee"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin2"));
    }

    @Test
    void testDeleteUser() throws Exception {
        Mockito.when(userService.getUserRole(1)).thenReturn("employee");

        mockMvc.perform(post("/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin2"));
    }


}
