package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.employeeDTO;
import com.sena.crud_basic.model.employee;
import com.sena.crud_basic.repository.Iemployee;

@Service
public class employeeService {

    @Autowired
    private Iemployee data;

    // Método invocado por el controller para guardar la entidad employee.
    public void save(employeeDTO employeeDTO) {
        employee employeeEntity = convertToEntity(employeeDTO);
        data.save(employeeEntity);
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
