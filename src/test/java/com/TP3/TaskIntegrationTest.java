package com.TP3;

import org.tp3.model.Task;
import org.tp3.repository.TaskRepository;
import org.tp3.service.TaskService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskIntegrationTest {

    // Démarrer un vrai conteneur MySQL via Docker
    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("taskmanager_test")
            .withUsername("testuser")
            .withPassword("testpass");

    // Injecter dynamiquement l'URL MySQL dans Spring
    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void cleanDatabase() {
        taskRepository.deleteAll();
    }

    // TEST 1 : Créer une tâche
    @Test
    @Order(1)
    void testCreateTask() {
        Task task = new Task("Tâche 1", "Description de la tâche 1");
        taskService.saveTask(task);

        assertNotNull(task.getId());
        Optional<Task> retrieved = taskService.findTaskById(task.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("Tâche 1", retrieved.get().getName());
    }

    // TEST 2 : Récupérer une tâche
    @Test
    @Order(2)
    void testGetTask() {
        Task task = new Task("Tâche récupérable", "Description");
        taskService.saveTask(task);

        Optional<Task> retrieved = taskService.findTaskById(task.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("Tâche récupérable", retrieved.get().getName());
    }

    // TEST 3 : Supprimer une tâche
    @Test
    @Order(3)
    void testDeleteTask() {
        Task task = new Task("À supprimer", "Sera supprimée");
        taskService.saveTask(task);
        Long id = task.getId();

        taskService.deleteTaskById(id);

        Optional<Task> retrieved = taskService.findTaskById(id);
        assertFalse(retrieved.isPresent());
    }

    // TEST 4 (nouveau) : Lister toutes les tâches
    @Test
    @Order(4)
    void testListAllTasks() {
        taskService.saveTask(new Task("Tâche A", "desc A"));
        taskService.saveTask(new Task("Tâche B", "desc B"));
        taskService.saveTask(new Task("Tâche C", "desc C"));

        assertEquals(3, taskRepository.findAll().size());
    }
}