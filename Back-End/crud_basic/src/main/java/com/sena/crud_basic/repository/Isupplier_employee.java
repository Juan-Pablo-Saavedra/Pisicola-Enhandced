package com.sena.crud_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sena.crud_basic.model.supplier_employee;

@Repository
public interface Isupplier_employee extends JpaRepository<supplier_employee, Integer> {
    /*
     * Métodos CRUD proporcionados por JpaRepository:
     * - save, findById, findAll, delete, etc.
     */
}
