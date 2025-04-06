package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.customerDTO;
import com.sena.crud_basic.model.customer;
import com.sena.crud_basic.repository.Icustomer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class customerService {

    @Autowired
    private Icustomer data;

    // Registrar un cliente
    public String save(customerDTO customerDTO) {
        customer customerEntity = convertToEntity(customerDTO);
        data.save(customerEntity);
        return "Cliente registrado exitosamente.";
    }

    // Listar todos los clientes
    public List<customerDTO> getAllCustomers() {
        List<customer> customers = data.findAll();
        return customers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Obtener un cliente por ID
    public customerDTO getCustomerById(int id) {
        Optional<customer> customer = data.findById(id);
        return customer.map(this::convertToDTO).orElse(null);
    }

    // Actualizar un cliente
    public String updateCustomer(int id, customerDTO customerDTO) {
        Optional<customer> existingCustomer = data.findById(id);

        if (existingCustomer.isEmpty()) {
            return "Cliente no encontrado.";
        }

        customer customerEntity = existingCustomer.get();
        customerEntity.setName(customerDTO.getName());
        customerEntity.setEmail(customerDTO.getEmail());
        customerEntity.setPhone(customerDTO.getPhone());

        data.save(customerEntity);
        return "Cliente actualizado exitosamente.";
    }

    // Eliminar un cliente
    public String deleteCustomer(int id) {
        if (!data.existsById(id)) {
            return "Cliente no encontrado.";
        }
        data.deleteById(id);
        return "Cliente eliminado exitosamente.";
    }

    // Convertir entidad a DTO
    private customerDTO convertToDTO(customer customerEntity) {
        return new customerDTO(
            customerEntity.getId(),
            customerEntity.getName(),
            customerEntity.getEmail(),
            customerEntity.getPhone()
        );
    }

    // Convertir DTO a entidad
    private customer convertToEntity(customerDTO customerDTO) {
        return new customer(
            customerDTO.getId(),
            customerDTO.getName(),
            customerDTO.getEmail(),
            customerDTO.getPhone()
        );
    }
}