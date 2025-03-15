package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.tankDTO;
import com.sena.crud_basic.model.tank;
import com.sena.crud_basic.repository.Itank;

@Service
public class tankService {

    @Autowired
    private Itank tankRepository;

    // Método para guardar una nueva entidad tank a partir del DTO
    public void save(tankDTO tankDTO) {
        tank tankEntity = convertToEntity(tankDTO);
        tankRepository.save(tankEntity);
    }

    // Convierte la entidad tank a DTO
    public tankDTO convertToDTO(tank tankEntity) {
        return new tankDTO(
            tankEntity.getid(),
            tankEntity.getcapacity(),
            tankEntity.getlocation(),
            tankEntity.getwaterType()
        );
    }

    // Convierte el DTO a la entidad tank
    public tank convertToEntity(tankDTO tankDTO) {
        return new tank(
            tankDTO.getId(),
            tankDTO.getCapacity(),
            tankDTO.getLocation(),
            tankDTO.getWaterType()
        );
    }
}
