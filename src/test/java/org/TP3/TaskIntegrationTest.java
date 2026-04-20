package org.TP3;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.tp3.exo1.model.Task;
import org.tp3.exo1.service.TaskService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = org.tp3.exo1.Application.class)
@Testcontainers
class TaskIntegrationTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("root")
            .withPassword("password");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
    }

    @Autowired
    private TaskService taskService;

    @Test
    void testCreateTask() {
        Task task = new Task("Tâche 1", "Description de la tâche 1");
        taskService.saveTask(task);
        Optional<Task> found = taskService.findTaskById(task.getId());
        assertTrue(found.isPresent());
        assertEquals(task, found.get());
    }

    @Test
    void testGetTask() {
        Task task = createAndSaveTask();
        Optional<Task> found = taskService.findTaskById(task.getId());
        assertTrue(found.isPresent());
        assertEquals(task, found.get());
    }

    @Test
    void testDeleteTask() {
        Task task = createAndSaveTask();
        taskService.deleteTaskById(task.getId());
        Optional<Task> found = taskService.findTaskById(task.getId());
        assertFalse(found.isPresent());
    }

    private Task createAndSaveTask() {
        Task task = new Task("Tâche 1", "Description de la tâche 1");
        taskService.saveTask(task);
        return task;
    }
}