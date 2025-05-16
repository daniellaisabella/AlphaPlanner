package org.example.alphaplanner.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.alphaplanner.models.SubProject;
import org.example.alphaplanner.models.Project;
import org.example.alphaplanner.models.Task;
import org.example.alphaplanner.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/subprojects")
public class SubProjectController {

    private final LabelService labelService;
    private final TaskService taskService;
    private final SubProjectService subProjectService;
    private final UserService userService;

    public SubProjectController(LabelService labelService, TaskService taskService, SubProjectService subProjectService, UserService userService) {
        this.labelService = labelService;
        this.taskService = taskService;
        this.subProjectService = subProjectService;
        this.userService = userService;
    }

    private boolean isloggedIn(HttpSession session) {
        return (session.getAttribute("userId") == null);
    }

    @GetMapping("/showsub")
    public String showSub(HttpSession session, Model model, @RequestParam int subId) {
        if (isloggedIn(session)) return "redirect:";
        SubProject subProject = subProjectService.getSubProject(subId);
        model.addAttribute("sub", subProject);
        List<Task> tasks = taskService.showAllTasksFromSub(subId);
        model.addAttribute("tasks", tasks);
        String labels = labelService.getLabelsInString(labelService.getAllLabels());
        model.addAttribute("labels", labels);
        return "subProject";
    }
    @PostMapping("/edit")
    private String edit(HttpServletRequest request, HttpSession session, @ModelAttribute SubProject freshSubProject) {
        if (isloggedIn(session)) return "redirect:";
        freshSubProject.setProjectId((Integer) session.getAttribute("projectId"));
        subProjectService.updateSubProject(freshSubProject);

        String referer = request.getHeader("referer");
        return "redirect:" + referer;
    }

    @PostMapping("/add")
    private String AddSubProject(HttpServletRequest request, HttpSession session, @ModelAttribute SubProject freshSubProject) {
        if (isloggedIn(session)) return "redirect:";
        System.out.println(freshSubProject.getProjectId());
        subProjectService.newSubProject(freshSubProject);
        String referer = request.getHeader("referer");
        return "redirect:" + referer;

    }

    @PostMapping("/delete")
    private String deleteSubProject(HttpServletRequest request, HttpSession session, @RequestParam int subId) {
        if (isloggedIn(session)) return "redirect:";
        subProjectService.deleteSubProject(subId);
        String referer = request.getHeader("referer");
        return "redirect:" + referer;

    }



}