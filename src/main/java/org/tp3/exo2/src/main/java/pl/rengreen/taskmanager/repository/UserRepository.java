package org.tp3.exo2.src.main.java.pl.rengreen.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tp3.exo2.src.main.java.pl.rengreen.taskmanager.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);
}
