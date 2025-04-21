package com.sena.crud_basic.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sena.crud_basic.DTO.fishDTO;
import com.sena.crud_basic.service.fishService;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5500")
@RestController
@RequestMapping("/api/v1/fish")
public class fishController {

    @Autowired
    private fishService fishService;

    // Endpoint para registrar un nuevo pez con validaciones
    @PostMapping
    public ResponseEntity<String> registerFish(@RequestBody fishDTO fishDTO) {
        String response = fishService.save(fishDTO);
        HttpStatus status = response.equals("Pez guardado exitosamente.") ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    // Endpoint para obtener todos los peces
    @GetMapping
    public ResponseEntity<List<fishDTO>> getAllFish() {
        List<fishDTO> fishes = fishService.getAllFish();
        return fishes.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(fishes, HttpStatus.OK);
    }

    // Endpoint para obtener un pez por ID con manejo de errores
    @GetMapping("/{id}")
    public ResponseEntity<Object> getFishById(@PathVariable int id) {
        Optional<fishDTO> fish = fishService.getFishById(id);
        return fish.isPresent()
                ? ResponseEntity.ok(fish.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pez no encontrado.");
    }

    // Endpoint para actualizar un pez con validaciones
    @PutMapping("/{id}")
    public ResponseEntity<String> updateFish(@PathVariable int id, @RequestBody fishDTO fishDTO) {
        String response = fishService.updateFish(id, fishDTO);
        HttpStatus status = response.equals("Pez actualizado exitosamente.") ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    // Endpoint para eliminar un pez con control de errores
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFish(@PathVariable int id) {
        String response = fishService.deleteFish(id);
        HttpStatus status = response.equals("Pez eliminado exitosamente.") ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(response, status);
    }

    // Nuevo Endpoint para filtrar peces por especie y peso mínimo
    @GetMapping("/filter")
    public ResponseEntity<List<fishDTO>> filterFish(
            @RequestParam(required = false) String species,
            @RequestParam(required = false) Float weight) {

        List<fishDTO> filteredFish = fishService.filterFish(species, weight);

        return filteredFish.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(filteredFish, HttpStatus.OK);
    }

}
