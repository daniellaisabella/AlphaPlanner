package org.example.alphaplanner.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphaplanner.models.Project;
import org.example.alphaplanner.service.BaseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {

    private final BaseService service;

    public ProjectController(BaseService service) {
        this.service = service;
    }

    @GetMapping("")
    private String projectManagerPage(HttpSession session, Model model)
    {
       List<Project> projects = service.getProjects((Integer) session.getAttribute("userId"));
       for (Project p : projects)
       {
           System.out.println(p.getProjectName()+"help");
       }
        model.addAttribute("projects",projects);
        return "pm-page";
    }

    private String isLoggedIn(HttpSession session, String s){
        if(session.getAttribute("userId") !=null)return s;
        return "redirect:/login";
    }


}
