// src/main/java/com/sena/crud_basic/config/applicationConfig.java
package com.sena.crud_basic.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import com.sena.crud_basic.repository.Iemployee;

@Configuration
@RequiredArgsConstructor
public class applicationConfig {

    private final Iemployee employeeRepo;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> employeeRepo
            .findByEmail(email)
            .map(employee -> new org.springframework.security.core.userdetails.User(
                employee.getEmail(),
                employee.getPassword(),
                new ArrayList<>()
            ))
            .orElseThrow(() ->
                new UsernameNotFoundException("Usuario no encontrado: " + email)
            );
    }
}
