package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import com.sena.crud_basic.DTO.customerDTO;
import com.sena.crud_basic.model.customer;
import com.sena.crud_basic.repository.Icustomer;

@Service
public class customerService {

    @Autowired
    private Icustomer data;

    public void save(customerDTO customerDTO) {
        customer customerRegister = convertToModel(customerDTO);
        data.save(customerRegister);
    }

    public customerDTO convertToDTO(customer customer) {
        return new customerDTO(
            customer.getid(),
            customer.getname(),
            customer.getemail(),
            customer.getphone()
        );
    }

    public customer convertToModel(customerDTO customerDTO) {
        return new customer(
            customerDTO.getId(),
            customerDTO.getName(),
            customerDTO.getEmail(),
            customerDTO.getPhone()
        );
    }
} 
