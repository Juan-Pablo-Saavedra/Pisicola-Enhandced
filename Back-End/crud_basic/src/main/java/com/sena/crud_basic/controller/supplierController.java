package com.sena.crud_basic.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.sena.crud_basic.DTO.supplierDTO;
import com.sena.crud_basic.service.supplierService;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5500")
@RestController
@RequestMapping("/api/v1/supplier")
public class supplierController {

    @Autowired
    private supplierService supplierService;

    // ✅ Endpoint para registrar un nuevo proveedor
    @PostMapping
    public ResponseEntity<String> registerSupplier(@RequestBody supplierDTO supplierDTO) {
        try {
            String response = supplierService.save(supplierDTO);
            HttpStatus status = response.equals("Proveedor guardado exitosamente.") ? HttpStatus.CREATED
                    : HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(response, status);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al registrar proveedor: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ Endpoint para obtener todos los proveedores
    @GetMapping
    public ResponseEntity<Object> getAllSuppliers() {
        try {
            List<supplierDTO> suppliers = supplierService.getAllSuppliers();
            return suppliers.isEmpty()
                    ? new ResponseEntity<>("No hay proveedores registrados.", HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(suppliers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al obtener proveedores: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ Endpoint para obtener un proveedor por ID
    @GetMapping("/{id}")
    public ResponseEntity<Object> getSupplierById(@PathVariable int id) {
        try {
            Optional<supplierDTO> supplier = supplierService.getSupplierById(id);
            return supplier.isPresent()
                    ? ResponseEntity.ok(supplier.get())
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Proveedor no encontrado.");
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al obtener proveedor: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ Endpoint para actualizar un proveedor
    @PutMapping("/{id}")
    public ResponseEntity<String> updateSupplier(@PathVariable int id, @RequestBody supplierDTO supplierDTO) {
        try {
            String response = supplierService.updateSupplier(id, supplierDTO);
            HttpStatus status = response.equals("Proveedor actualizado exitosamente.") ? HttpStatus.OK
                    : HttpStatus.BAD_REQUEST;
            return new ResponseEntity<>(response, status);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al actualizar proveedor: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ Endpoint para eliminar un proveedor asegurando que no tenga alimentos asociados
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteSupplier(@PathVariable int id) {
        try {
            Object response = supplierService.deleteSupplier(id);

            if (response instanceof String) {
                HttpStatus status = response.equals("Proveedor eliminado exitosamente.") ? HttpStatus.OK
                        : HttpStatus.NOT_FOUND;
                return new ResponseEntity<>(response, status);
            } else {
                return new ResponseEntity<>("El proveedor tiene alimentos asociados y no puede ser eliminado.",
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al eliminar proveedor: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ✅ Endpoint para filtrar proveedores por nombre y/o categoría
    @GetMapping("/filter")
    public ResponseEntity<Object> filterSuppliers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category) {
        try {
            List<supplierDTO> filteredSuppliers = supplierService.filterSuppliers(name, category);
            return filteredSuppliers.isEmpty()
                    ? new ResponseEntity<>("No hay proveedores que coincidan con el filtro.", HttpStatus.NO_CONTENT)
                    : new ResponseEntity<>(filteredSuppliers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno al filtrar proveedores: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
