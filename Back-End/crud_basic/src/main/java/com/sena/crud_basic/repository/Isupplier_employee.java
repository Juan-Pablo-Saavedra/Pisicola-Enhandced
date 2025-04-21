package com.sena.crud_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sena.crud_basic.model.supplier_employee;
import java.util.List;

@Repository
public interface Isupplier_employee extends JpaRepository<supplier_employee, Integer> {
    /*
     * Métodos CRUD proporcionados por JpaRepository:
     * - save, findById, findAll, delete, etc.
     */

    // Método para obtener todas las relaciones de un empleado específico
    List<supplier_employee> findByEmployeeId(Integer employeeId);

    // Método para obtener todas las relaciones de un proveedor específico
    List<supplier_employee> findBySupplierId(Integer supplierId);
}
