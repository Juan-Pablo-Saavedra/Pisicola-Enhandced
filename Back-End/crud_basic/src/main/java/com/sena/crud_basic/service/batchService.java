package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.batchDTO;
import com.sena.crud_basic.model.batch;
import com.sena.crud_basic.model.fish;
import com.sena.crud_basic.model.tank;
import com.sena.crud_basic.model.food;
import com.sena.crud_basic.repository.Ibatch;
import com.sena.crud_basic.repository.Ifish;
import com.sena.crud_basic.repository.Itank;
import com.sena.crud_basic.repository.Ifood;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class batchService {

    @Autowired
    private Ibatch batchRepository;

    @Autowired
    private Ifish fishRepository;

    @Autowired
    private Itank tankRepository;

    @Autowired
    private Ifood foodRepository;

    // Método para guardar un batch con validaciones
    public String save(batchDTO batchDTO) {
        if (batchDTO.getQuantity() <= 0) {
            return "La cantidad debe ser mayor a 0.";
        }

        Optional<fish> fishOpt = fishRepository.findById(batchDTO.getFishId());
        Optional<tank> tankOpt = tankRepository.findById(batchDTO.getTankId());
        Optional<food> foodOpt = foodRepository.findById(batchDTO.getFoodId());

        if (fishOpt.isEmpty() || tankOpt.isEmpty() || foodOpt.isEmpty()) {
            return "Los identificadores de fish, tank o food no son válidos.";
        }

        batch batchEntity = convertToEntity(batchDTO, fishOpt.get(), tankOpt.get(), foodOpt.get());
        batchRepository.save(batchEntity);
        return "Batch guardado exitosamente.";
    }

    // Método para obtener todos los batch
    public List<batchDTO> getAllBatch() {
        List<batch> batches = batchRepository.findAll();
        return batches.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Método para obtener un batch por ID
    public Optional<batchDTO> getBatchById(int id) {
        Optional<batch> batchOpt = batchRepository.findById(id);
        return batchOpt.map(this::convertToDTO);
    }

    // Método para actualizar un batch con validaciones
    public String updateBatch(int id, batchDTO batchDTO) {
        Optional<batch> existingBatchOpt = batchRepository.findById(id);

        if (existingBatchOpt.isEmpty()) {
            return "Batch no encontrado.";
        }

        if (batchDTO.getQuantity() <= 0) {
            return "La cantidad debe ser mayor a 0.";
        }

        Optional<fish> fishOpt = fishRepository.findById(batchDTO.getFishId());
        Optional<tank> tankOpt = tankRepository.findById(batchDTO.getTankId());
        Optional<food> foodOpt = foodRepository.findById(batchDTO.getFoodId());

        if (fishOpt.isEmpty() || tankOpt.isEmpty() || foodOpt.isEmpty()) {
            return "Los identificadores de fish, tank o food no son válidos.";
        }

        batch batchEntity = existingBatchOpt.get();
        batchEntity.setQuantity(batchDTO.getQuantity());
        batchEntity.setFish(fishOpt.get());
        batchEntity.setTank(tankOpt.get());
        batchEntity.setFood(foodOpt.get());

        batchRepository.save(batchEntity);
        return "Batch actualizado exitosamente.";
    }

    // Método para eliminar un batch
    public String deleteBatch(int id) {
        Optional<batch> batchOpt = batchRepository.findById(id);

        if (batchOpt.isEmpty()) {
            return "Batch no encontrado.";
        }

        batchRepository.deleteById(id);
        return "Batch eliminado exitosamente.";
    }

    // Método de filtrado por cantidad mínima
    public List<batchDTO> filterByQuantity(int quantity) {
        List<batch> filteredBatches = batchRepository.findByQuantityGreaterThanEqual(quantity);
        return filteredBatches.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Conversión entre DTO y Entidad
    private batchDTO convertToDTO(batch batchEntity) {
        return new batchDTO(
            batchEntity.getId(),
            batchEntity.getQuantity(),
            batchEntity.getFish().getId(),
            batchEntity.getTank().getId(),
            batchEntity.getFood().getId()
        );
    }

    private batch convertToEntity(batchDTO batchDTO, fish fishEntity, tank tankEntity, food foodEntity) {
        return new batch(
            batchDTO.getId(),
            batchDTO.getQuantity(),
            fishEntity,
            tankEntity,
            foodEntity
        );
    }
}
