package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.maintenanceDTO;
import com.sena.crud_basic.model.maintenance;
import com.sena.crud_basic.repository.Imaintenance;

@Service
public class maintenanceService {

    @Autowired
    private Imaintenance maintenanceRepository;

    // Método para guardar una nueva entidad maintenance a partir del DTO
    public void save(maintenanceDTO maintenanceDTO) {
        maintenance maintenanceEntity = convertToEntity(maintenanceDTO);
        maintenanceRepository.save(maintenanceEntity);
    }

    // Convierte la entidad maintenance a DTO
    public maintenanceDTO convertToDTO(maintenance maintenanceEntity) {
        return new maintenanceDTO(
            maintenanceEntity.getid(),
            maintenanceEntity.getdate(),
            maintenanceEntity.getdescription(),
            maintenanceEntity.gettank(),
            maintenanceEntity.getemployee()
        );
    }

    // Convierte el DTO a la entidad maintenance
    public maintenance convertToEntity(maintenanceDTO maintenanceDTO) {
        return new maintenance(
            maintenanceDTO.getId(),
            maintenanceDTO.getDate(),
            maintenanceDTO.getDescription(),
            maintenanceDTO.getTank(),
            maintenanceDTO.getEmployee()
        );
    }
}
