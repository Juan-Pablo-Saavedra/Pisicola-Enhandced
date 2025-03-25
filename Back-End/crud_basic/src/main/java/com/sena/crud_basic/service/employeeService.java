package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.employeeDTO;
import com.sena.crud_basic.model.employee;
import com.sena.crud_basic.DTO.responseDTO;
import com.sena.crud_basic.repository.Iemployee;
import org.springframework.http.HttpStatus;

@Service
public class employeeService {

    @Autowired
    private Iemployee data;

    // Método invocado por el controller para guardar la entidad employee.
    public responseDTO save(employeeDTO employeeDTO) {
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
            employeeEntity.getId(),
            employeeEntity.getName(),
            employeeEntity.getPosition(),
            employeeEntity.getPhone()
        );
    }

    // Convierte el DTO a entidad.
    public employee convertToEntity(employeeDTO employeeDTO) {
        return new employee(
            employeeDTO.getId(),
            employeeDTO.getName(),
            employeeDTO.getPosition(),
            employeeDTO.getPhone()
        );
    }
}