package com.sena.crud_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sena.crud_basic.model.permission_roles;

public interface Ipermission_role extends JpaRepository<permission_roles, Integer> {

}