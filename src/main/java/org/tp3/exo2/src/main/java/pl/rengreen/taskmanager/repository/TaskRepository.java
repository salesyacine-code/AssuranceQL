package org.tp3.exo2.src.main.java.pl.rengreen.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tp3.exo2.src.main.java.pl.rengreen.taskmanager.model.Task;
import org.tp3.exo2.src.main.java.pl.rengreen.taskmanager.model.User;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByOwnerOrderByDateDesc(User user);
}