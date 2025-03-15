package com.sena.crud_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sena.crud_basic.model.tank;

@Repository
public interface Itank extends JpaRepository<tank, Integer> {
    // Se heredan los métodos CRUD: save, findById, findAll, delete, etc.
}
