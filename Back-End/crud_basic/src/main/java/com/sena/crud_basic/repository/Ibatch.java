package com.sena.crud_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sena.crud_basic.model.batch;
import java.util.List;

@Repository
public interface Ibatch extends JpaRepository<batch, Integer> {

    // Métodos para filtrado
    List<batch> findByQuantityGreaterThanEqual(int quantity);
}
