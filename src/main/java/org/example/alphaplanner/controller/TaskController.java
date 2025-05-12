package org.example.alphaplanner.controller;


import jakarta.servlet.http.HttpSession;
import org.example.alphaplanner.models.Label;
import org.example.alphaplanner.models.SubProject;
import org.example.alphaplanner.models.Task;
import org.example.alphaplanner.service.BaseService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {


    private final LabelService labelService;
    private final TaskService taskService;
    private final SubProjectService subProjectService;

    public TaskController(LabelService labelService, TaskService taskService, SubProjectService subProjectService) {
        this.labelService = labelService;
        this.taskService = taskService;
        this.subProjectService = subProjectService;
    }

    //------------------------HELPER METHOD TO CHECK IF USER IS LOGGED IN------------------------------------
    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("userId") != null;
    }

    // dummy method to get into the task of a specific subproject=============================================
//===========================================================


    @GetMapping("/createTask")
    public String createTask(Model model, int sub_id, HttpSession session) {
        SubProject subProject = subProjectService.getSubProject(sub_id);
        model.addAttribute("sub_id", sub_id);
        List<Label> labels = labelService.getAllLabels();
        model.addAttribute("labels", labels);
        return isLoggedIn(session) ? "createTask" : "redirect:/login";
    }

    @PostMapping("/saveTask")
    public String saveTask(int sub_id,
                           String name,
                           String desc,
                           LocalDate deadline,
                           double estimatedHours,
                           @RequestParam(name = "labels_id", required = false) ArrayList<Integer> labels_id, HttpSession session) {

        List<Integer> labelsResult = new ArrayList<>();


        if (isLoggedIn(session)) {
            if (labels_id != null) {
                labelsResult = labels_id;
            }
            taskService.createTask(sub_id, name, desc, deadline, estimatedHours, labelsResult);
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
                           HttpSession session) {

        System.out.println(">>> Received updateTask POST request <<<");

        if (isLoggedIn(session)) {
            taskService.editTask(taskId, name, desc, deadline, estimatedHours, dedicatedHours, status);
            return "redirect:/tasks/showSub";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/updateLabelsFromTask")
    public String saveLabelsFromTask(@RequestParam(name = "labels", required = false) ArrayList<Integer> labels,
                                     @RequestParam(name = "taskId") int taskId,
                                     HttpSession session) {
        System.out.println(">>> Received updateTask POST request <<<");
        System.out.println("Received label IDs: " + labels);
        ArrayList<Integer> result = new ArrayList<>();
        if (isLoggedIn(session)) {
            if (labels != null) {
                result = labels;
            }
            labelService.addLabelsToTask(taskId, result);
            return "redirect:/tasks/showSub";
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
    public String deleteTask(@RequestParam(name = "task_id") int task_id, HttpSession session) {

        if (isLoggedIn(session)) {
            taskService.deleteTask(task_id);
            return "redirect:/tasks/showSub";
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
}
