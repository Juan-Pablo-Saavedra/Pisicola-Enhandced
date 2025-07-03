package com.sena.crud_basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.sena.crud_basic.model.recovery_requests;

public interface Irecovery_request extends JpaRepository<recovery_requests, Integer> {
}