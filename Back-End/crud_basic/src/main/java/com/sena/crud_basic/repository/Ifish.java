package com.sena.crud_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sena.crud_basic.model.fish;
import java.util.List;

@Repository
public interface Ifish extends JpaRepository<fish, Integer> {

    // Filtrar peces por coincidencia parcial (permite encontrar "Cachama" con "Ca")
    List<fish> findBySpeciesContainingIgnoreCase(String species);

    // Filtrar peces por peso mínimo
    List<fish> findByWeightGreaterThanEqual(float weight);

    // Filtrar peces por especie parcial y peso mínimo
    List<fish> findBySpeciesContainingIgnoreCaseAndWeightGreaterThanEqual(String species, float weight);
}

