package com.sena.crud_basic.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import com.sena.crud_basic.DTO.foodDTO;
import com.sena.crud_basic.service.foodService;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5500")
@RestController
@RequestMapping("/api/v1/food")
public class foodController {

    @Autowired
    private foodService foodService;

    @PostMapping
    public ResponseEntity<String> saveFood(@RequestBody foodDTO foodDTO) {
        try {
            String response = foodService.save(foodDTO);
            HttpStatus status = response.equals("Alimento guardado exitosamente.") ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(response, status);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al guardar alimento: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<Object> getAllFoods() {
        try {
            List<foodDTO> foods = foodService.getAllFoods();
            return foods.isEmpty()
                ? new ResponseEntity<>("No hay alimentos registrados.", HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(foods, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al obtener alimentos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getFoodById(@PathVariable int id) {
        try {
            Optional<foodDTO> foodOpt = foodService.getFoodById(id);
            return foodOpt.isPresent()
                ? ResponseEntity.ok(foodOpt.get())
                : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Alimento no encontrado.");
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al obtener el alimento: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateFood(@PathVariable int id, @RequestBody foodDTO foodDTO) {
        try {
            String response = foodService.updateFood(id, foodDTO);
            HttpStatus status = response.equals("Alimento actualizado exitosamente.") ? HttpStatus.OK : HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(response, status);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al actualizar alimento: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFood(@PathVariable int id) {
        try {
            String response = foodService.deleteFood(id);
            HttpStatus status = response.equals("Alimento eliminado exitosamente.") ? HttpStatus.OK : HttpStatus.NOT_FOUND;
            return new ResponseEntity<>(response, status);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al eliminar alimento: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<Object> filterFoods(
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String brand
    ) {
        try {
            List<foodDTO> filteredFoods = foodService.filterFoods(type, brand);
            return filteredFoods.isEmpty()
                ? new ResponseEntity<>("No hay alimentos que coincidan con el filtro.", HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(filteredFoods, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al filtrar alimentos: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
