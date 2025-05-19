package org.example.alphaplanner.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class AlphaPlannerExceptionHandler {



  
    @ExceptionHandler(Exception.class)
    public String handleIllegalArgument(Exception e, Model model) {

        model.addAttribute("errorMessage", e.getMessage());
        return "error";
    }
}
