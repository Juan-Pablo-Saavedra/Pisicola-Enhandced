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

    public List<Roles> findAll() {
        return data.findAll();
    }

    public Optional<Roles> findById(int id) {
        return data.findById(id);
    }

    public ResponsesDTO deleteRole(int id) {
        Optional<Roles> roleOpt = findById(id);
        if (!roleOpt.isPresent()) {
            return new ResponsesDTO(HttpStatus.NOT_FOUND.toString(), "El rol no existe");
        }
        data.deleteById(id);
        return new ResponsesDTO(HttpStatus.OK.toString(), "Rol eliminado correctamente");
    }

    public ResponsesDTO save(RoleDTO roleDTO) {
        Roles rol = convertToModel(roleDTO);
        data.save(rol);
        return new ResponsesDTO(HttpStatus.OK.toString(), "Rol guardado correctamente");
    }

    public ResponsesDTO updateRole(int id, RoleDTO roleDTO) {
        Optional<Roles> roleOpt = findById(id);
        if (!roleOpt.isPresent()) {
            return new ResponsesDTO(HttpStatus.NOT_FOUND.toString(), "El rol no existe");
        }
        Roles updatedRole = roleOpt.get();
        updatedRole.setName(roleDTO.getName());
        updatedRole.setDescription(roleDTO.getDescription());

        data.save(updatedRole);
        return new ResponsesDTO(HttpStatus.OK.toString(), "Rol actualizado correctamente");
    }

    public RoleDTO convertToDTO(Roles rol) {
        return new RoleDTO(
            rol.getRoleid(),
            rol.getName(),
            rol.getDescription()
        );
    }

    public Roles convertToModel(RoleDTO roleDTO) {
        return new Roles(
            0, // Nuevo rol, id generado por BD
            roleDTO.getName(),
            roleDTO.getDescription()
        );
    }
}