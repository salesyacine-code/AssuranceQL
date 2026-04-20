package org.tp3.exo1.service;


import org.springframework.stereotype.Service;
import org.tp3.exo1.model.Task;
import org.tp3.exo1.repository.TaskRepository;

import java.util.Optional;

@Service
public class TaskService {

    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public void saveTask(Task task) {
        repository.save(task);
    }

    public Optional<Task> findTaskById(Long id) {
        return repository.findById(id);
    }

    public void deleteTaskById(Long id) {
        repository.deleteById(id);
    }
}