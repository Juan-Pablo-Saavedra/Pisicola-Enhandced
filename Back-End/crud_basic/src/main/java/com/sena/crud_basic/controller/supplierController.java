package com.sena.crud_basic.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.sena.crud_basic.DTO.supplierDTO;
import com.sena.crud_basic.service.supplierService;

@RestController
@RequestMapping("/api/v1/supplier")
public class supplierController {

    @Autowired
    private supplierService supplierService;

    // Endpoint para registrar un nuevo supplier
    @PostMapping
    public ResponseEntity<Object> registerSupplier(@RequestBody supplierDTO supplierDTO) {
        supplierService.save(supplierDTO);
        return new ResponseEntity<>("Supplier registered successfully", HttpStatus.OK);
    }
}
