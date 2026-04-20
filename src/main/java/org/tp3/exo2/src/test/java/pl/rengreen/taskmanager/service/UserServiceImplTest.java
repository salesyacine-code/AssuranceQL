package org.tp3.exo2.src.test.java.pl.rengreen.taskmanager.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.tp3.exo2.src.main.java.pl.rengreen.taskmanager.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
public class UserServiceImplTest {

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
    private UserService userService;

    @Test
    public void findAll_shouldReturnUsersList() {
        // Act
        List<User> users = userService.findAll();

        // Assert — les données initiales sont chargées par InitialDataLoader
        assertThat(users).isNotEmpty();
    }

    @Test
    public void getUserByEmail_shouldReturnCorrectUser() {
        // Act — admin créé par InitialDataLoader
        User user = userService.getUserByEmail("admin@admin.com");

        // Assert
        assertThat(user).isNotNull();
        assertThat(user.getEmail()).isEqualTo("admin@admin.com");
    }

    @Test
    public void createUser_shouldPersistUserInDatabase() {
        // Arrange — nouveau scénario ajouté
        User user = new User();
        user.setName("Test User");
        user.setEmail("testuser@test.com");
        user.setPassword("password123");

        // Act
        userService.createUser(user);
        User found = userService.getUserByEmail("testuser@test.com");

        // Assert
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Test User");
    }
}
