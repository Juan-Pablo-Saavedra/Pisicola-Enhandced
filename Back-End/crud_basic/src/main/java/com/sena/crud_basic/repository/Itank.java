package com.sena.crud_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sena.crud_basic.model.tank;
import java.util.List;

@Repository
public interface Itank extends JpaRepository<tank, Integer> {

    // Buscar tanks cuyo valor de location contenga la cadena (sin distinguir mayúsculas o minúsculas)
    List<tank> findByLocationContainingIgnoreCase(String location);

    // Buscar tanks cuyo waterType contenga la cadena (sin distinguir mayúsculas o minúsculas)
    List<tank> findByWaterTypeContainingIgnoreCase(String waterType);

    // Buscar tanks filtrando por ambos: location y waterType
    List<tank> findByLocationContainingIgnoreCaseAndWaterTypeContainingIgnoreCase(String location, String waterType);
}
