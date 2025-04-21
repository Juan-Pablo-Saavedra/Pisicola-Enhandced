package com.sena.crud_basic.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.sena.crud_basic.DTO.customerDTO;
import com.sena.crud_basic.model.customer;
import com.sena.crud_basic.repository.Icustomer;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class customerService {

    @Autowired
    private Icustomer customerRepository;

    // Expresiones regulares para validaciones
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]+$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    // Método para guardar un cliente con validaciones
    public String save(customerDTO customerDTO) {
        if (customerDTO.getName() == null || customerDTO.getEmail() == null || customerDTO.getPhone() == null) {
            return "Todos los campos son obligatorios.";
        }

        if (!PHONE_PATTERN.matcher(customerDTO.getPhone()).matches()) {
            return "El teléfono solo puede contener números y el signo '+'.";
        }

        if (!EMAIL_PATTERN.matcher(customerDTO.getEmail()).matches()) {
            return "El correo electrónico debe tener un dominio válido (Ejemplo: usuario@dominio.com).";
        }

        Optional<customer> existingCustomer = customerRepository.findByEmail(customerDTO.getEmail());
        if (existingCustomer.isPresent()) {
            return "El correo electrónico ya está registrado.";
        }

        customer customerEntity = convertToEntity(customerDTO);
        customerRepository.save(customerEntity);
        return "Cliente guardado exitosamente.";
    }

    // Método para obtener todos los clientes
    public List<customerDTO> getAllCustomers() {
        List<customer> customers = customerRepository.findAll();
        return customers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Método para obtener un cliente por ID
    public Optional<customerDTO> getCustomerById(int id) {
        Optional<customer> customerOpt = customerRepository.findById(id);
        return customerOpt.map(this::convertToDTO);
    }

    // Método para actualizar un cliente con validaciones
    public String updateCustomer(int id, customerDTO customerDTO) {
        Optional<customer> existingCustomerOpt = customerRepository.findById(id);

        if (existingCustomerOpt.isEmpty()) {
            return "Cliente no encontrado.";
        }

        customer customerEntity = existingCustomerOpt.get();

        // Validaciones antes de actualizar
        if (customerDTO.getName() == null || customerDTO.getEmail() == null || customerDTO.getPhone() == null) {
            return "Todos los campos son obligatorios.";
        }

        if (!PHONE_PATTERN.matcher(customerDTO.getPhone()).matches()) {
            return "El teléfono solo puede contener números y el signo '+'.";
        }

        if (!EMAIL_PATTERN.matcher(customerDTO.getEmail()).matches()) {
            return "El correo electrónico debe tener un dominio válido (Ejemplo: usuario@dominio.com).";
        }

        customerEntity.setName(customerDTO.getName());
        customerEntity.setEmail(customerDTO.getEmail());
        customerEntity.setPhone(customerDTO.getPhone());

        customerRepository.save(customerEntity);
        return "Cliente actualizado exitosamente.";
    }

    // Método para eliminar un cliente
    public String deleteCustomer(int id) {
        Optional<customer> customerOpt = customerRepository.findById(id);

        if (customerOpt.isEmpty()) {
            return "Cliente no encontrado.";
        }

        customerRepository.deleteById(id);
        return "Cliente eliminado exitosamente.";
    }

    // Método de filtrado general
    public List<customerDTO> filterCustomers(String name, String phone, String email) {
        List<customer> filteredCustomers;

        if (name != null && !name.isEmpty()) {
            filteredCustomers = customerRepository.findByNameContainingIgnoreCase(name);
        } else if (phone != null && !phone.isEmpty()) {
            filteredCustomers = customerRepository.findByPhoneContainingIgnoreCase(phone);
        } else if (email != null && !email.isEmpty()) {
            filteredCustomers = customerRepository.findByEmailContainingIgnoreCase(email);
        } else {
            filteredCustomers = customerRepository.findAll();
        }

        return filteredCustomers.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Conversión entre DTO y Entidad
    private customerDTO convertToDTO(customer customerEntity) {
        return new customerDTO(
            customerEntity.getId(),
            customerEntity.getName(),
            customerEntity.getEmail(),
            customerEntity.getPhone()
        );
    }

    private customer convertToEntity(customerDTO customerDTO) {
        return new customer(
            customerDTO.getId(),
            customerDTO.getName(),
            customerDTO.getEmail(),
            customerDTO.getPhone()
        );
    }
}
