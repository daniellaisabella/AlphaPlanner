package org.example.alphaplanner.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphaplanner.models.User;
import org.example.alphaplanner.service.BaseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class TaskController {
    private BaseService service;

    public TaskController(BaseService service) {
        this.service = service;
    }

    private boolean isLoggenIn(HttpSession session){
        return session.getAttribute("user") !=null;
    }



}
