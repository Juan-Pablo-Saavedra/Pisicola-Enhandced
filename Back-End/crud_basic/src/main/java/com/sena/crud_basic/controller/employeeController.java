package com.sena.crud_basic.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sena.crud_basic.DTO.employeeDTO;
import com.sena.crud_basic.DTO.responseDTO;
import com.sena.crud_basic.model.rol;
import com.sena.crud_basic.security.CaptchaValidator;
import com.sena.crud_basic.service.employeeService;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5500")
@RestController
@RequestMapping("/api/v1/employee")
public class employeeController {

    @Autowired
    private employeeService employeeService;

    @Autowired
    private CaptchaValidator captchaValidator;

    // Propiedad para habilitar/deshabilitar reCAPTCHA
    @Value("${recaptcha.enabled:false}")
    private boolean recaptchaEnabled;

    @PostMapping
    public ResponseEntity<Object> registerEmployee(@RequestBody employeeDTO employeeDTO) {
        try {
            System.out.println("📨 Registrando nuevo empleado: " + employeeDTO.getName());
            
            // Validar reCAPTCHA solo si está habilitado
            if (recaptchaEnabled) {
                System.out.println("🔍 Validación reCAPTCHA habilitada");
                
                if (employeeDTO.getToken() == null || employeeDTO.getToken().isEmpty()) {
                    System.out.println("❌ Token reCAPTCHA vacío");
                    return new ResponseEntity<>("❌ Error: Token reCAPTCHA requerido", HttpStatus.BAD_REQUEST);
                }
                
                if (!captchaValidator.validateCaptcha(employeeDTO.getToken())) {
                    System.out.println("❌ Validación reCAPTCHA fallida");
                    return new ResponseEntity<>("❌ Error: Validación reCAPTCHA fallida", HttpStatus.BAD_REQUEST);
                }
                
                System.out.println("✅ reCAPTCHA validado correctamente");
            } else {
                System.out.println("⚠️ Validación reCAPTCHA deshabilitada - Modo desarrollo");
            }
            
            // Guardar el empleado
            responseDTO response = employeeService.save(employeeDTO);
            
            if (response.getStatus().equals(HttpStatus.BAD_REQUEST.toString())) {
                return new ResponseEntity<>(response.getMessage(), HttpStatus.BAD_REQUEST);
            }
            
            System.out.println("✅ Empleado registrado exitosamente: " + employeeDTO.getName());
            return new ResponseEntity<>(response.getMessage(), HttpStatus.OK);
            
        } catch (Exception e) {
            System.err.println("❌ Error en registerEmployee: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>("❌ Error en el servidor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginEmployee(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");
            
            System.out.println("🔐 Intento de login para: " + email);

            responseDTO response = employeeService.login(email, password);
            Map<String, String> responseBody = Map.of("message", response.getMessage());

            if (response.getStatus().equals(HttpStatus.UNAUTHORIZED.toString())) {
                System.out.println("❌ Login fallido para: " + email);
                return new ResponseEntity<>(responseBody, HttpStatus.UNAUTHORIZED);
            }

            System.out.println("✅ Login exitoso para: " + email);
            return new ResponseEntity<>(responseBody, HttpStatus.OK);
            
        } catch (Exception e) {
            System.err.println("❌ Error en login: " + e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(Map.of("message", "Error en el servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping
    public ResponseEntity<List<employeeDTO>> getAllEmployees() {
        try {
            List<employeeDTO> employees = employeeService.getAllEmployees();
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("❌ Error en getAllEmployees: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<employeeDTO> getEmployeeById(@PathVariable int id) {
        try {
            employeeDTO employee = employeeService.getEmployeeById(id);
            return employee != null
                    ? new ResponseEntity<>(employee, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            System.err.println("❌ Error en getEmployeeById: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEmployee(@PathVariable int id, @RequestBody employeeDTO employeeDTO) {
        try {
            responseDTO response = employeeService.updateEmployee(id, employeeDTO);
            return response.getStatus().equals(HttpStatus.BAD_REQUEST.toString())
                    ? new ResponseEntity<>(response.getMessage(), HttpStatus.BAD_REQUEST)
                    : new ResponseEntity<>(response.getMessage(), HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("❌ Error en updateEmployee: " + e.getMessage());
            return new ResponseEntity<>("Error en el servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEmployee(@PathVariable int id) {
        try {
            responseDTO response = employeeService.deleteEmployee(id);
            return response.getStatus().equals(HttpStatus.NOT_FOUND.toString())
                    ? new ResponseEntity<>(response.getMessage(), HttpStatus.NOT_FOUND)
                    : new ResponseEntity<>(response.getMessage(), HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("❌ Error en deleteEmployee: " + e.getMessage());
            return new ResponseEntity<>("Error en el servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter/name")
    public ResponseEntity<List<employeeDTO>> filterByName(@RequestParam String name) {
        try {
            List<employeeDTO> employees = employeeService.filterByName(name);
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("❌ Error en filterByName: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter/phone")
    public ResponseEntity<List<employeeDTO>> filterByPhone(@RequestParam String phone) {
        try {
            List<employeeDTO> employees = employeeService.filterByPhone(phone);
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("❌ Error en filterByPhone: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter/position")
    public ResponseEntity<List<employeeDTO>> filterByPosition(@RequestParam rol position) {
        try {
            List<employeeDTO> employees = employeeService.filterByPosition(position);
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("❌ Error en filterByPosition: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/filter/email")
    public ResponseEntity<List<employeeDTO>> filterByEmail(@RequestParam String email) {
        try {
            List<employeeDTO> employees = employeeService.filterByEmail(email);
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("❌ Error en filterByEmail: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<Object> getRoles() {
        try {
            var roles = rol.values();
            return new ResponseEntity<>(roles, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println("❌ Error en getRoles: " + e.getMessage());
            return new ResponseEntity<>("Error en el servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}