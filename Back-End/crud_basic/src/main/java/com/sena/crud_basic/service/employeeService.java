package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.http.*;

import com.sena.crud_basic.DTO.RequestLoginDTO;
import com.sena.crud_basic.DTO.employeeDTO;
import com.sena.crud_basic.model.employee;
import com.sena.crud_basic.model.rol;
import com.sena.crud_basic.DTO.responseDTO;
import com.sena.crud_basic.DTO.responseLogin;
import com.sena.crud_basic.repository.Iemployee;
import com.sena.crud_basic.service.jwt.jwtServices;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class employeeService {

    @Autowired
    private Iemployee data;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private jwtServices jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Guardar un empleado
    public responseDTO save(employeeDTO employeeDTO) {
        if (employeeDTO == null) {
            return new responseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "Los datos del empleado son inválidos"
            );
        }

        Optional<employee> existingEmployee = data.findByEmail(employeeDTO.getEmail());
        if (existingEmployee.isPresent()) {
            return new responseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "❌ El correo ya está registrado"
            );
        }

        if (employeeDTO.getName() == null || employeeDTO.getName().trim().isEmpty() ||
            employeeDTO.getName().length() > 100) {
            return new responseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "❌ El nombre completo debe tener entre 1 y 100 caracteres"
            );
        }

        employee employeeEntity = convertToEntity(employeeDTO);
        
        // Encriptar la contraseña antes de guardar
        if (employeeDTO.getPassword() != null && !employeeDTO.getPassword().isEmpty()) {
            employeeEntity.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        }
        
        data.save(employeeEntity);
        return new responseDTO(
            HttpStatus.OK.toString(),
            "✅ Empleado guardado exitosamente"
        );
    }

    // Método de login corregido
    public responseLogin login(RequestLoginDTO login) {
        try {
            // Verificar si el usuario existe
            Optional<employee> employeeOpt = data.findByEmail(login.getEmail());
            if (employeeOpt.isEmpty()) {
                return new responseLogin(
                    null,
                    "❌ Usuario no encontrado"
                );
            }

            // Autenticar al usuario
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    login.getEmail(),
                    login.getPassword()
                )
            );

            // Crear UserDetails manualmente
            employee emp = employeeOpt.get();
            org.springframework.security.core.userdetails.User user = 
                new org.springframework.security.core.userdetails.User(
                    emp.getEmail(),
                    emp.getPassword(),
                    Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"))
                );
            
            // Generar el token
            String token = jwtService.getToken(user);
            
            // Verificar que el token se generó correctamente
            if (token == null || token.isEmpty()) {
                return new responseLogin(
                    null,
                    "❌ Error al generar token"
                );
            }
            
            // Retornar respuesta exitosa con token
            return new responseLogin(
                token,
                "✅ Inicio de sesión exitoso"
            );
            
        } catch (Exception e) {
            // En caso de error de autenticación
            System.out.println("Error en login: " + e.getMessage());
            return new responseLogin(
                null,
                "❌ Credenciales inválidas"
            );
        }
    }

    // Listar todos los empleados
    public List<employeeDTO> getAllEmployees() {
        return data.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    // Obtener un empleado por ID
    public employeeDTO getEmployeeById(int id) {
        return data.findById(id)
            .map(this::convertToDTO)
            .orElse(null);
    }

    // Actualizar un empleado
    public responseDTO updateEmployee(int id, employeeDTO employeeDTO) {
        Optional<employee> existingEmployee = data.findById(id);

        if (existingEmployee.isEmpty()) {
            return new responseDTO(
                HttpStatus.NOT_FOUND.toString(),
                "❌ Empleado no encontrado"
            );
        }

        employee employeeEntity = existingEmployee.get();
        employeeEntity.setName(employeeDTO.getName());
        employeeEntity.setPosition(employeeDTO.getPosition());
        employeeEntity.setPhone(employeeDTO.getPhone());
        
        // Encriptar la contraseña si se proporciona una nueva
        if (employeeDTO.getPassword() != null && !employeeDTO.getPassword().isEmpty()) {
            employeeEntity.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
        }
        
        employeeEntity.setEmail(employeeDTO.getEmail());

        data.save(employeeEntity);
        return new responseDTO(
            HttpStatus.OK.toString(),
            "✅ Empleado actualizado exitosamente"
        );
    }

    // Eliminar un empleado
    public responseDTO deleteEmployee(int id) {
        Optional<employee> employee = data.findById(id);

        if (employee.isEmpty()) {
            return new responseDTO(
                HttpStatus.NOT_FOUND.toString(),
                "❌ Empleado no encontrado"
            );
        }

        data.deleteById(id);
        return new responseDTO(
            HttpStatus.OK.toString(),
            "✅ Empleado eliminado exitosamente"
        );
    }

    // Filtros
    public List<employeeDTO> filterByName(String name) {
        return data.findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<employeeDTO> filterByPhone(String phone) {
        return data.findByPhoneContainingIgnoreCase(phone)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<employeeDTO> filterByPosition(rol position) {
        return data.findByPosition(position)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<employeeDTO> filterByEmail(String email) {
        return data.findByEmailContainingIgnoreCase(email)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Conversores
    public employeeDTO convertToDTO(employee employeeEntity) {
        return new employeeDTO(
            employeeEntity.getId(),
            employeeEntity.getName(),
            employeeEntity.getPosition(),
            employeeEntity.getPhone(),
            employeeEntity.getPassword(),
            employeeEntity.getEmail(),
            null
        );
    }

    public employee convertToEntity(employeeDTO employeeDTO) {
        return new employee(
            employeeDTO.getId() != null ? employeeDTO.getId() : 0,
            employeeDTO.getName(),
            employeeDTO.getPosition(),
            employeeDTO.getPhone(),
            employeeDTO.getPassword(),
            employeeDTO.getEmail()
        );
    }
}