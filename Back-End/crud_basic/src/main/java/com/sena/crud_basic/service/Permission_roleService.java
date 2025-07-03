package com.sena.crud_basic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;

import com.sena.crud_basic.repository.Ipage;
import com.sena.crud_basic.repository.Ipermission_role;
import com.sena.crud_basic.repository.Irole;
import com.sena.crud_basic.DTO.responseDTO;
import com.sena.crud_basic.model.permission_roles;
import com.sena.crud_basic.DTO.Permission_roleDTO;
import com.sena.crud_basic.model.pages;
import com.sena.crud_basic.model.roles;
import java.util.List;
import java.util.Optional;


@Service
public class Permission_roleService {
    @Autowired
    private Ipermission_role data;

    @Autowired
    private Irole roleRepository;

    @Autowired
    private Ipage pageRepository;

    public List<permission_roles> findAll() {
        return data.findAll();
    }

    public Optional<permission_roles> findById(int id) {
        return data.findById(id);
    }

    public responseDTO deletePermissionRole(int id) {
        Optional<permission_roles> permissionRole = findById(id);
        if (!permissionRole.isPresent()) {
            return new responseDTO(HttpStatus.NOT_FOUND.toString(), "El permiso de rol no existe");
        }

        data.deleteById(id);
        return new responseDTO(HttpStatus.OK.toString(), "Permiso de rol eliminado correctamente");
    }

    public responseDTO save(Permission_roleDTO permissionRoleDTO) {
        permission_roles permissionRole = convertToModel(permissionRoleDTO);
        data.save(permissionRole);
        return new responseDTO(HttpStatus.OK.toString(), "Permiso de rol guardado correctamente");
    }

    public responseDTO updatePermissionRole(int id, Permission_roleDTO permissionRoleDTO) {
        Optional<permission_roles> permissionRole = findById(id);
        if (!permissionRole.isPresent()) {
            return new responseDTO(HttpStatus.NOT_FOUND.toString(), "El permiso de rol no existe");
        }

        permission_roles updatedPermissionRole = permissionRole.get();
        updatedPermissionRole.setPage(permissionRoleDTO.getPage());
        updatedPermissionRole.setRole(permissionRoleDTO.getRole());
        updatedPermissionRole.setType(permissionRoleDTO.getType());

        data.save(updatedPermissionRole);
        return new responseDTO(HttpStatus.OK.toString(), "Permiso de rol actualizado correctamente");
    }

    public Permission_roleDTO convertToDTO(permission_roles permissionRole) {
        Permission_roleDTO dto = new Permission_roleDTO();
        dto.setPermission_roleid(permissionRole.getPermission_roleid());
        dto.setPage(permissionRole.getPage());
        dto.setRole(permissionRole.getRole());
        dto.setType(permissionRole.getType());
        return dto;
    }

    public permission_roles convertToModel(Permission_roleDTO permissionRoleDTO) {
        pages page = pageRepository.findById(permissionRoleDTO.getPage().getPageid())
                .orElseThrow(() -> new RuntimeException("Page not found"));
        roles role = roleRepository.findById(permissionRoleDTO.getRole().getRoleid())
                .orElseThrow(() -> new RuntimeException("Role not found"));
        return new permission_roles(
            permissionRoleDTO.getPermission_roleid(),
            page,
            role,
            permissionRoleDTO.getType()
        );
       
    }

}