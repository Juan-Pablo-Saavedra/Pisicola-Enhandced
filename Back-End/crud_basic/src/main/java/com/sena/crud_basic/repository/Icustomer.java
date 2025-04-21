package com.sena.crud_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sena.crud_basic.model.customer;
import java.util.List;
import java.util.Optional;

@Repository
public interface Icustomer extends JpaRepository<customer, Integer> {

    // Método para buscar un cliente por correo electrónico (CORRECCIÓN)
    Optional<customer> findByEmail(String email);

    // Métodos existentes para filtrado
    List<customer> findByNameContainingIgnoreCase(String name);
    List<customer> findByPhoneContainingIgnoreCase(String phone);
    List<customer> findByEmailContainingIgnoreCase(String email);
}
