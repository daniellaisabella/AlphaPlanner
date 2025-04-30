package org.example.alphaplanner.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.example.alphaplanner.models.Label;
import org.example.alphaplanner.models.SubProject;
import org.example.alphaplanner.models.Task;
import org.example.alphaplanner.service.BaseService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final BaseService service;

    public TaskController(BaseService service) {
        this.service = service;
    }
//------------------------HELPER METHOD TO CHECK IF USER IS LOGGED IN------------------------------------
    private boolean isLoggedIn(HttpSession session){
        return session.getAttribute("userId") !=null;
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


    @GetMapping("/createTask")
    public String createTask(Model model, int sub_id, HttpSession session) {
        SubProject subProject = service.getSubdummy(1);
        model.addAttribute("sub_id", sub_id);
        List<Label> labels = service.getAllLabels();
        model.addAttribute("labels", labels);
        return isLoggedIn(session) ? "createTask": "redirect:/login";
    }

    @PostMapping("/saveTask")
    public String saveTask(int sub_id,
                           String name,
                           String desc,
                           LocalDate deadline,
                           double estimatedHours,
                           @RequestParam(name="labels_id", required = false)ArrayList<Integer> labels_id, HttpSession session) {

        System.out.println("labels_id: " + labels_id);


        if (isLoggedIn(session)) {
            service.createTask(sub_id, name, desc, deadline, estimatedHours, labels_id);
            return "redirect:/tasks/showSub";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/updateTask")
    public String saveTask(@RequestParam("taskId") int taskId,
                           @RequestParam("taskName") String name,
                           @RequestParam("description") String desc,
                           @RequestParam("deadline") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadline,
                           @RequestParam("status") boolean status,
                           @RequestParam("estimatedHours") double estimatedHours,
                           @RequestParam("dedicatedHours") double dedicatedHours,
                           HttpSession session){

        System.out.println(">>> Received updateTask POST request <<<");

        if (isLoggedIn(session)) {
            service.editTask(taskId, name, desc, deadline, estimatedHours, dedicatedHours, status);
            return "redirect:/tasks/showSub";
        } else {
            return "redirect:/login";
        }
    }
}