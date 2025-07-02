package com.sena.crud_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sena.crud_basic.model.roles;

public interface Irole extends JpaRepository<roles, Integer> {

}