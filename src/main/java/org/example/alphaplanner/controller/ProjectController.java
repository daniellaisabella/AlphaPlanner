package org.example.alphaplanner.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphaplanner.models.Project;
import org.example.alphaplanner.service.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final BaseService service;

    public ProjectController(BaseService service) {
        this.service = service;
    }

    @PostMapping("")
    private String projectPage(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:";
        int userID = (int) session.getAttribute("userId");
        boolean authority = false;
        List<Project> projects = service.getProjects(userID);
        if (service.getUserRole(userID).equals("project manager")) authority = true;
        model.addAttribute("freshProject", new Project());
        model.addAttribute("projects", projects);
        model.addAttribute("role", authority);
        return "pm-page";
    }

    @PostMapping("/edit")
    private String edit(HttpSession session, @ModelAttribute Project freshProject) {
        if (session.getAttribute("userId") == null) return "redirect:";
        service.updateProject(freshProject);
        return "redirect:/projects";
    }

    @PostMapping("/add")
    private String AddProject(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return "redirect:/login";
        int userId = (int) session.getAttribute("userId");

        return "new-project";
    }


}
