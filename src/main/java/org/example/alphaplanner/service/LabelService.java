package org.example.alphaplanner.service;

import org.example.alphaplanner.models.Label;
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

    public void removeLabelFromTask(int task_id, int label_id){
        taskRepository.removeLabelFromTask(task_id, label_id);
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

    public List<Label> getAllLabels(){
        return taskRepository.getAllLabels();
    }

    public String getLabelsInString(List<Label> labels){
        return taskRepository.getLabelsInString(labels);
    }

    public Label getLabelById(int label_id){
        return taskRepository.getLabelById(label_id);
    }

    public List<Label>  getLabelsFromTask(int task_id){
        return taskRepository.getLabelsFromTask(task_id);
    }
}
