package com.sena.crud_basic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.sena.crud_basic.DTO.tankDTO;
import com.sena.crud_basic.service.tankService;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/tank")
@CrossOrigin(origins = "*")  // Permite peticiones de cualquier origen si es necesario.
public class tankController {

    @Autowired
    private tankService tankService;

    // Obtener todos los tanks
    @GetMapping
    public List<tankDTO> getAllTanks() {
        return tankService.getAllTanks();
    }

    // Obtener un tank por id
    @GetMapping("/{id}")
    public Optional<tankDTO> getTankById(@PathVariable int id) {
        return tankService.getTankById(id);
    }

    // Crear un nuevo tank
    @PostMapping
    public String createTank(@RequestBody tankDTO tankDTO) {
        return tankService.save(tankDTO);
    }

    // Actualizar un tank existente
    @PutMapping("/{id}")
    public String updateTank(@PathVariable int id, @RequestBody tankDTO tankDTO) {
        return tankService.updateTank(id, tankDTO);
    }

    // Eliminar un tank
    @DeleteMapping("/{id}")
    public String deleteTank(@PathVariable int id) {
        return tankService.deleteTank(id);
    }

    // Filtrar tanks. Los parámetros son opcionales.
    // Ejemplo: /api/v1/tank/filter?location=Neiva&waterType=Freshwater
    @GetMapping("/filter")
    public List<tankDTO> filterTanks(@RequestParam(required = false) String location,
                                     @RequestParam(required = false) String waterType) {
        return tankService.filterTanks(location, waterType);
    }
}
