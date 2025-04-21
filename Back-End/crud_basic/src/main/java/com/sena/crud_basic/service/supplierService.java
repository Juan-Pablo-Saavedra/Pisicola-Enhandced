package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.supplierDTO;
import com.sena.crud_basic.model.food;
import com.sena.crud_basic.model.supplier;
import com.sena.crud_basic.repository.Isupplier;
import com.sena.crud_basic.repository.Ifood;
import java.util.List;
import java.util.Optional;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
public class supplierService {

    @Autowired
    private Isupplier supplierRepository;
    
    @Autowired
    private Ifood foodRepository;

    public String save(supplierDTO supplierDTO) {
        try {
            if (supplierDTO.getName() == null || supplierDTO.getName().isEmpty()) {
                return "El nombre del proveedor es obligatorio.";
            }
            if (supplierDTO.getCategory() == null || supplierDTO.getCategory().isEmpty()) {
                return "La categoría del proveedor es obligatoria.";
            }
            if (supplierDTO.getContact() == null || supplierDTO.getContact().isEmpty()) {
                return "El contacto del proveedor es obligatorio.";
            }
            if (supplierDTO.getEmail() == null || supplierDTO.getEmail().isEmpty()) {
                return "El email del proveedor es obligatorio.";
            }
            supplier supplierEntity = convertToEntity(supplierDTO);
            supplierRepository.save(supplierEntity);
            return "Proveedor guardado exitosamente.";
        } catch (Exception e) {
            return "Error interno al guardar proveedor: " + e.getMessage();
        }
    }

    public List<supplierDTO> getAllSuppliers() {
        try {
            return supplierRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error interno al obtener proveedores: " + e.getMessage());
        }
    }

    public Optional<supplierDTO> getSupplierById(int id) {
        try {
            return supplierRepository.findById(id).map(this::convertToDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error interno al obtener el proveedor: " + e.getMessage());
        }
    }

    public String updateSupplier(int id, supplierDTO supplierDTO) {
        try {
            Optional<supplier> supplierOpt = supplierRepository.findById(id);
            if (supplierOpt.isEmpty()) return "Proveedor no encontrado.";
            
            supplier supplierEntity = supplierOpt.get();
            supplierEntity.setName(supplierDTO.getName());
            supplierEntity.setCategory(supplierDTO.getCategory());
            supplierEntity.setContact(supplierDTO.getContact());
            // Asignamos phone usando el mismo valor que contact
            supplierEntity.setPhone(supplierDTO.getContact());
            supplierEntity.setEmail(supplierDTO.getEmail());
            
            if (supplierDTO.getFoodAssociations() != null && !supplierDTO.getFoodAssociations().isEmpty()) {
                int foodId = supplierDTO.getFoodAssociations().get(0).getId();
                food foodEntity = foodRepository.getReferenceById(foodId);
                // Establecer la relación bidireccional:
                foodEntity.setSupplier(supplierEntity);
                supplierEntity.setFoodAssociations(Collections.singletonList(foodEntity));
            } else {
                supplierEntity.setFoodAssociations(Collections.emptyList());
            }
            
            supplierRepository.save(supplierEntity);
            return "Proveedor actualizado exitosamente.";
        } catch (Exception e) {
            return "Error interno al actualizar proveedor: " + e.getMessage();
        }
    }

    public String deleteSupplier(int id) {
        try {
            Optional<supplier> supplierOpt = supplierRepository.findById(id);
            if (supplierOpt.isEmpty()) return "Proveedor no encontrado.";
            
            supplierRepository.deleteById(id);
            return "Proveedor eliminado exitosamente.";
        } catch (Exception e) {
            return "Error interno al eliminar proveedor: " + e.getMessage();
        }
    }

    public List<supplierDTO> filterSuppliers(String category, String name) {
        try {
            List<supplier> filteredSuppliers;
            boolean hasCategory = (category != null && !category.isEmpty());
            boolean hasName = (name != null && !name.isEmpty());
            if (hasCategory && hasName) {
                filteredSuppliers = supplierRepository.findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(name, category);
            } else if (hasCategory) {
                filteredSuppliers = supplierRepository.findByCategoryContainingIgnoreCase(category);
            } else if (hasName) {
                filteredSuppliers = supplierRepository.findByNameContainingIgnoreCase(name);
            } else {
                filteredSuppliers = supplierRepository.findAll();
            }
            return filteredSuppliers.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error interno al filtrar proveedores: " + e.getMessage());
        }
    }

    private supplierDTO convertToDTO(supplier supplierEntity) {
        return new supplierDTO(
            supplierEntity.getId(),
            supplierEntity.getName(),
            supplierEntity.getCategory(),
            supplierEntity.getContact(),
            supplierEntity.getEmail(),
            supplierEntity.getFoodAssociations()
        );
    }

    private supplier convertToEntity(supplierDTO supplierDTO) {
        // Usamos el constructor de supplier con 6 parámetros:
        // (id, name, category, contact, phone, email). Asignamos phone con el valor de contact.
        supplier sup = new supplier(
            supplierDTO.getId(),
            supplierDTO.getName(),
            supplierDTO.getCategory(),
            supplierDTO.getContact(),
            supplierDTO.getContact(),
            supplierDTO.getEmail()
        );
        if (supplierDTO.getFoodAssociations() != null && !supplierDTO.getFoodAssociations().isEmpty()) {
            int foodId = supplierDTO.getFoodAssociations().get(0).getId();
            food foodEntity = foodRepository.getReferenceById(foodId);
            // Establecer la relación bidireccional:
            foodEntity.setSupplier(sup);
            sup.setFoodAssociations(Collections.singletonList(foodEntity));
        } else {
            sup.setFoodAssociations(Collections.emptyList());
        }
        return sup;
    }
}
