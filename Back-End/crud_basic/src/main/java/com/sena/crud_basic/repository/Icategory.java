package com.sena.crud_basic.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sena.crud_basic.model.category;

@Repository
public interface Icategory extends JpaRepository<category, Integer> {
    List<category> findByNameContainingIgnoreCase(String name);
    
    // ✅ Agregar el método para verificar si una categoría ya existe
    boolean existsByName(String name);
}
