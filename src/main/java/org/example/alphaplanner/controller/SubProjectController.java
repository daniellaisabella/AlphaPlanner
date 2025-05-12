package org.example.alphaplanner.controller;

import jakarta.servlet.http.HttpServletRequest;
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
    private String edit(HttpServletRequest request, HttpSession session, @ModelAttribute SubProject freshSubProject) {
        if (isloggedIn(session)) return "redirect:";
        service.updateSubProject(freshSubProject);
        String referer = request.getHeader("referer");
        return "redirect:" + referer;
    }

    @PostMapping("/add")
    private String AddSubProject(HttpServletRequest request, HttpSession session, @ModelAttribute SubProject freshSubProject) {
        if (isloggedIn(session)) return "redirect:";
        System.out.println(freshSubProject.getProjectId());
        service.newSubProject(freshSubProject);
        String referer = request.getHeader("referer");
        return "redirect:" + referer;

    }

    @GetMapping("/delete")
    private String deleteSubProject(HttpServletRequest request, HttpSession session, @RequestParam int subId){
        if (isloggedIn(session)) return "redirect:";
        service.deleteSubProject(subId);
        String referer = request.getHeader("referer");
        return "redirect:" + referer;

    }

    @GetMapping("/showSub")
    public String showSub(HttpSession session, Model model, @RequestParam int subId) {
        if (isloggedIn(session)) return "redirect:";
        SubProject subProject = service.getSubProject(subId);
        model.addAttribute("sub", subProject);
        List<Task> tasks = service.showAllTasksFromSub(subId);
        model.addAttribute("tasks", tasks);
        String labels = service.getLabelsInString(service.getAllLabels());
        model.addAttribute("labels", labels);
        return "subProject";
    }

}