package org.tp3.exo2.src.main.java.pl.rengreen.taskmanager.service;

import org.tp3.exo2.src.main.java.pl.rengreen.taskmanager.model.Role;

import java.util.List;

public interface RoleService {
    Role createRole(Role role);

    List<Role> findAll();
}
