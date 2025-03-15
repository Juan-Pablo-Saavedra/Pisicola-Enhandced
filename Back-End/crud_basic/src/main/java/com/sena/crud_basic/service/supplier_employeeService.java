package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.supplier_employeeDTO;
import com.sena.crud_basic.model.supplier_employee;
import com.sena.crud_basic.repository.Isupplier_employee;

@Service
public class supplier_employeeService {

    @Autowired
    private Isupplier_employee supplierEmployeeRepository;

    // Método para guardar un supplier_employee a partir del DTO
    public void save(supplier_employeeDTO dto) {
        supplier_employee entity = convertToEntity(dto);
        supplierEmployeeRepository.save(entity);
    }

    // Convierte la entidad supplier_employee a DTO
    public supplier_employeeDTO convertToDTO(supplier_employee entity) {
        return new supplier_employeeDTO(
            entity.getid(),         // método definido en la entidad (getid)
            entity.getsupplier(),   // obtiene la instancia de supplier
            entity.getemployee()    // obtiene la instancia de employee
        );
    }

    // Convierte el DTO a la entidad supplier_employee
    public supplier_employee convertToEntity(supplier_employeeDTO dto) {
        return new supplier_employee(
            dto.getId(),
            dto.getSupplier(),
            dto.getEmployee()
        );
    }
}
