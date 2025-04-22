package com.sena.crud_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sena.crud_basic.model.maintenance;
import java.util.Date;
import java.util.List;

@Repository
public interface Imaintenance extends JpaRepository<maintenance, Integer> {
    // Ejemplo: búsqueda por rango de fechas.
    List<maintenance> findByDateBetween(Date start, Date end);
}
