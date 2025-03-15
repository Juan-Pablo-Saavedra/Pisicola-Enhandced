package com.sena.crud_basic.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.sena.crud_basic.DTO.maintenanceDTO;
import com.sena.crud_basic.service.maintenanceService;

@RestController
@RequestMapping("/api/v1/maintenance")
public class maintenanceController {

    @Autowired
    private maintenanceService maintenanceService;

    // Endpoint para registrar una nueva entidad maintenance
    @PostMapping
    public ResponseEntity<Object> registerMaintenance(@RequestBody maintenanceDTO maintenanceDTO) {
        maintenanceService.save(maintenanceDTO);
        return new ResponseEntity<>("Maintenance record registered successfully", HttpStatus.OK);
    }
}
