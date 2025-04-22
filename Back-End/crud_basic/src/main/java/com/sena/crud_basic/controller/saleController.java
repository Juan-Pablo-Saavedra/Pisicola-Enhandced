package com.sena.crud_basic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.sena.crud_basic.DTO.saleDTO;
import com.sena.crud_basic.service.saleService;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/sale")
@CrossOrigin(origins = "*")
public class saleController {

    @Autowired
    private saleService saleService;

    // Obtener todas las ventas
    @GetMapping
    public List<saleDTO> getAllSales() {
        return saleService.getAllSales();
    }

    // Obtener una venta por id
    @GetMapping("/{id}")
    public Optional<saleDTO> getSaleById(@PathVariable int id) {
        return saleService.getSaleById(id);
    }

    // Registrar una nueva venta
    @PostMapping
    public String createSale(@RequestBody saleDTO saleDTO) {
        return saleService.save(saleDTO);
    }

    // Actualizar una venta existente
    @PutMapping("/{id}")
    public String updateSale(@PathVariable int id, @RequestBody saleDTO saleDTO) {
        return saleService.updateSale(id, saleDTO);
    }

    // Eliminar una venta
    @DeleteMapping("/{id}")
    public String deleteSale(@PathVariable int id) {
        return saleService.deleteSale(id);
    }

    // Filtrar ventas con parámetros modificados para mejor compatibilidad
    @GetMapping("/filter")
    public List<saleDTO> filterSales(
            // Formato específico para la fecha de tipo String
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
            @RequestParam(required = false) Float total,
            @RequestParam(required = false) Integer customerId) {
        
        // Si tenemos una fecha, la usamos como start y end (un solo día)
        return saleService.filterSales(date, date, total, customerId);
    }
}