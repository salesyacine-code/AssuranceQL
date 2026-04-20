package org.tp3.exo2.src.main.java.pl.rengreen.taskmanager.service;

import org.tp3.exo2.src.main.java.pl.rengreen.taskmanager.model.Task;
import org.tp3.exo2.src.main.java.pl.rengreen.taskmanager.model.User;

import java.util.List;

public interface TaskService {

    void createTask(Task task);

    void updateTask(Long id, Task task);

    void deleteTask(Long id);

    List<Task> findAll();

    List<Task> findByOwnerOrderByDateDesc(User user);

    void setTaskCompleted(Long id);

    void setTaskNotCompleted(Long id);

    List<Task> findFreeTasks();

    Task getTaskById(Long taskId);

    void assignTaskToUser(Task task, User user);

    void unassignTask(Task task);
}
