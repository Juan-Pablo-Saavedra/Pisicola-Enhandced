package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.saleDTO;
import com.sena.crud_basic.model.sale;
import com.sena.crud_basic.repository.Isale;

@Service
public class saleService {

    @Autowired
    private Isale saleRepository;

    // Método para guardar una nueva entidad sale a partir del DTO
    public void save(saleDTO saleDTO) {
        sale saleEntity = convertToEntity(saleDTO);
        saleRepository.save(saleEntity);
    }

    // Convierte la entidad sale a DTO
    public saleDTO convertToDTO(sale saleEntity) {
        return new saleDTO(
            saleEntity.getid(),
            saleEntity.getdate(),
            saleEntity.gettotal(),
            saleEntity.getcustomer()
        );
    }

    // Convierte el DTO a la entidad sale
    public sale convertToEntity(saleDTO saleDTO) {
        return new sale(
            saleDTO.getId(),
            saleDTO.getDate(),
            saleDTO.getTotal(),
            saleDTO.getCustomer()
        );
    }
}
