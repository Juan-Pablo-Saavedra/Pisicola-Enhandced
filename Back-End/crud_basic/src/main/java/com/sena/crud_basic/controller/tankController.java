package com.sena.crud_basic.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.sena.crud_basic.DTO.tankDTO;
import com.sena.crud_basic.service.tankService;

@RestController
@RequestMapping("/api/v1/tank")
public class tankController {

    @Autowired
    private tankService tankService;

    // Endpoint para registrar un nuevo tank
    @PostMapping
    public ResponseEntity<Object> registerTank(@RequestBody tankDTO tankDTO) {
        tankService.save(tankDTO);
        return new ResponseEntity<>("Tank registered successfully", HttpStatus.OK);
    }
}
