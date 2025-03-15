package com.sena.crud_basic.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.sena.crud_basic.DTO.supplier_employeeDTO;
import com.sena.crud_basic.service.supplier_employeeService;

@RestController
@RequestMapping("/api/v1/supplier_employee")
public class supplier_employeeController {

    @Autowired
    private supplier_employeeService supplierEmployeeService;

    // Endpoint para registrar un nuevo supplier_employee
    @PostMapping
    public ResponseEntity<Object> registerSupplierEmployee(@RequestBody supplier_employeeDTO dto) {
        supplierEmployeeService.save(dto);
        return new ResponseEntity<>("supplier_employee registered successfully", HttpStatus.OK);
    }
}
