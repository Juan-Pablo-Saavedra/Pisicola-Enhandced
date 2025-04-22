package com.sena.crud_basic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.sena.crud_basic.DTO.maintenanceDTO;
import com.sena.crud_basic.service.maintenanceService;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/maintenance")
@CrossOrigin(origins = "*")
public class maintenanceController {

    @Autowired
    private maintenanceService maintenanceService;

    // Obtener todos los mantenimientos
    @GetMapping
    public List<maintenanceDTO> getAllMaintenance() {
        return maintenanceService.getAllMaintenance();
    }

    // Obtener un mantenimiento por ID
    @GetMapping("/{id}")
    public Optional<maintenanceDTO> getMaintenanceById(@PathVariable int id) {
        return maintenanceService.getMaintenanceById(id);
    }

    // Registrar un nuevo mantenimiento
    @PostMapping
    public String createMaintenance(@RequestBody maintenanceDTO maintenanceDTO) {
        return maintenanceService.save(maintenanceDTO);
    }

    // Actualizar un mantenimiento existente
    @PutMapping("/{id}")
    public String updateMaintenance(@PathVariable int id, @RequestBody maintenanceDTO maintenanceDTO) {
        return maintenanceService.updateMaintenance(id, maintenanceDTO);
    }

    // Eliminar un mantenimiento
    @DeleteMapping("/{id}")
    public String deleteMaintenance(@PathVariable int id) {
        return maintenanceService.deleteMaintenance(id);
    }

    // Filtrar mantenimientos por rango de fechas (parámetros opcionales)
    // Se espera que la URL tenga formato: ?start=yyyy-MM-dd&end=yyyy-MM-dd
    @GetMapping("/filter")
    public List<maintenanceDTO> filterMaintenance(
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date start,
            @RequestParam(required = false) @DateTimeFormat(pattern="yyyy-MM-dd") Date end) {
        return maintenanceService.filterMaintenance(start, end);
    }
}
