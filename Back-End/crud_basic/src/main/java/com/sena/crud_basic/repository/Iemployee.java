package com.sena.crud_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sena.crud_basic.model.employee;
import java.util.Optional;

@Repository
public interface Iemployee extends JpaRepository<employee, Integer> {
    /*
     * C - Create
     * R - Read
     * U - Update
     * D - Delete
     */

    // Método para buscar un empleado por su email
    Optional<employee> findByEmail(String email);
}
