package org.example.alphaplanner.controller;


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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("userId") != null;
    }

    // dummy method to get into the task of a specific subproject=============================================
    @GetMapping("/showSub")
    public String showSub(Model model) {
        SubProject subProject = service.getSubdummy(1);
        model.addAttribute("sub", subProject);
        List<Task> tasks = service.showAllTasksFromSub(1);
        model.addAttribute("tasks", tasks);
        List<String> labels = service.getLabelsInString(service.getAllLabels());
        model.addAttribute("labels", labels);
        return "subProject";
    }
//===========================================================


    @GetMapping("/createTask")
    public String createTask(Model model, int sub_id, HttpSession session) {
        SubProject subProject = service.getSubdummy(1);
        model.addAttribute("sub_id", sub_id);
        List<Label> labels = service.getAllLabels();
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
            if (labels_id != null){
                labelsResult = labels_id;
            }
            service.createTask(sub_id, name, desc, deadline, estimatedHours, labelsResult);
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
            service.editTask(taskId, name, desc, deadline, estimatedHours, dedicatedHours, status);
            return "redirect:/tasks/showSub";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/updateLabelsFromTask")
    public String saveLabelsFromTask(@RequestParam(name = "labels", required = false) ArrayList<String> labels,
                                     @RequestParam(name = "taskId") int taskId,
                                     HttpSession session) {
        System.out.println(">>> Received updateTask POST request <<<");
        ArrayList<String> result = new ArrayList<>();
        if (isLoggedIn(session)) {
            if (labels != null){
                result = labels;
            }
            service.addLabelsToTask(taskId, result);
            return "redirect:/tasks/showSub";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/manageLabels")
    public String manageLabels(Model model, HttpSession session){
        if (isLoggedIn(session)) {
            List<Label> labels = service.getAllLabels();
            model.addAttribute("labels", labels);
            return "manageLabels";
        } else {
            return "redirect:/login";
        }
    }

    @PostMapping("/deleteLabel")
    public String deleteLabel(@RequestParam(name = "label_id") int label_id, HttpSession session){

        if (isLoggedIn(session)) {
            service.deleteLabel(label_id);
            return "redirect:/tasks/manageLabels";
        } else {
            return "redirect:/login";
        }

    }

    @PostMapping("/deleteTask")
    public String deleteTask(@RequestParam(name = "task_id") int task_id, HttpSession session){

        if (isLoggedIn(session)) {
            service.deleteTask(task_id);
            return "redirect:/tasks/showSub";
        } else {
            return "redirect:/login";
        }

    }

    @PostMapping("/createLabel")
    public String createLabel(@RequestParam(name = "labelName") String labelName,
                              RedirectAttributes redirectAttributes,
                              HttpSession session){
        if(service.checkIfLabelNameExist(labelName)){
            redirectAttributes.addFlashAttribute("NameExists", true);
            return "redirect:/tasks/manageLabels";
        }
        if (isLoggedIn(session)) {
           service.createLabel(labelName);
            return "redirect:/tasks/manageLabels";
        } else {
            return "redirect:/login";
        }
    }
}
