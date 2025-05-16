package org.example.alphaplanner.service;

import org.example.alphaplanner.models.UserDto;
import org.example.alphaplanner.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AssigneesService {

    private final TaskRepository taskRepository;

    private AssigneesService(TaskRepository taskRepository)
    {
        this.taskRepository = taskRepository;
    }

    public List<UserDto> getAssigneesFromTask(int task_id){
        return taskRepository.getAssigneesFromTask(task_id);
    }

    public String getAssigneesInString(List<UserDto> users) {
        return taskRepository.getAssigneesInString(users);
    }

    public void addAssigneesToTask(int task_id, int user_id){
        taskRepository.addAssigneesToTask(task_id, user_id);
    }

    public void removeAssigneesFromTask(int task_id, int user_id){
        taskRepository.removeAssigneesFromTask(task_id, user_id);
    }


}
