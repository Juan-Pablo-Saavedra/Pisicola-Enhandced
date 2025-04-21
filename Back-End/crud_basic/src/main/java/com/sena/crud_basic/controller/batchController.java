package com.sena.crud_basic.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sena.crud_basic.DTO.batchDTO;
import com.sena.crud_basic.service.batchService;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5500")
@RestController
@RequestMapping("/api/v1/batch")
public class batchController {

    @Autowired
    private batchService batchService;

    // Registrar un nuevo batch con validaciones
    @PostMapping
    public ResponseEntity<String> registerBatch(@RequestBody batchDTO batchDTO) {
        String response = batchService.save(batchDTO);
        HttpStatus status = response.equals("Batch guardado exitosamente.") ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    // Obtener todos los batch
    @GetMapping
    public ResponseEntity<List<batchDTO>> getAllBatch() {
        List<batchDTO> batches = batchService.getAllBatch();
        return batches.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(batches, HttpStatus.OK);
    }

    // Obtener batch por ID con manejo de errores
    @GetMapping("/{id}")
    public ResponseEntity<Object> getBatchById(@PathVariable int id) {
        Optional<batchDTO> batch = batchService.getBatchById(id);
        return batch.isPresent()
                ? ResponseEntity.ok(batch.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Batch no encontrado.");
    }

    // Actualizar un batch con validaciones
    @PutMapping("/{id}")
    public ResponseEntity<String> updateBatch(@PathVariable int id, @RequestBody batchDTO batchDTO) {
        String response = batchService.updateBatch(id, batchDTO);
        HttpStatus status = response.equals("Batch actualizado exitosamente.") ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    // Eliminar un batch con control de errores
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBatch(@PathVariable int id) {
        String response = batchService.deleteBatch(id);
        HttpStatus status = response.equals("Batch eliminado exitosamente.") ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(response, status);
    }

    // Filtrar batch por cantidad mínima
    @GetMapping("/filter")
    public ResponseEntity<List<batchDTO>> filterByQuantity(@RequestParam int quantity) {
        List<batchDTO> batches = batchService.filterByQuantity(quantity);
        return batches.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(batches, HttpStatus.OK);
    }
}
