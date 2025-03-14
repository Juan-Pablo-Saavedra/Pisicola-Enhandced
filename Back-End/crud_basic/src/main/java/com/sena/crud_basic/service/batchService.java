package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.sena.crud_basic.DTO.batchDTO;
import com.sena.crud_basic.model.batch;
import com.sena.crud_basic.repository.Ibatch;

@Service
public class batchService {

    @Autowired
    private Ibatch data;

    // Método llamado desde el controller para crear un batch
    public void createBatch(batchDTO batchDTO) {
        batch batchRegister = convertToModel(batchDTO);
        data.save(batchRegister);
    }

    // Convierte la entidad "batch" a DTO, transfiriendo los objetos completos
    public batchDTO convertToDTO(batch batchEntity) {
        return new batchDTO(
            batchEntity.getId(),
            batchEntity.getQuantity(),
            batchEntity.getFish(),
            batchEntity.getTank(),
            batchEntity.getFood()
        );
    }

    // Convierte el DTO a entidad "batch"
    public batch convertToModel(batchDTO batchDTO) {
        return new batch(
            batchDTO.getId(),
            batchDTO.getQuantity(),
            batchDTO.getFish(),
            batchDTO.getTank(),
            batchDTO.getFood()
        );
    }
}
