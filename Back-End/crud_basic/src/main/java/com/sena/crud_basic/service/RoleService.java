package com.sena.crud_basic.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.sena.crud_basic.DTO.responseDTO;
import com.sena.crud_basic.DTO.roleDTO;
import com.sena.crud_basic.model.roles;
import com.sena.crud_basic.repository.Irole;

@Service
public class RoleService {

     @Autowired
    private Irole data;

    public List<roles> findAll() {
        return data.findAll();
    }

    public Optional<roles> findById(int id) {
        return data.findById(id);
    }

    public responseDTO deleteRole(int id) {
        Optional<roles> roleOpt = findById(id);
        if (!roleOpt.isPresent()) {
            return new responseDTO(HttpStatus.NOT_FOUND.toString(), "El rol no existe");
        }
        data.deleteById(id);
        return new responseDTO(HttpStatus.OK.toString(), "Rol eliminado correctamente");
    }

    public responseDTO save(roleDTO roleDTO) {
        roles rol = convertToModel(roleDTO);
        data.save(rol);
        return new responseDTO(HttpStatus.OK.toString(), "Rol guardado correctamente");
    }

    public responseDTO updateRole(int id, roleDTO roleDTO) {
        Optional<roles> roleOpt = findById(id);
        if (!roleOpt.isPresent()) {
            return new responseDTO(HttpStatus.NOT_FOUND.toString(), "El rol no existe");
        }
        roles updatedRole = roleOpt.get();
        updatedRole.setName(roleDTO.getName());
        updatedRole.setDescription(roleDTO.getDescription());

        data.save(updatedRole);
        return new responseDTO(HttpStatus.OK.toString(), "Rol actualizado correctamente");
    }

    public roleDTO convertToDTO(roles rol) {
        return new roleDTO(
            rol.getRoleid(),
            rol.getName(),
            rol.getDescription()
        );
    }

    public roles convertToModel(roleDTO roleDTO) {
        return new roles(
            0, // Nuevo rol, id generado por BD
            roleDTO.getName(),
            roleDTO.getDescription()
        );
    }
}