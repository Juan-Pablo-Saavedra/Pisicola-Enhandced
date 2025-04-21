package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.supplier_employeeDTO;
import com.sena.crud_basic.model.supplier;
import com.sena.crud_basic.model.employee;
import com.sena.crud_basic.model.supplier_employee;
import com.sena.crud_basic.repository.Isupplier_employee;
import com.sena.crud_basic.repository.Iemployee;
import com.sena.crud_basic.repository.Isupplier;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class supplier_employeeService {

    @Autowired
    private Isupplier_employee supplierEmployeeRepository;

    @Autowired
    private Iemployee employeeRepository;

    @Autowired
    private Isupplier supplierRepository;

    // Método para guardar una relación entre empleado y proveedor
    public String save(supplier_employeeDTO dto) {
        if (dto.getSupplier() == null || dto.getEmployee() == null) {
            return "Proveedor y empleado son requeridos.";
        }

        Optional<supplier> supplierOpt = supplierRepository.findById(dto.getSupplier().getId());
        Optional<employee> employeeOpt = employeeRepository.findById(dto.getEmployee().getId());

        if (supplierOpt.isEmpty()) {
            return "Proveedor no encontrado.";
        }

        if (employeeOpt.isEmpty()) {
            return "Empleado no encontrado.";
        }

        supplier_employee entity = convertToEntity(dto);
        supplierEmployeeRepository.save(entity);
        return "Relación entre proveedor y empleado guardada exitosamente.";
    }

    // Obtener todas las relaciones proveedor-empleado
    public List<supplier_employeeDTO> getAllRelations() {
        List<supplier_employee> relations = supplierEmployeeRepository.findAll();
        return relations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener relaciones por empleado
    public List<supplier_employeeDTO> getRelationsByEmployee(int employeeId) {
        List<supplier_employee> relations = supplierEmployeeRepository.findByEmployeeId(employeeId);
        return relations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener relaciones por proveedor
    public List<supplier_employeeDTO> getRelationsBySupplier(int supplierId) {
        List<supplier_employee> relations = supplierEmployeeRepository.findBySupplierId(supplierId);
        return relations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Método para eliminar una relación
    public String deleteRelation(int id) {
        Optional<supplier_employee> relationOpt = supplierEmployeeRepository.findById(id);
        
        if (relationOpt.isEmpty()) {
            return "Relación no encontrada.";
        }
        
        supplierEmployeeRepository.deleteById(id);
        return "Relación eliminada exitosamente.";
    }

    // Convierte la entidad supplier_employee a DTO
    public supplier_employeeDTO convertToDTO(supplier_employee entity) {
        return new supplier_employeeDTO(
            entity.getId(),        // Corregido el método getId()
            entity.getSupplier(),  // obtiene la instancia de supplier
            entity.getEmployee()   // obtiene la instancia de employee
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
