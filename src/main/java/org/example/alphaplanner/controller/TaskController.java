package org.example.alphaplanner.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.alphaplanner.models.SubProject;
import org.example.alphaplanner.models.Task;
import org.example.alphaplanner.service.BaseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/taks")
public class TaskController {

    private final BaseService service;

    public TaskController(BaseService service) {
        this.service = service;
    }

    // dummy method to get into the task of a specific subproject=============================================
    @GetMapping("/showSub")
    public String showSub(Model model) {
        SubProject subProject = service.getSubdummy(1);
        model.addAttribute("sub", subProject);
        List<Task> tasks = service.showAllTasksFromSub(1);


        model.addAttribute("tasks", tasks);
        return "allTasksFromBob";
    }
//===========================================================
}
