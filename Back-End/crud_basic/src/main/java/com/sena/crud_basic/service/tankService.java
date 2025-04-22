package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.tankDTO;
import com.sena.crud_basic.model.tank;
import com.sena.crud_basic.repository.Itank;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class tankService {

    @Autowired
    private Itank tankRepository;

    // Crear un nuevo tank
    public String save(tankDTO tankDTO) {
        try {
            if (tankDTO.getCapacity() <= 0) {
                return "La capacidad debe ser mayor que cero.";
            }
            if (tankDTO.getLocation() == null || tankDTO.getLocation().isEmpty()) {
                return "La ubicación es obligatoria.";
            }
            if (tankDTO.getWaterType() == null || tankDTO.getWaterType().isEmpty()) {
                return "El tipo de agua es obligatorio.";
            }
            tank tankEntity = convertToEntity(tankDTO);
            tankRepository.save(tankEntity);
            return "Tank registrado exitosamente.";
        } catch (Exception e) {
            return "Error interno al guardar tank: " + e.getMessage();
        }
    }

    // Obtener todos los tanks
    public List<tankDTO> getAllTanks() {
        try {
            return tankRepository.findAll()
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error interno al obtener tanks: " + e.getMessage());
        }
    }

    // Obtener un tank por id
    public Optional<tankDTO> getTankById(int id) {
        try {
            return tankRepository.findById(id).map(this::convertToDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error interno al obtener el tank: " + e.getMessage());
        }
    }

    // Actualizar un tank existente
    public String updateTank(int id, tankDTO tankDTO) {
        try {
            Optional<tank> optionalTank = tankRepository.findById(id);
            if (optionalTank.isEmpty()) {
                return "Tank no encontrado.";
            }
            tank tankEntity = optionalTank.get();
            tankEntity.setCapacity(tankDTO.getCapacity());
            tankEntity.setLocation(tankDTO.getLocation());
            tankEntity.setWaterType(tankDTO.getWaterType());
            tankRepository.save(tankEntity);
            return "Tank actualizado exitosamente.";
        } catch (Exception e) {
            return "Error interno al actualizar tank: " + e.getMessage();
        }
    }

    // Eliminar un tank
    public String deleteTank(int id) {
        try {
            Optional<tank> optionalTank = tankRepository.findById(id);
            if (optionalTank.isEmpty()) {
                return "Tank no encontrado.";
            }
            tankRepository.deleteById(id);
            return "Tank eliminado exitosamente.";
        } catch (Exception e) {
            return "Error interno al eliminar tank: " + e.getMessage();
        }
    }

    // Filtrar tanks por location y waterType (si se envía alguno de los parámetros, se aplican)
    public List<tankDTO> filterTanks(String location, String waterType) {
        try {
            List<tank> filteredTanks;
            boolean hasLocation = (location != null && !location.isEmpty());
            boolean hasWaterType = (waterType != null && !waterType.isEmpty());
            if (hasLocation && hasWaterType) {
                filteredTanks = tankRepository.findByLocationContainingIgnoreCaseAndWaterTypeContainingIgnoreCase(location, waterType);
            } else if (hasLocation) {
                filteredTanks = tankRepository.findByLocationContainingIgnoreCase(location);
            } else if (hasWaterType) {
                filteredTanks = tankRepository.findByWaterTypeContainingIgnoreCase(waterType);
            } else {
                filteredTanks = tankRepository.findAll();
            }
            return filteredTanks.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error interno al filtrar tanks: " + e.getMessage());
        }
    }
    
    // Métodos de conversión entre tank y tankDTO
    private tankDTO convertToDTO(tank tankEntity) {
        return new tankDTO(
                tankEntity.getId(),
                tankEntity.getCapacity(),
                tankEntity.getLocation(),
                tankEntity.getWaterType()
        );
    }

    private tank convertToEntity(tankDTO tankDTO) {
        return new tank(
                tankDTO.getId(),
                tankDTO.getCapacity(),
                tankDTO.getLocation(),
                tankDTO.getWaterType()
        );
    }
}
