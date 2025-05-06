package org.example.alphaplanner.controller;

import org.example.alphaplanner.models.User;
import org.example.alphaplanner.service.BaseService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BaseService service;

    private User user;

    @BeforeEach
    void setUp(){
        User user = new User("Alice", "alice@alpha.com","123","admin");
    }
}
