package org.example.alphaplanner.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphaplanner.service.BaseService;
import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    private BaseService service;

    public UserController(BaseService service) {
        this.service = service;
    }

    private boolean isLoggenIn(HttpSession session){
        return session.getAttribute("user") !=null;
    }
}
