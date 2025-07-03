package com.sena.crud_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sena.crud_basic.model.employee;
import com.sena.crud_basic.model.rol;

import java.util.List;
import java.util.Optional;

@Repository
public interface Iemployee extends JpaRepository<employee, Integer> {
    /*
     * C - Create
     * R - Read
     * U - Update
     * D - Delete
     */

    // Buscar empleado por correo electrónico
    Optional<employee> findByEmail(String email);

    // Filtrar empleados por nombre (ignorando mayúsculas/minúsculas)
    List<employee> findByNameContainingIgnoreCase(String name);

    // Filtrar empleados por teléfono (ignorando mayúsculas/minúsculas)
    List<employee> findByPhoneContainingIgnoreCase(String phone);

    // Filtrar empleados por cargo (posición) (ignorando mayúsculas/minúsculas)
    List<employee> findByPosition(rol position);

    // Filtrar empleados por correo electrónico (ignorando mayúsculas/minúsculas)
    List<employee> findByEmailContainingIgnoreCase(String email);

    List<employee> findAllByEnabled(boolean enabled);
    
    
}
