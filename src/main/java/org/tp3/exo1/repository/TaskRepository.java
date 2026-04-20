package org.tp3.exo1.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.tp3.exo1.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {}