package org.example.alphaplanner.service;

import org.example.alphaplanner.models.*;
import org.example.alphaplanner.repository.ProjectRepository;
import org.example.alphaplanner.repository.SubProjectRepository;
import org.example.alphaplanner.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.example.alphaplanner.repository.TaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
public class BaseService {

    private final ProjectRepository projectRepository;
    private UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final SubProjectRepository subProjectRepository;

    public BaseService(UserRepository userRepository, TaskRepository taskRepository, ProjectRepository projectRepository, SubProjectRepository subProjectRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.subProjectRepository = subProjectRepository;
    }

    //------------ USER METHODS-----------//



//------------------------------------------------------------------------------------


//-------------------------------------Assignees-----------------------------------------------



//----------------------------------Projects--------------------------------------------------


//----------------------------------Projects--------------------------------------------------


}
