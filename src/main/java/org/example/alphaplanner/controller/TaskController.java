package org.example.alphaplanner.controller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/taks")
public class TaskController {

// dummy method to get into the task of a specific subproject
    @GetMapping("/showSub")
    public String showSub(int sub_id, Model model){
        model.addAttribute()
        return
    }
//===========================================================
}
