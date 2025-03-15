package com.sena.crud_basic.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.sena.crud_basic.DTO.saleDTO;
import com.sena.crud_basic.service.saleService;

@RestController
@RequestMapping("/api/v1/sale")
public class saleController {

    @Autowired
    private saleService saleService;

    // Endpoint para registrar una nueva entidad sale
    @PostMapping
    public ResponseEntity<Object> registerSale(@RequestBody saleDTO saleDTO) {
        saleService.save(saleDTO);
        return new ResponseEntity<>("Sale registered successfully", HttpStatus.OK);
    }
}
