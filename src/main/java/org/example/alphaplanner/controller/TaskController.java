package org.example.alphaplanner.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.alphaplanner.models.Label;
import org.example.alphaplanner.models.SubProject;
import org.example.alphaplanner.service.AssigneesService;
import org.example.alphaplanner.service.LabelService;
import org.example.alphaplanner.service.SubProjectService;
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
import java.util.List;

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



    @GetMapping("/createTask")
    public String createTask(Model model, @RequestParam("subId") int subId, HttpSession session) {
        model.addAttribute("subId", subId);
        List<Label> labels = labelService.getAllLabels();
        model.addAttribute("labels", labels);
        return isLoggedIn(session) ? "createTask" : "redirect:/login";
    }

    @PostMapping("/saveTask")
    public String saveTask(@RequestParam("subId") int subId,
                           String name,
                           String desc,
                           LocalDate deadline,
                           double estimatedHours,
                           @RequestParam(name = "labels_id", required = false) ArrayList<Integer> labels_id,
                           HttpSession session, Model model) {

        System.out.println(subId);
        List<Integer> labelsResult = new ArrayList<>();

        if (isLoggedIn(session)) {
            if (labels_id != null) {
                labelsResult = labels_id;
            }
            taskService.createTask(subId, name, desc, deadline, estimatedHours, labelsResult);
            return "redirect:/subprojects/showSub?subId=" + subId   ;
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/updateTask")
    public String updateTask(@RequestParam("taskId") int taskId,
                           @RequestParam("taskName") String name,
                           @RequestParam("description") String desc,
                           @RequestParam("deadline") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate deadline,
                           @RequestParam("status") boolean status,
                           @RequestParam("estimatedHours") double estimatedHours,
                           @RequestParam("dedicatedHours") double dedicatedHours,
                           HttpServletRequest request,
                           HttpSession session) {

        System.out.println(">>> Received updateTask POST request <<<");
        System.out.println("estimated: " + estimatedHours);
        System.out.println("dedicated " + dedicatedHours);

        if (isLoggedIn(session)) {
            taskService.editTask(taskId, name, desc, deadline, estimatedHours, dedicatedHours, status);
            return "redirect:" + request.getHeader("referer");
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/updateLabelsFromTask")
    public String saveLabelsFromTask(@RequestParam(name = "labels", required = false) ArrayList<Integer> labels,
                                     @RequestParam(name = "taskId") int taskId,  HttpServletRequest request,
                                     HttpSession session) {
        System.out.println(">>> Received updateTask POST request <<<");
        System.out.println("Received label IDs: " + labels);
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

    @GetMapping("/manageLabels")
    public String manageLabels(Model model, HttpSession session) {
        if (isLoggedIn(session)) {
            List<Label> labels = labelService.getAllLabels();
            model.addAttribute("labels", labels);
            return "manageLabels";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/deleteLabel")
    public String deleteLabel(@RequestParam(name = "label_id") int label_id, HttpSession session) {

        if (isLoggedIn(session)) {
            labelService.deleteLabel(label_id);
            return "redirect:/tasks/manageLabels";
        } else {
            return "redirect:/login";
        }

    }

    @PostMapping("/deleteTask")
    public String deleteTask(@RequestParam(name = "task_id") int task_id, HttpSession session,  HttpServletRequest request) {

        if (isLoggedIn(session)) {
            taskService.deleteTask(task_id);
            return "redirect:" + request.getHeader("referer");
        } else {
            return "redirect:/login";
        }

    }

    @PostMapping("/createLabel")
    public String createLabel(@RequestParam(name = "labelName") String labelName,
                              Model model,
                              HttpSession session) {
        if (labelService.checkIfLabelNameExist(labelName)) {
            List<Label> labels = labelService.getAllLabels();
            model.addAttribute("labels", labels);
            model.addAttribute("nameExists", true);
            return "manageLabels";
        }
        if (isLoggedIn(session)) {
            labelService.createLabel(labelName);
            return "redirect:/tasks/manageLabels";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/updateAssigneesFromTask")
    public String updateAssigneesFromTask(@RequestParam(name = "assignees", required = false) ArrayList<Integer> assignees,
                                          @RequestParam(name = "taskId") int taskId,  HttpServletRequest request,
                                          HttpSession session){

        System.out.println(">>> Received updateTask POST request <<<");
        System.out.println("Received label IDs: " + assignees);
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
