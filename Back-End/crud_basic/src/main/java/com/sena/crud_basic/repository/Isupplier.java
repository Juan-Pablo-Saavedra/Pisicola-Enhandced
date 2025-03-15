package com.sena.crud_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.sena.crud_basic.model.supplier;

@Repository
public interface Isupplier extends JpaRepository<supplier, Integer> {
    /*
     * C - Create
     * R - Read
     * U - Update
     * D - Delete
     */
}
