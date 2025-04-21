package com.sena.crud_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sena.crud_basic.model.supplier;
import java.util.List;

@Repository
public interface Isupplier extends JpaRepository<supplier, Integer> {
    
    // Buscar proveedores cuyo nombre contenga la cadena (sin importar mayúsculas o minúsculas)
    List<supplier> findByNameContainingIgnoreCase(String name);

    // Buscar proveedores cuya categoría contenga la cadena (sin importar mayúsculas o minúsculas)
    List<supplier> findByCategoryContainingIgnoreCase(String category);

    // Buscar proveedores cuyo nombre o categoría contenga la cadena indicada
    List<supplier> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String name, String category);
}
