package org.example.alphaplanner.service;

import org.example.alphaplanner.models.Dto.UserToProjectDto;
import org.example.alphaplanner.models.User;
import org.example.alphaplanner.repository.ProjectRepository;
import org.example.alphaplanner.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class UserService {

    private final ProjectRepository projectRepository;
    public UserRepository userRepository;

    public UserService(UserRepository userRepository, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    public boolean login(User user) {
        return userRepository.login(user);
    }

    public User getUserById(int userId) {
        return userRepository.getUserById(userId);
    }

    public int getUserId(User user) {
        return userRepository.getUserId(user);
    }

    public String getUserRole(Object userId) {
        return userRepository.getUserRole(userId);
    }

    public List<User> getUsersByRole(String role) {
        return userRepository.getUsersByRole(role);
    }


    public boolean checkForDup(String email) {
        return userRepository.checkForDup(email);
    }

    public void saveUser(User user) {
        userRepository.saveUser(user);
    }

    public List<String> getRoles() {
        return userRepository.getRoles();
    }

    public List<String> getSkills() {
        return userRepository.getSkills();
    }

    public void deleteUser(int userId) {
        userRepository.deleteUser(userId);
    }

    public void updateUser(User user) {
        userRepository.updateUser(user);
    }

    public List<User> getEmployeesNotAssignedToProject(List<User> usersAssignedToProject)
        {
        List<User> availableUsers = userRepository.getUsersByRole("employee");
        List<User> finalUser = new ArrayList<>();
            System.out.println(availableUsers);
        for (User u : availableUsers) {
            for (User u2 : usersAssignedToProject) {
                if (u2.getUserId() == u.getUserId()) {
                    availableUsers.remove(u);
                    break;
                }
            }
        }


        return availableUsers;
    }

}
