package org.example.alphaplanner.service;

import org.example.alphaplanner.models.User;
import org.example.alphaplanner.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseService {

    private UserRepository userRepository;

    public BaseService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //------------ USER METHODS-----------//

    public boolean login(User user) {
        return userRepository.login(user);
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
}
