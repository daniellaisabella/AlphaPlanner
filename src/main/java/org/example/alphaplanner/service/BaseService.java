package org.example.alphaplanner.service;


import org.example.alphaplanner.models.Task;
import org.example.alphaplanner.repository.TaskRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BaseService {

    private final TaskRepository taskRepository = new TaskRepository(new JdbcTemplate());




//----------------------Tasks and labels----------------------------------------------

public List<Task> showAllTasksFromSub(int sub_id){
    return taskRepository.showAllTasksFromSub(sub_id);
}

//------------------------------------------------------------------------------------
}
