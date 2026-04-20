package org.tp3.exo2.src.test.java.pl.rengreen.taskmanager.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.tp3.exo2.src.main.java.pl.rengreen.taskmanager.model.Task;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class TaskServiceImplTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("taskmanager")
            .withUsername("root")
            .withPassword("password");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.driver-class-name",
                () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.database-platform",
                () -> "org.hibernate.dialect.MySQL5InnoDBDialect");
    }

    @Autowired
    private TaskService taskService;

    @Test
    public void findAll_shouldReturnTasksList() {
        // Arrange
        Task task1 = new Task();
        task1.setName("Task 1");
        task1.setDescription("Description 1");
        task1.setDate(LocalDate.now());
        task1.setCompleted(false);

        Task task2 = new Task();
        task2.setName("Task 2");
        task2.setDescription("Description 2");
        task2.setDate(LocalDate.now());
        task2.setCompleted(false);

        taskService.createTask(task1);
        taskService.createTask(task2);

        // Act
        List<Task> tasks = taskService.findAll();

        // Assert
        assertThat(tasks).isNotEmpty();
        assertThat(tasks.size()).isGreaterThanOrEqualTo(2);
    }

    @Test
    public void createTask_shouldPersistTaskInDatabase() {
        // Arrange
        Task task = new Task();
        task.setName("New Task");
        task.setDescription("New Description");
        task.setDate(LocalDate.now());
        task.setCompleted(false);

        // Act
        taskService.createTask(task);
        Task found = taskService.getTaskById(task.getId());

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("New Task");
        assertThat(found.getDescription()).isEqualTo("New Description");
    }

    @Test
    public void deleteTask_shouldRemoveTaskFromDatabase() {
        // Arrange
        Task task = new Task();
        task.setName("Task to delete");
        task.setDescription("Will be deleted");
        task.setDate(LocalDate.now());
        task.setCompleted(false);
        taskService.createTask(task);
        Long id = task.getId();

        // Act
        taskService.deleteTask(id);

        // Assert
        assertThat(taskService.getTaskById(id)).isNull();
    }

    @Test
    public void setTaskCompleted_shouldMarkTaskAsCompleted() {
        // Arrange — nouveau scénario ajouté
        Task task = new Task();
        task.setName("Task to complete");
        task.setDescription("Will be completed");
        task.setDate(LocalDate.now());
        task.setCompleted(false);
        taskService.createTask(task);

        // Act
        taskService.setTaskCompleted(task.getId());
        Task updated = taskService.getTaskById(task.getId());

        // Assert
        assertThat(updated.isCompleted()).isTrue();
    }
}
