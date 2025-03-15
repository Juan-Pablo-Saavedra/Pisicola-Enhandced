package com.sena.crud_basic.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.sena.crud_basic.DTO.fishDTO;
import com.sena.crud_basic.service.fishService;

@RestController
@RequestMapping("/api/v1/fish")
public class fishController {

    @Autowired
    private fishService fishService;

    // Endpoint para registrar una nueva entidad fish
    @PostMapping
    public ResponseEntity<Object> registerFish(@RequestBody fishDTO fishDTO) {
        fishService.save(fishDTO);
        return new ResponseEntity<>("Fish registered successfully", HttpStatus.OK);
    }
}
