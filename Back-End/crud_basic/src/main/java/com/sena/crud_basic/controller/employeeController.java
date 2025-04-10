package com.sena.crud_basic.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sena.crud_basic.DTO.employeeDTO;
import com.sena.crud_basic.service.employeeService;
import com.sena.crud_basic.DTO.responseDTO;
import com.sena.crud_basic.model.rol;


import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5500") // Permite solicitudes del frontend
@RestController
@RequestMapping("/api/v1/employee")
public class employeeController {

    @Autowired
    private employeeService employeeService;

    // Registrar un empleado
    @PostMapping
    public ResponseEntity<Object> registerEmployee(@RequestBody employeeDTO employeeDTO) {
        responseDTO response = employeeService.save(employeeDTO);

        if (response.getStatus().equals(HttpStatus.BAD_REQUEST.toString())) {
            return new ResponseEntity<>(response.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response.getMessage(), HttpStatus.OK);
    }

    // Login de empleado
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginEmployee(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        responseDTO response = employeeService.login(email, password);

        Map<String, String> responseBody = Map.of("message", response.getMessage());

        if (response.getStatus().equals(HttpStatus.UNAUTHORIZED.toString())) {
            return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }

    // Listar todos los empleados
    @GetMapping
    public ResponseEntity<List<employeeDTO>> getAllEmployees() {
        List<employeeDTO> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    // Obtener un empleado por ID
    @GetMapping("/{id}")
    public ResponseEntity<employeeDTO> getEmployeeById(@PathVariable int id) {
        employeeDTO employee = employeeService.getEmployeeById(id);

        if (employee == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    // Actualizar un empleado
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEmployee(@PathVariable int id, @RequestBody employeeDTO employeeDTO) {
        responseDTO response = employeeService.updateEmployee(id, employeeDTO);

        if (response.getStatus().equals(HttpStatus.BAD_REQUEST.toString())) {
            return new ResponseEntity<>(response.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(response.getMessage(), HttpStatus.OK);
    }

    // Eliminar un empleado
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable int id) {
        responseDTO response = employeeService.deleteEmployee(id);

        if (response.getStatus().equals(HttpStatus.NOT_FOUND.toString())) {
            return new ResponseEntity<>(response.getMessage(), HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(response.getMessage(), HttpStatus.OK);
    }

    // Filtro por nombre
    @GetMapping("/filter/name")
    public ResponseEntity<List<employeeDTO>> filterByName(@RequestParam String name) {
        List<employeeDTO> employees = employeeService.filterByName(name);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    // Filtro por teléfono
    @GetMapping("/filter/phone")
    public ResponseEntity<List<employeeDTO>> filterByPhone(@RequestParam String phone) {
        List<employeeDTO> employees = employeeService.filterByPhone(phone);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    // Filtro por cargo (posición)
    @GetMapping("/filter/position")
    public ResponseEntity<List<employeeDTO>> filterByPosition(@RequestParam rol position) {
        List<employeeDTO> employees = employeeService.filterByPosition(position);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    // Filtro por correo electrónico
    @GetMapping("/filter/email")
    public ResponseEntity<List<employeeDTO>> filterByEmail(@RequestParam String email) {
        List<employeeDTO> employees = employeeService.filterByEmail(email);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    // Obtener roles
    @GetMapping("/roles")
    public ResponseEntity<Object> getRoles() {
        var roles =rol.values();
        return new ResponseEntity<>(roles, HttpStatus.OK);
    }

    
}
 