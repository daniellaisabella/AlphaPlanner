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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        mockMvc.perform(get("/subprojects/showsub") //Mocker en HTTP GET request til controllerens endpoint
                        .param("subId", "1") //simulerer en GET request med sub id 1 til controlleren - Vis / GET subprojekt med id 1
                        .session(session)) //tilføjer en mocket session, for at tjekke status
                .andExpect(status().isOk()) //forventer en logget bruger
                .andExpect(view().name("subProject")) //forventer view HTML "subproject"
                .andExpect(model().attributeExists("sub")) //forventer følgende attributter som Model har (subProject objekt)
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attribute("labels", notNullValue())) //forventer at labels ikke er null og ikke er en tom String
                .andExpect(model().attribute("labels", not(emptyString())));
    }

    @Test
    void testEdit() throws Exception {
        mockMvc.perform(post("/subprojects/edit") //Mocker en HTTP POST request til controllerens endpoint
                        .session(session) //simulerer en mocket session, for at tjekke status
                        .param("subId", "1") //Simulerer følgende form felter, som i HTML form
                        .param("subProjectName", "UI Design")
                        .param("subProjectDesc", "Design af brugergrænseflade")
                        .param("subProjectDeadline", "2024-12-31")
                        .param("status", "false")
                        .param("estimatedTime", "10.0")
                        .param("progress", "100.0")
                        .param("projectId", "1")
                        .header("referer", "/")) //simulerer en header til serveren som hjælper den med at forstå ekstra oplysnigner. Her, hvilken sidebrugeren kom fra
                .andExpect(status().is3xxRedirection()) //forventer at brugeren bliver redirected
                .andExpect(redirectedUrl("/")); //forventer at brugeren bliver redirected til ("/")
    }
 @Test
    void testAdd() throws Exception{
     mockMvc.perform(post("/subprojects/add")
             .session(session)//Mocker en HTTP POST request til Controllerens endpoint
                     .param("subId", "1") //Simulerer følgende form felter, som i HTML form
                     .param("subProjectName", "UI Design")
                     .param("subProjectDesc", "Design af brugergrænseflade")
                     .param("subProjectDeadline", "2024-12-31")
                     .param("status", "false")
                     .param("estimatedTime", "10.0")
                     .param("progress", "100.0")
                     .param("projectId", "1")
                     .header("referer", "/")) //simulerer en header til serveren som hjælper den med at forstå ekstra oplysnigner. Her, hvilken sidebrugeren kom fra
             .andExpect(status().is3xxRedirection()) //forventer at brugeren bliver redirected
             .andExpect(redirectedUrl("/")); //forventer at brugeren bliver redirected til ("/")
 }

}


