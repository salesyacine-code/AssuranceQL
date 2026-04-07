// src/main/java/com/example/service/UserService.java
package org.tp2.exo1;

public class UserService {

    private final UserRepository userRepository;

    // Injection de dépendance par constructeur
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Récupère un utilisateur par son ID via le repository.
     * @param id l'identifiant de l'utilisateur
     * @return l'utilisateur trouvé
     * @throws IllegalArgumentException si l'utilisateur n'existe pas
     */
    public User getUserById(long id) {
        User user = userRepository.findUserById(id);
        if (user == null) {
            throw new IllegalArgumentException("Utilisateur introuvable pour l'id : " + id);
        }
        return user;
    }
}