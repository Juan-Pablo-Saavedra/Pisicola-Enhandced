package com.sena.crud_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sena.crud_basic.model.maintenance;

@Repository
public interface Imaintenance extends JpaRepository<maintenance, Integer> {
    /*
     * C - Create
     * R - Read
     * U - Update
     * D - Delete
     */
}
