package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.foodDTO;
import com.sena.crud_basic.model.food;
import com.sena.crud_basic.repository.Ifood;

@Service
public class foodService {

    @Autowired
    private Ifood foodRepository;

    // Método para guardar un food a partir del DTO
    public void save(foodDTO foodDTO) {
        food foodEntity = convertToEntity(foodDTO);
        foodRepository.save(foodEntity);
    }

    // Conversión de entidad a DTO
    public foodDTO convertToDTO(food foodEntity) {
        return new foodDTO(
            foodEntity.getid(),
            foodEntity.gettype(),
            foodEntity.getbrand(),
            foodEntity.getsupplier()
        );
    }

    // Conversión de DTO a entidad
    public food convertToEntity(foodDTO foodDTO) {
        return new food(
            foodDTO.getId(),
            foodDTO.getType(),
            foodDTO.getBrand(),
            foodDTO.getSupplier()
        );
    }
}
