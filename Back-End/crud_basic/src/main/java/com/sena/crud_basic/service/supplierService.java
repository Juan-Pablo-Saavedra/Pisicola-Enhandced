package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.supplierDTO;
import com.sena.crud_basic.model.supplier;
import com.sena.crud_basic.repository.Isupplier;

@Service
public class supplierService {

    @Autowired
    private Isupplier supplierRepository;

    // Método para guardar un supplier a partir del DTO
    public void save(supplierDTO supplierDTO) {
        supplier supplierEntity = convertToEntity(supplierDTO);
        supplierRepository.save(supplierEntity);
    }

    // Convierte la entidad supplier a DTO
    public supplierDTO convertToDTO(supplier supplierEntity) {
        return new supplierDTO(
            supplierEntity.getid(),
            supplierEntity.getname(),
            supplierEntity.getcontact(),
            supplierEntity.getphone()
        );
    }

    // Convierte el DTO a la entidad supplier
    public supplier convertToEntity(supplierDTO supplierDTO) {
        return new supplier(
            supplierDTO.getId(),
            supplierDTO.getName(),
            supplierDTO.getContact(),
            supplierDTO.getPhone()
        );
    }
}
