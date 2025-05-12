package org.example.alphaplanner.controller;

import jakarta.servlet.http.HttpSession;
import org.example.alphaplanner.models.User;
import org.example.alphaplanner.service.BaseService;
import org.example.alphaplanner.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("")
@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
        if (userService.login(user)){
            session.setAttribute("userId",userService.getUserId(user));
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
    public String profile(HttpSession session){
        if (userService.getUserRole(session.getAttribute("userId")).equals("admin")){
            return isLoggedIn(session) ? "redirect:/admin1" : "redirect:/logout";
        } else {
            return isLoggedIn(session) ? "redirect:/projects" : "redirect:/logout";
        }
    }

    @GetMapping("/admin1")
    public String admin1 (Model model, HttpSession session){
        model.addAttribute("activePage","admin1");
        model.addAttribute("projectManagers",service.getUsersByRole("project manager"));
        return service.getUserRole(session.getAttribute("userId")).equals("admin")
                && isLoggedIn(session) ? "adminProjectManager": "redirect:/logout";
        model.addAttribute("projectManagers",userService.getUsersByRole("project manager"));
        return isLoggedIn(session) ? "adminProjectManager": "redirect:/logout";
    }

    @GetMapping("/admin2")
    public String admin2 (Model model, HttpSession session){
        model.addAttribute("activePage","admin2");
        model.addAttribute("employees",service.getUsersByRole("employee"));
        return service.getUserRole(session.getAttribute("userId")).equals("admin")
                && isLoggedIn(session) ? "adminEmployees": "redirect:/logout";
        model.addAttribute("employees",userService.getUsersByRole("employee"));
        return isLoggedIn(session) ? "adminEmployees": "redirect:/logout";
    }

    @GetMapping("/myProfile")
    public String myProfile (Model model, HttpSession session){
        int userID = (int) session.getAttribute("userId");
        boolean aut = service.getUserRole(userID).equals("admin");
        model.addAttribute("role", aut);
        model.addAttribute("user",service.getUserById((Integer) session.getAttribute("userId")));
        model.addAttribute("user",userService.getUserById((Integer) session.getAttribute("userId")));
        return isLoggedIn(session) ? "myProfile": "redirect:/logout";
    }

    @GetMapping("/create")
    public String createUser(Model model, HttpSession session){
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.getRoles());
        model.addAttribute("enumSkills", userService.getSkills());

        return isLoggedIn(session) ? "createUser": "redirect:/logout";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user, Model model){
        if(userService.checkForDup(user.getEmail())){
            model.addAttribute("duplicate", true);
            return "/createUser";
        }else{
            userService.saveUser(user);
            return user.getRole().equals("employee") ? "redirect:/admin2" : "redirect:/admin1";
        }
    }

    @GetMapping("/edit/{userId}")
    public String editUser(@PathVariable int userId, HttpSession session, Model model){
        User user = userService.getUserById(userId);
        model.addAttribute("user",user);
        model.addAttribute("roles", userService.getRoles());
        model.addAttribute("enumSkills", userService.getSkills());
        model.addAttribute("userEmail", user.getEmail().replace("@alpha.com", ""));

        return isLoggedIn(session) ? "editUser": "redirect:/logout";
    }

    @PostMapping("/update")
    public String updateUser(@RequestParam String emailPrefix, @ModelAttribute User user){
        user.setEmail(emailPrefix + "@alpha.com");
        userService.updateUser(user);
        return user.getRole().equals("employee") ? "redirect:/admin2" : "redirect:/admin1";
    }

    @PostMapping("/delete/{userId}")
    public String deleteUser(@PathVariable int userId){
        boolean userRole = userService.getUserRole(userId).equals("employee");
        userService.deleteUser(userId);
        return userRole ? "redirect:/admin2" : "redirect:/admin1";
    }












}

