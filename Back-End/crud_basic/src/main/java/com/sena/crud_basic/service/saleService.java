package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.saleDTO;
import com.sena.crud_basic.model.sale;
import com.sena.crud_basic.model.customer;
import com.sena.crud_basic.repository.Isale;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Date;
import java.util.Calendar;

@Service
public class saleService {

    @Autowired
    private Isale saleRepository;

    // Crear una nueva venta
    public String save(saleDTO saleDTO) {
        try {
            if (saleDTO.getDate() == null) {
                return "La fecha es obligatoria.";
            }
            if (saleDTO.getTotal() <= 0) {
                return "El total debe ser mayor que cero.";
            }
            if (saleDTO.getCustomerId() <= 0) {
                return "El customer es obligatorio.";
            }
            sale saleEntity = convertToEntity(saleDTO);
            saleRepository.save(saleEntity);
            return "Venta registrada exitosamente.";
        } catch (Exception e) {
            return "Error al registrar sale: " + e.getMessage();
        }
    }

    // Obtener todas las ventas
    public List<saleDTO> getAllSales() {
        try {
            return saleRepository.findAll()
                    .stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las ventas: " + e.getMessage());
        }
    }

    // Obtener una venta por id
    public Optional<saleDTO> getSaleById(int id) {
        try {
            return saleRepository.findById(id).map(this::convertToDTO);
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener la venta: " + e.getMessage());
        }
    }

    // Actualizar una venta existente
    public String updateSale(int id, saleDTO saleDTO) {
        try {
            Optional<sale> optionalSale = saleRepository.findById(id);
            if (optionalSale.isEmpty()) {
                return "Venta no encontrada.";
            }
            sale saleEntity = optionalSale.get();
            saleEntity.setdate(saleDTO.getDate());
            saleEntity.settotal(saleDTO.getTotal());
            // Actualizamos el customer asignándole el id recibido
            customer cust = new customer();
            cust.setId(saleDTO.getCustomerId());
            saleEntity.setcustomer(cust);
            saleRepository.save(saleEntity);
            return "Venta actualizada exitosamente.";
        } catch (Exception e) {
            return "Error al actualizar sale: " + e.getMessage();
        }
    }

    // Eliminar una venta
    public String deleteSale(int id) {
        try {
            Optional<sale> optionalSale = saleRepository.findById(id);
            if (optionalSale.isEmpty()) {
                return "Venta no encontrada.";
            }
            saleRepository.deleteById(id);
            return "Venta eliminada exitosamente.";
        } catch (Exception e) {
            return "Error al eliminar sale: " + e.getMessage();
        }
    }

    // Método mejorado para filtrar ventas
    public List<saleDTO> filterSales(Date start, Date end, Float total, Integer customerId) {
        try {
            // Obtenemos todas las ventas primero
            List<sale> filteredSales = saleRepository.findAll();
            
            // Si tenemos fecha inicio/fin, filtramos por rango
            if (start != null && end != null) {
                // Para incluir todo el día de end, ajustamos la fecha final a 23:59:59
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(end);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                Date adjustedEnd = calendar.getTime();
                
                filteredSales = filteredSales.stream()
                        .filter(s -> {
                            Date saleDate = s.getdate();
                            return saleDate != null && 
                                  !saleDate.before(start) && 
                                  !saleDate.after(adjustedEnd);
                        })
                        .collect(Collectors.toList());
            }
            
            // Filtrar por total si se proporciona
            if (total != null) {
                filteredSales = filteredSales.stream()
                        .filter(s -> Math.abs(s.gettotal() - total) < 0.01) // Comparación de igualdad con tolerancia para floats
                        .collect(Collectors.toList());
            }
            
            // Filtrar por cliente si se proporciona ID
            if (customerId != null && customerId > 0) {
                filteredSales = filteredSales.stream()
                        .filter(s -> s.getcustomer() != null && s.getcustomer().getId() == customerId)
                        .collect(Collectors.toList());
            }
            
            return filteredSales.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al filtrar sales: " + e.getMessage());
        }
    }

    // Conversión de entidad a DTO: se extrae el customer id y su nombre
    private saleDTO convertToDTO(sale saleEntity) {
        int custId = 0;
        String custName = "";
        if (saleEntity.getcustomer() != null) {
            custId = saleEntity.getcustomer().getId();
            custName = saleEntity.getcustomer().getName();
        }
        return new saleDTO(
                saleEntity.getid(),
                saleEntity.getdate(),
                saleEntity.gettotal(),
                custId,
                custName
        );
    }

    // Conversión de DTO a entidad: se crea un objeto customer con el id recibido.
    private sale convertToEntity(saleDTO saleDTO) {
        customer cust = new customer();
        cust.setId(saleDTO.getCustomerId());
        return new sale(
                saleDTO.getId(),
                saleDTO.getDate(),
                saleDTO.getTotal(),
                cust
        );
    }
}