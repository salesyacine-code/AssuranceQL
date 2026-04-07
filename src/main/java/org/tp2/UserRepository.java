// src/main/java/com/example/repository/UserRepository.java
package org.tp2;

import org.tp2.User;

public interface UserRepository {
    /**
     * Recherche un utilisateur par son identifiant.
     * @param id l'identifiant de l'utilisateur
     * @return l'utilisateur correspondant, ou null si non trouvé
     */
    User findUserById(long id);
}