package com.sena.crud_basic.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.sena.crud_basic.DTO.employeeDTO;
import com.sena.crud_basic.service.employeeService;

@RestController
@RequestMapping("/api/v1/employee") // Ruta base sin barra final
public class employeeController {

    @Autowired
    private employeeService employeeService;

    // Se usa @PostMapping sin parámetro para que el endpoint sea exactamente "/api/v1/employee"
    @PostMapping
    public ResponseEntity<Object> registerEmployee(@RequestBody employeeDTO employeeDTO) {
        employeeService.save(employeeDTO);
        return new ResponseEntity<>("Employee registered successfully", HttpStatus.OK);
    }
}
