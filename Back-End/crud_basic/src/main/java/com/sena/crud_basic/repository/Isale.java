package com.sena.crud_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sena.crud_basic.model.sale;
import java.util.List;
import java.util.Date;

@Repository
public interface Isale extends JpaRepository<sale, Integer> {

    // Buscar ventas dentro de un rango de fechas
    List<sale> findByDateBetween(Date start, Date end);
    
    // Buscar ventas cuyo customer tenga un nombre que contenga (ignora mayúsculas/minúsculas)
    List<sale> findByCustomer_NameContainingIgnoreCase(String name);
}
