package org.example.alphaplanner.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.example.alphaplanner.models.Dto.UserToProjectDto;
import org.example.alphaplanner.models.Project;
import org.example.alphaplanner.models.SubProject;
import org.example.alphaplanner.models.User;
import org.example.alphaplanner.service.AuthorizationService;
import org.example.alphaplanner.service.ProjectService;
import org.example.alphaplanner.service.SubProjectService;
import org.example.alphaplanner.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {


    private final UserService userService;
    private final ProjectService projectService;
    private final SubProjectService subProjectService;
    private final AuthorizationService authorizationService;

    public ProjectController(UserService userService, ProjectService projectService, SubProjectService subProjectService, AuthorizationService authorizationService) {
        this.userService = userService;
        this.projectService = projectService;
        this.subProjectService = subProjectService;
        this.authorizationService = authorizationService;
    }

    private boolean isNotloggedIn(HttpSession session) {
        return (session.getAttribute("userId") == null);
    }

    @GetMapping("")
    private String usersProjectsPage(HttpSession session, Model model) {
        if (isNotloggedIn(session)) return "redirect:";
        int userID = (int) session.getAttribute("userId");
        boolean authority = userService.getUserRole(userID).equals("project manager");
        List<Project> projects = projectService.getProjects(userID);
        model.addAttribute("freshProject", new Project());
        model.addAttribute("projects", projects);
        model.addAttribute("role", authority);
        return !userService.getUserRole(session.getAttribute("userId")).equals("admin") ? "projects" : "redirect:/admin1";
    }

    @PostMapping("/edit")
    private String edit(HttpSession session, @ModelAttribute Project freshProject) {
        if (isNotloggedIn(session)) return "redirect:";
        projectService.updateProject(freshProject);
        return "redirect:/projects";
    }

    @PostMapping("/add")
    private String AddProject(HttpSession session, @ModelAttribute Project freshProject) {
        if (isNotloggedIn(session)) return "redirect:";
        int userId = (int) session.getAttribute("userId");
        freshProject.setPm_id(userId);
        projectService.newProject(freshProject);
        return "redirect:/projects";
    }

    @PostMapping("/delete")
    private String deleteProject(HttpSession session, @RequestParam("projectId") int projectId) {
        int userId = (int) session.getAttribute("userId");
        if (!authorizationService.authProjectManager(userId, projectId)) return "redirect:";
        projectService.deleteProject(projectId);
        return "redirect:/projects";

    }

    //project-overview meaning subprojects
    @GetMapping("/projectoverview")
    private String projectOverview(HttpSession session, Model model, @RequestParam int projectId) {

        if (isNotloggedIn(session)) return "redirect:";
        session.setAttribute("projectId", projectId);
        boolean authority = userService.getUserRole(session.getAttribute("userId")).equals("project manager");
        Project parentProject = projectService.getProject(projectId);
        SubProject freshSubProject = new SubProject();
        freshSubProject.setProjectId(projectId);
        List<SubProject> subProjects = subProjectService.getSubProjects(projectId);
        model.addAttribute("role", authority);
        model.addAttribute("projectName", parentProject.getProjectName());
        model.addAttribute("freshSubProject", freshSubProject);
        model.addAttribute("subProjects", subProjects);
        model.addAttribute("projectId", projectId);

        return "project";
    }


    @GetMapping("/projectassignees")
    private String projectAssignees(HttpSession session, Model model, @RequestParam int projectId) {
        if (isNotloggedIn(session)) return "redirect:";
        session.setAttribute("project", projectId);
        Project parentProject = projectService.getProject(projectId);
        List<User> assignedUsers = projectService.getUsersByProjectId(projectId);
        List<User> availableUsers = userService.getEmployeesNotAssignedToProject(projectId);
        boolean isEmpty = availableUsers.isEmpty();
        boolean authority = userService.getUserRole(session.getAttribute("userId")).equals("project manager");
        model.addAttribute("isEmpty", isEmpty);
        model.addAttribute("projectId", projectId);
        model.addAttribute("projectName", parentProject.getProjectName());
        model.addAttribute("role", authority);
        model.addAttribute("availableUsers", availableUsers);
        for (User u : availableUsers)
        {
            System.out.println(u.getName());
        }
        model.addAttribute("assignedUsers", assignedUsers);
        return "project-assignees";
    }

    @PostMapping("assignemployee")
    private String assignEmployee(HttpSession session, HttpServletRequest request, @RequestParam("employeeId") int employeeId) {
        int projectId = (int) session.getAttribute("projectId");
        String referer = request.getHeader("referer");
        UserToProjectDto newJunction = new UserToProjectDto(employeeId,projectId);
        if (!authorizationService.authProjectManager((Integer) session.getAttribute("userId"), newJunction.getProjectId()))
            return "redirect:";

        List<User> assignedUsers = projectService.getUsersByProjectId(projectId);

        for (User user : assignedUsers) {
            if (user.getUserId() == newJunction.getUserId())
                return "redirect:" + referer;
        }
        projectService.assignUserToProject(newJunction);

        return "redirect:" + referer;
    }

    @PostMapping("unassignemployee")
    private String unAssignEmployee(HttpServletRequest request, HttpSession session, @RequestParam("employeeId") int employeeId){
        UserToProjectDto newJunction = new UserToProjectDto(employeeId, (Integer) session.getAttribute("projectId"));
        if (!authorizationService.authProjectManager((Integer) session.getAttribute("userId"), newJunction.getProjectId()))
            return "redirect:";

        projectService.unassignUserFromProject(newJunction);

        String referer = request.getHeader("referer");
        return "redirect:" + referer;
    }


}
