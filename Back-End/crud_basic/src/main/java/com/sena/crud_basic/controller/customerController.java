package com.sena.crud_basic.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sena.crud_basic.DTO.customerDTO;
import com.sena.crud_basic.service.customerService;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5500") // Permitir solicitudes desde el frontend
@RestController
@RequestMapping("/api/v1/customer")
public class customerController {

    @Autowired
    private customerService customerService;

    // Registrar un cliente
    @PostMapping
    public ResponseEntity<String> registerCustomer(@RequestBody customerDTO customerDTO) {
        String message = customerService.save(customerDTO);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    // Listar todos los clientes
    @GetMapping
    public ResponseEntity<List<customerDTO>> getAllCustomers() {
        List<customerDTO> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    // Obtener un cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<customerDTO> getCustomerById(@PathVariable int id) {
        customerDTO customer = customerService.getCustomerById(id);
        if (customer == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    // Actualizar un cliente
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable int id, @RequestBody customerDTO customerDTO) {
        String message = customerService.updateCustomer(id, customerDTO);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // Eliminar un cliente
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable int id) {
        String message = customerService.deleteCustomer(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }
}