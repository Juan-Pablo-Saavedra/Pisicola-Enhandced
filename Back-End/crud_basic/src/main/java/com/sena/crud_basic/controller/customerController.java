package com.sena.crud_basic.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sena.crud_basic.DTO.customerDTO;
import com.sena.crud_basic.service.customerService;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5500")
@RestController
@RequestMapping("/api/v1/customer")
public class customerController {

    @Autowired
    private customerService customerService;

    // Registrar un nuevo cliente con validaciones
    @PostMapping
    public ResponseEntity<String> registerCustomer(@RequestBody customerDTO customerDTO) {
        String response = customerService.save(customerDTO);
        HttpStatus status = response.equals("Cliente guardado exitosamente.") ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    // Obtener todos los clientes
    @GetMapping
    public ResponseEntity<List<customerDTO>> getAllCustomers() {
        List<customerDTO> customers = customerService.getAllCustomers();
        return customers.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
public ResponseEntity<Object> getCustomerById(@PathVariable int id) {
    Optional<customerDTO> customer = customerService.getCustomerById(id);
    return customer.isPresent()
            ? ResponseEntity.ok(customer.get())
            : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado.");
}

    // Actualizar un cliente con validaciones
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCustomer(@PathVariable int id, @RequestBody customerDTO customerDTO) {
        String response = customerService.updateCustomer(id, customerDTO);
        HttpStatus status = response.equals("Cliente actualizado exitosamente.") ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

    // Eliminar un cliente con control de errores
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable int id) {
        String response = customerService.deleteCustomer(id);
        HttpStatus status = response.equals("Cliente eliminado exitosamente.") ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return new ResponseEntity<>(response, status);
    }

    // Filtro general por nombre, teléfono o correo con manejo de excepciones
    @GetMapping("/filter")
    public ResponseEntity<List<customerDTO>> filterCustomers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email) {
        List<customerDTO> customers = customerService.filterCustomers(name, phone, email);
        return customers.isEmpty()
                ? new ResponseEntity<>(HttpStatus.NO_CONTENT)
                : new ResponseEntity<>(customers, HttpStatus.OK);
    }
}
