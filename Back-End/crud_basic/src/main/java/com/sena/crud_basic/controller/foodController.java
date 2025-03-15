package com.sena.crud_basic.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.sena.crud_basic.DTO.foodDTO;
import com.sena.crud_basic.service.foodService;

@RestController
@RequestMapping("/api/v1/food")
public class foodController {

    @Autowired
    private foodService foodService;

    // Endpoint para registrar un nuevo food
    @PostMapping
    public ResponseEntity<Object> registerFood(@RequestBody foodDTO foodDTO) {
        foodService.save(foodDTO);
        return new ResponseEntity<>("Food registered successfully", HttpStatus.OK);
    }
}
