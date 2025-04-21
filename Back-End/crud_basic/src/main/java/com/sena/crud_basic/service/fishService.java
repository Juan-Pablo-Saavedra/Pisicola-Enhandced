package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.fishDTO;
import com.sena.crud_basic.model.fish;
import com.sena.crud_basic.repository.Ifish;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class fishService {

    @Autowired
    private Ifish fishRepository;

    // Método para crear un nuevo pez con validaciones mejoradas
    public String save(fishDTO fishDTO) {
        if (fishDTO.getSpecies() == null || fishDTO.getSpecies().isEmpty()) {
            return "La especie del pez es obligatoria.";
        }

        if (fishDTO.getWeight() <= 0) {
            return "El peso del pez debe ser mayor a 0.";
        }

        if (fishDTO.getSize() == null || fishDTO.getSize().isEmpty()) {
            return "El tamaño del pez es obligatorio.";
        }

        fish fishEntity = convertToEntity(fishDTO);
        fishRepository.save(fishEntity);
        return "Pez guardado exitosamente.";
    }

    // Método para obtener todos los peces
    public List<fishDTO> getAllFish() {
        List<fish> fishes = fishRepository.findAll();
        return fishes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Método para obtener un pez por ID con manejo de errores
    public Optional<fishDTO> getFishById(int id) {
        Optional<fish> fishOpt = fishRepository.findById(id);
        return fishOpt.map(this::convertToDTO);
    }

    // Método para actualizar un pez con validaciones mejoradas
    public String updateFish(int id, fishDTO fishDTO) {
        Optional<fish> existingFishOpt = fishRepository.findById(id);

        if (existingFishOpt.isEmpty()) {
            return "Pez no encontrado.";
        }

        if (fishDTO.getSpecies() == null || fishDTO.getSpecies().isEmpty()) {
            return "La especie del pez es obligatoria.";
        }

        if (fishDTO.getWeight() <= 0) {
            return "El peso del pez debe ser mayor a 0.";
        }

        if (fishDTO.getSize() == null || fishDTO.getSize().isEmpty()) {
            return "El tamaño del pez es obligatorio.";
        }

        fish fishEntity = existingFishOpt.get();
        fishEntity.setSpecies(fishDTO.getSpecies());
        fishEntity.setSize(fishDTO.getSize());
        fishEntity.setWeight(fishDTO.getWeight());

        fishRepository.save(fishEntity);
        return "Pez actualizado exitosamente.";
    }

    // Método para eliminar un pez con mejor manejo de errores
    public String deleteFish(int id) {
        Optional<fish> fishOpt = fishRepository.findById(id);

        if (fishOpt.isEmpty()) {
            return "Pez no encontrado.";
        }

        fishRepository.deleteById(id);
        return "Pez eliminado exitosamente.";
    }

    // Método optimizado para convertir una entidad fish a fishDTO
    private fishDTO convertToDTO(fish fishEntity) {
        return new fishDTO(
            fishEntity.getId(),
            fishEntity.getSpecies(),
            fishEntity.getSize() != null ? fishEntity.getSize() : "No especificado",
            fishEntity.getWeight()
        );
    }

    // Método optimizado para convertir fishDTO a una entidad fish
    private fish convertToEntity(fishDTO fishDTO) {
        return new fish(
            fishDTO.getId(),
            fishDTO.getSpecies(),
            fishDTO.getSize(),
            fishDTO.getWeight()
        );
    }

    public List<fishDTO> filterFish(String species, Float weight) {
        List<fish> filteredFishes;
    
        if (species != null && weight != null) {
            filteredFishes = fishRepository.findBySpeciesContainingIgnoreCaseAndWeightGreaterThanEqual(species, weight);
        } else if (species != null) {
            filteredFishes = fishRepository.findBySpeciesContainingIgnoreCase(species);
        } else if (weight != null) {
            filteredFishes = fishRepository.findByWeightGreaterThanEqual(weight);
        } else {
            filteredFishes = fishRepository.findAll();
        }
    
        return filteredFishes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
}
