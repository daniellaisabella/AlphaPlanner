package org.example.alphaplanner.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphaplanner.service.BaseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class ProjectController {

    private final BaseService service;

    public ProjectController(BaseService service) {
        this.service = service;
    }

    @GetMapping("/pm1")
    private String projectManagerPage(HttpSession session, Model model)
    {
        model.addAttribute(service.getProjects(session.getAttribute("userid")));
        return isLoggedIn(session,"pm-Page");
    }

    private String isLoggedIn(HttpSession session, String s){
        if(session.getAttribute("userId") !=null)return s;
        return "redirect:/login";
    }


}
