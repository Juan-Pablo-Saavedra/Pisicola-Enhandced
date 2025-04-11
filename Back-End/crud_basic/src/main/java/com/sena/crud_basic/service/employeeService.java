package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.employeeDTO;
import com.sena.crud_basic.model.employee;
import com.sena.crud_basic.model.rol;
import com.sena.crud_basic.DTO.responseDTO;
import com.sena.crud_basic.repository.Iemployee;
import org.springframework.http.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class employeeService {

    @Autowired
    private Iemployee data;

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
        data.save(employeeEntity);
        return new responseDTO(
            HttpStatus.OK.toString(),
            "✅ Empleado guardado exitosamente"
        );
    }

    // Iniciar sesión
    public responseDTO login(String email, String password) {
        Optional<employee> employee = data.findByEmail(email);

        if (employee.isEmpty()) {
            return new responseDTO(
                HttpStatus.UNAUTHORIZED.toString(),
                "❌ Email no registrado"
            );
        }

        if (!employee.get().getPassword().equals(password)) {
            return new responseDTO(
                HttpStatus.UNAUTHORIZED.toString(),
                "❌ Contraseña incorrecta"
            );
        }

        return new responseDTO(
            HttpStatus.OK.toString(),
            "✅ Inicio de sesión exitoso"
        );
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
        employeeEntity.setPassword(employeeDTO.getPassword());
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