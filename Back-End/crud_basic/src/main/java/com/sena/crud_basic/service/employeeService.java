package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.employeeDTO;
import com.sena.crud_basic.model.employee;
import com.sena.crud_basic.DTO.responseDTO;
import com.sena.crud_basic.repository.Iemployee;
import org.springframework.http.HttpStatus;
import java.util.Optional;

@Service
public class employeeService {

    @Autowired
    private Iemployee data;

public Optional <employee> findByEmail(String email) {
        return data.findByEmail(email);
    }

    // Método invocado por el controller para guardar la entidad Employee.
    public responseDTO save(employeeDTO employeeDTO) {
        // Validar si el correo ya existe
        Optional<employee> existingEmployee = data.findByEmail(employeeDTO.getEmail());
        if (existingEmployee.isPresent()) {
            return new responseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "El correo ya está registrado"
            );
        }
        
        // Validar la longitud del nombre antes de proceder
        if (employeeDTO.getName().length() < 1 || employeeDTO.getName().length() > 100) {
            return new responseDTO(
                HttpStatus.BAD_REQUEST.toString(),
                "El nombre completo tiene que ser menor de 100 caracteres"
            );
        }
        
        // Si la validación es exitosa, guardar la entidad
        employee employeeEntity = convertToEntity(employeeDTO);
        data.save(employeeEntity);
        return new responseDTO(
            HttpStatus.OK.toString(),
            "Empleado guardado exitosamente"
        );
    }

    // Convierte la entidad a DTO.
    public employeeDTO convertToDTO(employee employeeEntity) {
        return new employeeDTO(
            employeeEntity.getName(),
            employeeEntity.getPosition(),
            employeeEntity.getPhone(),
            employeeEntity.getPassword(),
            employeeEntity.getEmail()
        );
    }

    // Convierte el DTO a entidad.
    public employee convertToEntity(employeeDTO employeeDTO) {
        return new employee(
            0, // ID se asigna automáticamente por la base de datos
            employeeDTO.getName(),
            employeeDTO.getPosition(),
            employeeDTO.getPhone(),
            employeeDTO.getPassword(),
            employeeDTO.getEmail()
        );
    }
}
