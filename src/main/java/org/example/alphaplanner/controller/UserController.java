package org.example.alphaplanner.controller;
import jakarta.servlet.http.HttpSession;
import org.example.alphaplanner.models.User;
import org.example.alphaplanner.service.BaseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("")
@Controller
public class UserController {
    private BaseService service;

    public UserController(BaseService service) {
        this.service = service;
    }

    private boolean isLoggedIn(HttpSession session){
        return session.getAttribute("userId") !=null;
    }


    @GetMapping("/")
    public String showLogin(Model model){
        model.addAttribute("userLogin",new User());
        return "login";
    }

    @PostMapping("/")
    public String login(@ModelAttribute("userLogin") User user, Model model, HttpSession session){
        if (service.login(user)){
            session.setAttribute("userId",service.getUserId(user));
            session.setMaxInactiveInterval(1800);
            return "redirect:/profile";
        } else {
            model.addAttribute("wrongInput",true);
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        //Invalidates session
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/profile")
    public String profile(Model model, HttpSession session){
        if (service.getUserRole(session.getAttribute("userId")).equals("admin")){
            return "redirect:/admin1";
        }
        return isLoggedIn(session) ? "adminProfile" : "redirect:/logout";
    }

    @GetMapping("/admin1")
    public String admin1 (Model model, HttpSession session){
        model.addAttribute("projectManagers",service.getUsersByRole("project manager"));

        return isLoggedIn(session) ? "adminProjectManager": "redirect:/logout";
    }

    @GetMapping("/admin2")
    public String admin2 (Model model, HttpSession session){
        model.addAttribute("employees",service.getUsersByRole("employee"));
        return isLoggedIn(session) ? "adminEmployees": "redirect:/logout";
    }

    @GetMapping("/create")
    public String createUser(Model model, HttpSession session){
        model.addAttribute("user", new User());
        model.addAttribute("roles", service.getRoles());
        model.addAttribute("skills", service.getSkills());

        return isLoggedIn(session) ? "createUser": "redirect:/logout";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user, Model model){
        if(service.checkForDup(user.getEmail())){
            model.addAttribute("duplicate", true);
            return "redirect:/create";
        }else{
            service.saveUser(user);
            return user.getRole().equals("employee") ? "redirect:/admin2" : "redirect:/admin1";
        }
    }

    @GetMapping("/edit/{userId}")
    public String editUser(@PathVariable int userId, HttpSession session, Model model){
        User user = service.getUserById(userId);
        model.addAttribute("user",user);
        model.addAttribute("roles", service.getRoles());
        model.addAttribute("skills", service.getSkills());
        model.addAttribute("userEmail", user.getEmail().replace("@alpha.com", ""));

        return isLoggedIn(session) ? "editUser": "redirect:/logout";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam String emailPrefix, @ModelAttribute User user){
        System.out.println("Updating user: " + user);
        user.setEmail(emailPrefix + "@alpha.com");
        service.updateUser(user);
        return user.getRole().equals("employee") ? "redirect:/admin2" : "redirect:/admin1";
    }

    @PostMapping("/delete/{userId}")
    public String deleteUser(@PathVariable int userId){
        boolean userRole = service.getUserRole(userId).equals("employee");
        service.deleteUser(userId);
        return userRole ? "redirect:/admin2" : "redirect:/admin1";
    }












}

