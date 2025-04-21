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

    // Método para guardar un tanque con validaciones
    public String save(tankDTO tankDTO) {
        if (tankDTO.getCapacity() <= 0) {
            return "La capacidad del tanque debe ser mayor a 0.";
        }

        if (tankDTO.getLocation() == null || tankDTO.getLocation().isEmpty()) {
            return "La ubicación del tanque es obligatoria.";
        }

        if (tankDTO.getWaterType() == null || tankDTO.getWaterType().isEmpty()) {
            return "El tipo de agua es obligatorio.";
        }

        tank tankEntity = convertToEntity(tankDTO);
        tankRepository.save(tankEntity);
        return "Tanque guardado exitosamente.";
    }

    // Método para obtener todos los tanques
    public List<tankDTO> getAllTanks() {
        List<tank> tanks = tankRepository.findAll();
        return tanks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Método para obtener un tanque por ID
    public Optional<tankDTO> getTankById(int id) {
        Optional<tank> tankOpt = tankRepository.findById(id);
        return tankOpt.map(this::convertToDTO);
    }

    // Método para actualizar un tanque con validaciones
    public String updateTank(int id, tankDTO tankDTO) {
        Optional<tank> existingTankOpt = tankRepository.findById(id);

        if (existingTankOpt.isEmpty()) {
            return "Tanque no encontrado.";
        }

        if (tankDTO.getCapacity() <= 0) {
            return "La capacidad del tanque debe ser mayor a 0.";
        }

        if (tankDTO.getLocation() == null || tankDTO.getLocation().isEmpty()) {
            return "La ubicación del tanque es obligatoria.";
        }

        if (tankDTO.getWaterType() == null || tankDTO.getWaterType().isEmpty()) {
            return "El tipo de agua es obligatorio.";
        }

        tank tankEntity = existingTankOpt.get();
        tankEntity.setCapacity(tankDTO.getCapacity());
        tankEntity.setLocation(tankDTO.getLocation());
        tankEntity.setWaterType(tankDTO.getWaterType());

        tankRepository.save(tankEntity);
        return "Tanque actualizado exitosamente.";
    }

    // Método para eliminar un tanque
    public String deleteTank(int id) {
        Optional<tank> tankOpt = tankRepository.findById(id);

        if (tankOpt.isEmpty()) {
            return "Tanque no encontrado.";
        }

        tankRepository.deleteById(id);
        return "Tanque eliminado exitosamente.";
    }

    // Conversión entre entidad y DTO
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
