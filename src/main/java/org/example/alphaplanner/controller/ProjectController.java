package org.example.alphaplanner.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphaplanner.models.Project;
import org.example.alphaplanner.models.SubProject;
import org.example.alphaplanner.models.Task;
import org.example.alphaplanner.service.BaseService;
import org.example.alphaplanner.service.ProjectService;
import org.example.alphaplanner.service.SubProjectService;
import org.example.alphaplanner.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {


    private final UserService userService;
    private final ProjectService projectService;
    private final SubProjectService subProjectService;

    public ProjectController(UserService userService, ProjectService projectService, SubProjectService subProjectService) {
        this.userService = userService;
        this.projectService = projectService;
        this.subProjectService = subProjectService;
    }
    private boolean isloggedIn(HttpSession session)
    {
        return (session.getAttribute("userId") == null);
    }

    @GetMapping("")
    private String projectPage(HttpSession session, Model model) {
        if (isloggedIn(session)) return "redirect:";
        int userID = (int) session.getAttribute("userId");
        boolean authority = userService.getUserRole(userID).equals("project manager");
        List<Project> projects = projectService.getProjects(userID);
        model.addAttribute("freshProject", new Project());
        model.addAttribute("projects", projects);
        model.addAttribute("role", authority);
        return "pm-page";
    }

    @PostMapping("/edit")
    private String edit(HttpSession session, @ModelAttribute Project freshProject) {
        if (isloggedIn(session)) return "redirect:";
        projectService.updateProject(freshProject);
        return "redirect:/projects";
    }

    @PostMapping("/add")
    private String AddProject(HttpSession session, @ModelAttribute Project freshProject) {
        if (isloggedIn(session)) return "redirect:";
        int userId = (int) session.getAttribute("userId");
        freshProject.setPm_id(userId);
        projectService.newProject(freshProject);
        return "redirect:/projects";
    }

    @GetMapping("/delete")
    private String deleteProject(HttpSession session, @RequestParam int id){
        if (isloggedIn(session)) return "redirect:";
        int userId = (int) session.getAttribute("userId");
        projectService.deleteProject(id);
        return "redirect:/projects";

    }

    @GetMapping("/projectOverview")
    private String projectOverview(HttpSession session, Model model, @RequestParam int id)
    {
        if (isloggedIn(session)) return "redirect:";

        boolean authority = userService.getUserRole(session.getAttribute("userId")).equals("project manager");
        Project parentProject = projectService.getProject(id);
        SubProject freshSubProject = new SubProject();
        freshSubProject.setProjectId(id);
        List<SubProject> subProjects = subProjectService.getSubProjects(id);
        model.addAttribute("role", authority);
        model.addAttribute("projectName", parentProject.getProjectName());
        model.addAttribute("freshSubProject", freshSubProject);
        model.addAttribute("subProjects", subProjects);
        return "project-overview";
    }


}
