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
import com.sena.crud_basic.DTO.responseDTO;

@RestController
@RequestMapping("/api/v1/employee") // Ruta base sin barra final
public class employeeController {

    @Autowired
    private employeeService employeeService;

    @PostMapping
    public ResponseEntity<Object> registerEmployee(@RequestBody employeeDTO employeeDTO) {
        responseDTO response = employeeService.save(employeeDTO);
        
        if (response.getStatus().equals(HttpStatus.BAD_REQUEST.toString())) {
            return new ResponseEntity<>(response.getMessage(), HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(response.getMessage(), HttpStatus.OK);
    }
}
