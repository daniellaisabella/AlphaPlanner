package org.example.alphaplanner.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.alphaplanner.models.SubProject;
import org.example.alphaplanner.models.Task;
import org.example.alphaplanner.service.LabelService;
import org.example.alphaplanner.service.SubProjectService;
import org.example.alphaplanner.service.TaskService;
import org.example.alphaplanner.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.text.IsEmptyString.emptyString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(SubProjectController.class)
class SubProjectControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private SubProject subProject;

    @MockitoBean
    private LabelService labelService;
    @MockitoBean
    private TaskService taskService;
    @MockitoBean
    private SubProjectService subProjectService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private MockHttpSession session;

    @BeforeEach
    void setUp() {
        session = new MockHttpSession();
        session.setAttribute("userId", 1);

        SubProject sub = new SubProject(
                1,
                "UI Design",
                "Design af brugergrænseflade",
                LocalDate.of(2024, 12, 31),
                false,
                10.0,
                100.0,
                1
        );
        when(userService.getUserRole(1)).thenReturn("project manager");
        when(subProjectService.getSubProjects(1)).thenReturn(List.of(sub));
        when(subProjectService.getSubProject(1)).thenReturn(sub);
        when(taskService.showAllTasksFromSub(1)).thenReturn(List.of());
        when(labelService.getLabelsInString(any())).thenReturn("Label1, Label2"); // Return mock labels

    }


    @Test
    void testShowSub() throws Exception {
        mockMvc.perform(get("/subprojects/showsub").param("subId", "1").session(session))
                .andExpect(status().isOk())
                .andExpect(view().name("subProject"))
                .andExpect(model().attributeExists("sub"))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attribute("labels", notNullValue()))
                .andExpect(model().attribute("labels", not(emptyString())));
    }

@Test
    void testEdit() throws Exception {
        mockMvc.perform(post("subprojects/edit"))
}

    @PostMapping("/edit")
    private String edit(HttpServletRequest request, HttpSession session, @ModelAttribute SubProject freshSubProject) {
        if (isloggedIn(session)) return "redirect:";
        subProjectService.updateSubProject(freshSubProject);
        String referer = request.getHeader("referer");
        return "redirect:" + referer;
    }

}


