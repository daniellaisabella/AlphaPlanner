package org.example.alphaplanner.service;

import org.example.alphaplanner.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {


    private final TaskRepository taskRepository;

    public LabelService(TaskRepository taskRepository)
    {
        this.taskRepository = taskRepository;
    }


    public void addLabelsToTask(int task_id, List<Integer> labels){
        taskRepository.addLabelsToTask(task_id, labels);
    }



    public void createLabel(String name){
        taskRepository.createLabel(name);
    }

    public void deleteLabel(int label_id){
        taskRepository.deleteLabel(label_id);
    }

    public boolean checkIfLabelNameExist(String name){
        return taskRepository.checkIfLabelNameExist(name);
    }

    public String getAllLabels(){
        return taskRepository.getAllLabels();
    }


public int StringToInt(String name){
        return Integer.parseInt(name);
}

}
