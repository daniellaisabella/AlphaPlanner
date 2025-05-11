package org.example.alphaplanner.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphaplanner.models.Project;
import org.example.alphaplanner.models.SubProject;
import org.example.alphaplanner.models.Task;
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
    private boolean isloggedIn(HttpSession session)
    {
        return (session.getAttribute("userId") == null);
    }

    @GetMapping("")
    private String projectPage(HttpSession session, Model model) {
        if (isloggedIn(session)) return "redirect:";
        int userID = (int) session.getAttribute("userId");
        boolean authority = service.getUserRole(userID).equals("project manager");
        List<Project> projects = service.getProjects(userID);
        model.addAttribute("freshProject", new Project());
        model.addAttribute("projects", projects);
        model.addAttribute("role", authority);
        return "pm-page";
    }

    @PostMapping("/edit")
    private String edit(HttpSession session, @ModelAttribute Project freshProject) {
        if (isloggedIn(session)) return "redirect:";
        service.updateProject(freshProject);
        return "redirect:/projects";
    }

    @PostMapping("/add")
    private String AddProject(HttpSession session, @ModelAttribute Project freshProject) {
        if (isloggedIn(session)) return "redirect:";
        int userId = (int) session.getAttribute("userId");
        freshProject.setPm_id(userId);
        service.newProject(freshProject);
        return "redirect:/projects";
    }

    @GetMapping("/delete")
    private String deleteProject(HttpSession session, @RequestParam int id){
        if (isloggedIn(session)) return "redirect:";
        int userId = (int) session.getAttribute("userId");
        service.deleteProject(userId, id);
        return "redirect:/projects";

    }

    @GetMapping("/projectOverview")
    private String projectOverview(HttpSession session, Model model, @RequestParam int id)
    {
        if (isloggedIn(session)) return "redirect:";

        boolean authority = service.getUserRole(session.getAttribute("userId")).equals("project manager");
        Project parentProject = service.getProject(id);
        List<SubProject> subProjects = service.getSubProjects(id);
        model.addAttribute("role", authority);
        model.addAttribute("projectName", parentProject.getProjectName());
        model.addAttribute("freshSubProject", new SubProject());
        model.addAttribute("subProjects", subProjects);
        return "project-overview";
    }


}
