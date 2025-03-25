package com.sena.crud_basic.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sena.crud_basic.DTO.batchDTO;
import com.sena.crud_basic.service.batchService;

@RestController
@RequestMapping("/api/v1/batch")
public class batchController {
    @Autowired
    private batchService batchService;

    @PostMapping
    public ResponseEntity<Object> registerBatch(@RequestBody batchDTO batchDTO) {
        batchService.createBatch(batchDTO);
        return new ResponseEntity<>("Registro ok", HttpStatus.OK);
    }
}
