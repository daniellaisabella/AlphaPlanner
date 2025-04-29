package org.example.alphaplanner.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphaplanner.service.BaseService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProjectController {

    private final BaseService service;

    public ProjectController(BaseService service) {
        this.service = service;
    }

    private boolean isLoggenIn(HttpSession session){
        return session.getAttribute("user") !=null;
    }

    @GetMapping("/pm1")
    private String projectManagerPage()
    {
        return "pm-page";
    }



}
