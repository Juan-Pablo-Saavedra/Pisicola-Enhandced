package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.categoryDTO;
import com.sena.crud_basic.model.category;
import com.sena.crud_basic.repository.Icategory;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class categoryService {

    @Autowired
    private Icategory categoryRepository;

    public String save(categoryDTO categoryDTO) {
        if (categoryDTO.getName() == null || categoryDTO.getName().isEmpty()) {
            return "El nombre de la categoría es obligatorio.";
        }

        if (categoryRepository.existsByName(categoryDTO.getName())) {
            return "La categoría ya existe.";
        }

        category categoryEntity = convertToEntity(categoryDTO);
        categoryRepository.save(categoryEntity);
        return "Categoría guardada exitosamente.";
    }

    public List<categoryDTO> getAllCategories() {
        List<category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<categoryDTO> getCategoryById(int id) {
        Optional<category> categoryOpt = categoryRepository.findById(id);
        return categoryOpt.map(this::convertToDTO);
    }

    public String updateCategory(int id, categoryDTO categoryDTO) {
        Optional<category> existingCategoryOpt = categoryRepository.findById(id);

        if (existingCategoryOpt.isEmpty()) {
            return "Categoría no encontrada.";
        }

        if (categoryDTO.getName() == null || categoryDTO.getName().isEmpty()) {
            return "El nombre de la categoría es obligatorio.";
        }

        category categoryEntity = existingCategoryOpt.get();
        categoryEntity.setName(categoryDTO.getName());

        categoryRepository.save(categoryEntity);
        return "Categoría actualizada exitosamente.";
    }

    public String deleteCategory(int id) {
        Optional<category> categoryOpt = categoryRepository.findById(id);

        if (categoryOpt.isEmpty()) {
            return "Categoría no encontrada.";
        }

        categoryRepository.deleteById(id);
        return "Categoría eliminada exitosamente.";
    }

    // Método de conversión a DTO
    private categoryDTO convertToDTO(category categoryEntity) {
        return new categoryDTO(categoryEntity.getId(), categoryEntity.getName());
    }

    // Método de conversión a entidad
    private category convertToEntity(categoryDTO categoryDTO) {
        return new category(categoryDTO.getId(), categoryDTO.getName());
    }

    public List<categoryDTO> filterCategories(String name) {
        List<category> filteredCategories;
    
        if (name != null && !name.isEmpty()) {
            filteredCategories = categoryRepository.findByNameContainingIgnoreCase(name);
        } else {
            filteredCategories = categoryRepository.findAll();
        }
    
        return filteredCategories.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
}
