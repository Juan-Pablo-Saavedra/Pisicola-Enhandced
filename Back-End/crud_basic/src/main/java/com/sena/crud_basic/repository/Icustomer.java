package com.sena.crud_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sena.crud_basic.model.customer;

@Repository
public interface Icustomer extends JpaRepository<customer, Integer> {

    /*
     * C - Create
     * R - Read
     * U - Update
     * D - Delete
     */
}
