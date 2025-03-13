package com.sena.crud_basic.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sena.crud_basic.DTO.customerDTO;
import com.sena.crud_basic.service.customerService;

@RestController
@RequestMapping("api/v1/customer")
public class customerController {

    @Autowired
    private customerService customerService;

    @PostMapping("/")
    public ResponseEntity<Object> registerCustomer(@RequestBody customerDTO customerDTO) {
        customerService.save(customerDTO);
        return new ResponseEntity<>("Registor ok",HttpStatus.OK);
    }
} 
