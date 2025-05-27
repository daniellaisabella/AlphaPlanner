package org.example.alphaplanner.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.alphaplanner.service.AssigneesService;
import org.example.alphaplanner.service.LabelService;
import org.example.alphaplanner.service.TaskService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/tasks")
public class TaskController {


    private final LabelService labelService;
    private final TaskService taskService;
    private final AssigneesService assigneesService;

    public TaskController(LabelService labelService, TaskService taskService, AssigneesService assigneesService) {
        this.labelService = labelService;
        this.taskService = taskService;
        this.assigneesService = assigneesService;
    }

    //------------------------HELPER METHOD TO CHECK IF USER IS LOGGED IN------------------------------------
    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("userId") != null;
    }



    @GetMapping("/createtask")
    public String createTask(Model model, @RequestParam("subId") int subId, HttpSession session) {
        model.addAttribute("subId", subId);

        String labelsRaw = labelService.getAllLabels();
        List<String[]> labels = Arrays.stream(labelsRaw.split(","))
                .map(entry -> entry.split(":", 2)) // Ensure splitting only on the first colon
                .collect(Collectors.toList());

        model.addAttribute("labels", labels);
        return isLoggedIn(session) ? "createtask" : "redirect:/login";
    }



    @PostMapping("/savetask")
    public String saveTask(@RequestParam("subId") int subId,
                           String name,
                           String desc,
                           LocalDate deadline,
                           double estimatedHours,
                           @RequestParam(name = "labels_id", required = false) ArrayList<Integer> labels_id,
                           HttpSession session, Model model) {

        List<Integer> labelsResult = new ArrayList<>();

        if (isLoggedIn(session)) {
            if (labels_id != null) {
                labelsResult = labels_id;
            }
            taskService.createTask(subId, name, desc, deadline, estimatedHours, labelsResult);
            return "redirect:/subprojects/showsub?subId=" + subId   ;
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/updatetask")
    public String updateTask(@RequestParam("taskId") int taskId,
                           @RequestParam("taskName") String name,
                           @RequestParam("description") String desc,
                           @RequestParam("deadline") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadline,
                           @RequestParam("status") boolean status,
                           @RequestParam("estimatedHours") double estimatedHours,
                           @RequestParam("dedicatedHours") double dedicatedHours,
                           HttpServletRequest request,
                           HttpSession session) {



        if (isLoggedIn(session)) {
            taskService.editTask(taskId, name, desc, deadline, estimatedHours, dedicatedHours, status);
            return "redirect:" + request.getHeader("referer");
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/updatelabelsfromTask")
    public String saveLabelsFromTask(@RequestParam(name = "labels", required = false) ArrayList<Integer> labels,
                                     @RequestParam(name = "taskId") int taskId,  HttpServletRequest request,
                                     HttpSession session) {

        ArrayList<Integer> result = new ArrayList<>();
        if (isLoggedIn(session)) {
            if (labels != null) {
                result = labels;
            }
            labelService.addLabelsToTask(taskId, result);
            return "redirect:" + request.getHeader("referer");
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/managelabels")
    public String manageLabels(Model model, HttpSession session, @RequestParam(name = "subProjectId") int subProjectId) {
        if (isLoggedIn(session)) {
            String labelsRaw = labelService.getAllLabels(); // e.g. "1:Bug,2:UI,3:Urgent"

            List<String[]> labels = Arrays.stream(labelsRaw.split(","))
                    .map(entry -> entry.split(":", 2)) // Ensure splitting only on the first colon
                    .collect(Collectors.toList());

            model.addAttribute("labels", labels); // Now a List<String[]>
            model.addAttribute("subProject", subProjectId);
            return "manageLabels";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/deletelabel")
    public String deleteLabel(@RequestParam(name = "label_id") int label_id, HttpSession session) {

        if (isLoggedIn(session)) {
            labelService.deleteLabel(label_id);
            return "redirect:/tasks/managelabels";
        } else {
            return "redirect:/login";
        }

    }

    @PostMapping("/deletetask")
    public String deleteTask(@RequestParam(name = "task_id") int task_id, HttpSession session,  HttpServletRequest request) {

        if (isLoggedIn(session)) {
            taskService.deleteTask(task_id);
            return "redirect:" + request.getHeader("referer");
        } else {
            return "redirect:/login";
        }

    }

    @PostMapping("/createlabel")
    public String createLabel(@RequestParam(name = "labelName") String labelName,
                              Model model,
                              HttpSession session) {
        if (labelService.checkIfLabelNameExist(labelName)) {
            String labels = labelService.getAllLabels();
            model.addAttribute("labels", labels);
            model.addAttribute("nameExists", true);
            return "manageLabels";
        }
        if (isLoggedIn(session)) {
            labelService.createLabel(labelName);
            return "redirect:/tasks/managelabels";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/updateassigneesfromtask")
    public String updateAssigneesFromTask(@RequestParam(name = "assignees", required = false) ArrayList<Integer> assignees,
                                          @RequestParam(name = "taskId") int taskId,  HttpServletRequest request,
                                          HttpSession session){


        ArrayList<Integer> result = new ArrayList<>();
        if (isLoggedIn(session)) {
            if (assignees != null) {
                result = assignees;
            }
            assigneesService.addAssigneesToTask(taskId, result);
            return "redirect:" + request.getHeader("referer");
        } else {
            return "redirect:/login";
        }

    }
}
