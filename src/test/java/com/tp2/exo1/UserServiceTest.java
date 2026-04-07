// src/test/java/com/example/service/UserServiceTest.java

package  com.tp2.exo1;
import org.tp2.exo1.User;
import org.tp2.exo1.UserRepository;
import org.tp2.exo1.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)   // Active Mockito avec JUnit 5
class UserServiceTest {

    @Mock
    private UserRepository userRepository;   // Mock auto-créé par Mockito

    @InjectMocks
    private UserService userService;         // userRepository injecté automatiquement

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User(1L, "Alice Dupont", "alice@example.com");
    }

    // ── Test 1 : cas nominal ────────────────────────────────────────────────

    @Test
    @DisplayName("getUserById doit retourner l'utilisateur correspondant")
    void getUserById_shouldReturnUser_whenUserExists() {
        // ARRANGE – configurer le comportement du mock
        when(userRepository.findUserById(1L)).thenReturn(mockUser);

        // ACT – appeler la méthode testée
        User result = userService.getUserById(1L);

        // ASSERT – vérifier le résultat
        assertNotNull(result, "Le résultat ne doit pas être null");
        assertEquals(1L,              result.getId(),    "L'id doit correspondre");
        assertEquals("Alice Dupont",  result.getName(),  "Le nom doit correspondre");
        assertEquals("alice@example.com", result.getEmail(), "L'email doit correspondre");
    }

    // ── Test 2 : vérification de l'appel au mock (tâche 4 du TP) ──────────

    @Test
    @DisplayName("getUserById doit appeler findUserById avec le bon argument")
    void getUserById_shouldCallFindUserById_withCorrectId() {
        // ARRANGE
        when(userRepository.findUserById(1L)).thenReturn(mockUser);

        // ACT
        userService.getUserById(1L);

        // ASSERT – verify() confirme que findUserById a bien été appelé avec l'id 1L
        verify(userRepository, times(1)).findUserById(1L);

        // S'assurer qu'aucune autre interaction n'a eu lieu sur le mock
        verifyNoMoreInteractions(userRepository);
    }

    // ── Test 3 : cas d'erreur ───────────────────────────────────────────────

    @Test
    @DisplayName("getUserById doit lancer une exception si l'utilisateur est introuvable")
    void getUserById_shouldThrowException_whenUserNotFound() {
        // ARRANGE – le mock retourne null pour un id inexistant
        when(userRepository.findUserById(99L)).thenReturn(null);

        // ACT + ASSERT
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.getUserById(99L)
        );

        assertTrue(ex.getMessage().contains("99"),
                "Le message d'erreur doit mentionner l'id recherché");

        // Vérifier que le mock a bien été sollicité
        verify(userRepository).findUserById(99L);
    }
}