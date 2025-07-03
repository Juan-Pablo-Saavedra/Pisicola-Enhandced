package com.sena.crud_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sena.crud_basic.model.pages;

public interface Ipage extends JpaRepository<pages, Integer> {

}