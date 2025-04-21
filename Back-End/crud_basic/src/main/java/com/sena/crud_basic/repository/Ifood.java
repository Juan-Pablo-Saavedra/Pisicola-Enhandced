package com.sena.crud_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sena.crud_basic.model.food;
import java.util.List;

@Repository
public interface Ifood extends JpaRepository<food, Integer> {

    // Buscar alimentos asociados a un proveedor específico
    List<food> findBySupplierId(int supplierId);

    // Buscar alimentos cuyo tipo contenga la cadena (ignora mayúsculas/minúsculas)
    List<food> findByTypeContainingIgnoreCase(String type);

    // Buscar alimentos cuya marca contenga la cadena (ignora mayúsculas/minúsculas)
    List<food> findByBrandContainingIgnoreCase(String brand);

    // Buscar alimentos cuyo tipo o marca contenga la cadena (ignora mayúsculas/minúsculas)
    List<food> findByTypeContainingIgnoreCaseOrBrandContainingIgnoreCase(String type, String brand);
}
