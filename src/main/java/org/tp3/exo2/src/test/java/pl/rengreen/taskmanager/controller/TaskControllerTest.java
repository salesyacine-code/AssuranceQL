package org.tp3.exo2.src.test.java.pl.rengreen.taskmanager.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.instanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.tp3.exo2.src.main.java.pl.rengreen.taskmanager.model.Task;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class TaskControllerTest {

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
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    public void updateTask_shouldReturnStatusOkAndFilledTaskForm() throws Exception {
        mockMvc.perform(get("/task/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("views/filledTaskForm"))
                .andExpect(model().attribute("task", instanceOf(Task.class)));
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    public void deleteTask_shouldRedirectToTasks() throws Exception {
        mockMvc.perform(get("/task/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/tasks"));
    }

    @Test
    @WithMockUser(username = "admin@admin.com", roles = {"ADMIN"})
    public void listTasks_shouldReturnTasksView() throws Exception {
        // Nouveau scénario ajouté
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(view().name("views/tasks"));
    }
}
