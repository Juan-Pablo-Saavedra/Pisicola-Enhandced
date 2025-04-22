package com.sena.crud_basic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sena.crud_basic.DTO.maintenanceDTO;
import com.sena.crud_basic.model.maintenance;
import com.sena.crud_basic.model.tank;
import com.sena.crud_basic.model.employee;
import com.sena.crud_basic.repository.Imaintenance;
import java.util.Date;
import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class maintenanceService {

    @Autowired
    private Imaintenance maintenanceRepository;

    // Registrar un nuevo mantenimiento
    public String save(maintenanceDTO dto) {
        if (dto.getDate() == null) {
            return "La fecha es obligatoria.";
        }
        if (dto.getDescription() == null || dto.getDescription().trim().isEmpty()) {
            return "La descripción es obligatoria.";
        }
        if (dto.getTankId() <= 0) {
            return "El tanque es obligatorio.";
        }
        if (dto.getEmployeeId() <= 0) {
            return "El empleado es obligatorio.";
        }
        try {
            maintenance m = convertToEntity(dto);
            maintenanceRepository.save(m);
            return "Mantenimiento registrado correctamente.";
        } catch (Exception e) {
            return "Error al registrar mantenimiento: " + e.getMessage();
        }
    }

    // Obtener todos los mantenimientos
    public List<maintenanceDTO> getAllMaintenance() {
        try {
            return maintenanceRepository.findAll()
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener mantenimientos: " + e.getMessage());
        }
    }

    // Obtener mantenimiento por ID
    public Optional<maintenanceDTO> getMaintenanceById(int id) {
        try {
            return maintenanceRepository.findById(id)
                    .map(this::convertToDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener mantenimiento: " + e.getMessage());
        }
    }

    // Actualizar un mantenimiento existente
    public String updateMaintenance(int id, maintenanceDTO dto) {
        Optional<maintenance> opt = maintenanceRepository.findById(id);
        if (opt.isEmpty()) {
            return "Mantenimiento no encontrado.";
        }
        try {
            maintenance m = opt.get();
            m.setdate(dto.getDate());
            m.setdescription(dto.getDescription());
            // Actualizar la relación con Tank usando métodos estándar del entity tank
            tank t = new tank();
            t.setId(dto.getTankId());
            m.settank(t);
            // Actualizar la relación con Employee
            employee emp = new employee();
            emp.setId(dto.getEmployeeId());
            m.setemployee(emp);
            maintenanceRepository.save(m);
            return "Mantenimiento actualizado correctamente.";
        } catch (Exception e) {
            return "Error al actualizar mantenimiento: " + e.getMessage();
        }
    }

    // Eliminar mantenimiento
    public String deleteMaintenance(int id) {
        Optional<maintenance> opt = maintenanceRepository.findById(id);
        if (opt.isEmpty()) {
            return "Mantenimiento no encontrado.";
        }
        try {
            maintenanceRepository.deleteById(id);
            return "Mantenimiento eliminado correctamente.";
        } catch (Exception e) {
            return "Error al eliminar mantenimiento: " + e.getMessage();
        }
    }

    // Filtrar mantenimientos por rango de fechas (opcional)
    public List<maintenanceDTO> filterMaintenance(Date start, Date end) {
        try {
            List<maintenance> list = maintenanceRepository.findAll();
            if (start != null && end != null) {
                list = list.stream()
                        .filter(m -> !m.getdate().before(start) && !m.getdate().after(end))
                        .collect(Collectors.toList());
            }
            return list.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al filtrar mantenimientos: " + e.getMessage());
        }
    }

    // Conversión de entidad a DTO
    private maintenanceDTO convertToDTO(maintenance m) {
        int tId = 0;
        String tName = "";
        if (m.gettank() != null) {
            // Usamos los métodos estándar de tank: getId() y getLocation()
            tId = m.gettank().getId();
            tName = m.gettank().getLocation();
        }
        int empId = 0;
        String empName = "";
        if (m.getemployee() != null) {
            // Usamos los métodos estándar de employee: getId() y getName()
            empId = m.getemployee().getId();
            empName = m.getemployee().getName();
        }
        return new maintenanceDTO(
                m.getid(),
                m.getdate(),
                m.getdescription(),
                tId,
                tName,
                empId,
                empName
        );
    }

    // Conversión de DTO a entidad
    private maintenance convertToEntity(maintenanceDTO dto) {
        tank t = new tank();
        t.setId(dto.getTankId());
        employee emp = new employee();
        emp.setId(dto.getEmployeeId());
        return new maintenance(dto.getId(), dto.getDate(), dto.getDescription(), t, emp);
    }
}
