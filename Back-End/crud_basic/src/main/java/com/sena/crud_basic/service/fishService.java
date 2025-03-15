package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.fishDTO;
import com.sena.crud_basic.model.fish;
import com.sena.crud_basic.repository.Ifish;

@Service
public class fishService {

    @Autowired
    private Ifish fishRepository;

    // Método para crear un nuevo fish a partir del DTO
    public void save(fishDTO fishDTO) {
        fish fishEntity = convertToEntity(fishDTO);
        fishRepository.save(fishEntity);
    }

    // Método para convertir una entidad fish a fishDTO
    public fishDTO convertToDTO(fish fishEntity) {
        return new fishDTO(
            fishEntity.getid(),
            fishEntity.getspecies(),
            fishEntity.getsize(),
            fishEntity.getweight()
        );
    }

    // Método para convertir fishDTO a una entidad fish
    public fish convertToEntity(fishDTO fishDTO) {
        return new fish(
            fishDTO.getId(),
            fishDTO.getSpecies(),
            fishDTO.getSize(),
            fishDTO.getWeight()
        );
    }
}
