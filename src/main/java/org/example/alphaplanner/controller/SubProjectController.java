package org.example.alphaplanner.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphaplanner.models.SubProject;
import org.example.alphaplanner.models.Project;
import org.example.alphaplanner.models.Task;
import org.example.alphaplanner.service.BaseService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/SubProjects")
public class SubProjectController {

    private final BaseService service;

    public SubProjectController(BaseService service) {
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
        List<SubProject> subProjects = service.getSubProjects(userID);
        model.addAttribute("freshSubProject", new SubProject());
        model.addAttribute("projects", subProjects);
        model.addAttribute("role", authority);
        return "subProject";
    }

    @PostMapping("/edit")
    private String edit(HttpSession session, @ModelAttribute SubProject freshSubProject) {
        if (isloggedIn(session)) return "redirect:";
        service.updateSubProject(freshSubProject);
        return "redirect:/SubProjects";
    }

    @PostMapping("/add")
    private String AddSubProject(HttpSession session, @ModelAttribute SubProject freshSubProject, @ModelAttribute int projectId) {
        if (isloggedIn(session)) return "redirect:";
        freshSubProject.setProjectId(projectId);
        service.newSubProject(freshSubProject);
        return "redirect:/projects";
    }

    @GetMapping("/delete")
    private String deleteSubProject(HttpSession session, @RequestParam int id){
        if (isloggedIn(session)) return "redirect:";
        service.deleteSubProject(id);
        return "redirect:/projects";

    }

}