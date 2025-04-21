package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.foodDTO;
import com.sena.crud_basic.model.food;
import com.sena.crud_basic.repository.Ifood;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class foodService {

    @Autowired
    private Ifood foodRepository;

    public String save(foodDTO foodDTO) {
        try {
            if (foodDTO.getType() == null || foodDTO.getType().isEmpty()) {
                return "El tipo de alimento es obligatorio.";
            }

            if (foodDTO.getBrand() == null || foodDTO.getBrand().isEmpty()) {
                return "La marca del alimento es obligatoria.";
            }

            food foodEntity = convertToEntity(foodDTO);
            foodRepository.save(foodEntity);
            return "Alimento guardado exitosamente.";
        } catch (Exception e) {
            e.printStackTrace(); // Importante para debug
            return "Error interno al guardar alimento: " + e.getMessage();
        }
    }

    public List<foodDTO> getAllFoods() {
        try {
            List<food> foods = foodRepository.findAll();
            return foods.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace(); // Importante para debug
            throw new RuntimeException("Error interno al obtener alimentos: " + e.getMessage());
        }
    }

    public Optional<foodDTO> getFoodById(int id) {
        try {
            return foodRepository.findById(id).map(this::convertToDTO);
        } catch (Exception e) {
            e.printStackTrace(); // Importante para debug
            throw new RuntimeException("Error interno al obtener el alimento: " + e.getMessage());
        }
    }

    public String updateFood(int id, foodDTO foodDTO) {
        try {
            Optional<food> foodOpt = foodRepository.findById(id);
            if (foodOpt.isEmpty()) return "Alimento no encontrado.";

            if (foodDTO.getType() == null || foodDTO.getType().isEmpty()) {
                return "El tipo de alimento es obligatorio.";
            }

            if (foodDTO.getBrand() == null || foodDTO.getBrand().isEmpty()) {
                return "La marca del alimento es obligatoria.";
            }

            food foodEntity = foodOpt.get();
            foodEntity.setType(foodDTO.getType());
            foodEntity.setBrand(foodDTO.getBrand());
            
            // Manejo seguro de supplier
            if (foodDTO.getSupplier() != null) {
                foodEntity.setSupplier(foodDTO.getSupplier());
            }

            foodRepository.save(foodEntity);
            return "Alimento actualizado exitosamente.";
        } catch (Exception e) {
            e.printStackTrace(); // Importante para debug
            return "Error interno al actualizar alimento: " + e.getMessage();
        }
    }

    public String deleteFood(int id) {
        try {
            Optional<food> foodOpt = foodRepository.findById(id);
            if (foodOpt.isEmpty()) return "Alimento no encontrado.";

            foodRepository.deleteById(id);
            return "Alimento eliminado exitosamente.";
        } catch (Exception e) {
            e.printStackTrace(); // Importante para debug
            return "Error interno al eliminar alimento: " + e.getMessage();
        }
    }

    public List<foodDTO> filterFoods(String type, String brand) {
        try {
            List<food> filteredFoods;

            // Manejo mejorado para evitar NullPointerException
            if (type != null && !type.isEmpty() && brand != null && !brand.isEmpty()) {
                // Ambos parámetros presentes
                filteredFoods = foodRepository.findByTypeContainingIgnoreCaseOrBrandContainingIgnoreCase(type, brand);
            } else if (type != null && !type.isEmpty()) {
                // Solo type presente
                filteredFoods = foodRepository.findByTypeContainingIgnoreCase(type != null ? type : "");
            } else if (brand != null && !brand.isEmpty()) {
                // Solo brand presente
                filteredFoods = foodRepository.findByBrandContainingIgnoreCase(brand != null ? brand : "");
            } else {
                // Ningún parámetro presente
                filteredFoods = foodRepository.findAll();
            }

            return filteredFoods.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace(); // Importante para debug
            throw new RuntimeException("Error interno al filtrar alimentos: " + e.getMessage());
        }
    }

    private foodDTO convertToDTO(food foodEntity) {
        if (foodEntity == null) {
            return null;
        }
        return new foodDTO(
            foodEntity.getId(), 
            foodEntity.getType(), 
            foodEntity.getBrand(), 
            foodEntity.getSupplier()
        );
    }

    private food convertToEntity(foodDTO foodDTO) {
        if (foodDTO == null) {
            return null;
        }
        return new food(
            foodDTO.getId(), 
            foodDTO.getType(), 
            foodDTO.getBrand(), 
            foodDTO.getSupplier()
        );
    }
}