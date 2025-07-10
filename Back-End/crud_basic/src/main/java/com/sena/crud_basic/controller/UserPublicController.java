package com.sena.crud_basic.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sena.crud_basic.DTO.RequestLoginDTO;
import com.sena.crud_basic.DTO.responseDTO;
import com.sena.crud_basic.DTO.responseLogin;
import com.sena.crud_basic.DTO.employeeDTO;
import com.sena.crud_basic.service.employeeService;

@RestController
@RequestMapping("api/v1/public/users")
public class UserPublicController {
     @Autowired
    private employeeService userService;

   @PostMapping("/register")
    public ResponseEntity<Object> saveUser(@RequestBody employeeDTO user) {
        responseDTO response = userService.save(user);
        // ResponsesDTO response =null;
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<responseLogin> login(@RequestBody RequestLoginDTO userDTO) {
        // Llamar al método correcto pasando el objeto completo
        responseLogin response = userService.login(userDTO);
        return ResponseEntity.ok(response);
    }

    //  @PostMapping("/forgot") //falta desarrollar
    // public ResponseEntity<Object> forgot(@RequestBody UserDTO userDTO) {
    //     // ResponsesDTO response = userService.save(userDTO);
    //     return new ResponseEntity<>(response, HttpStatus.OK);
    // }
    
}