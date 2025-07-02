// src/main/java/com/sena/crud_basic/config/securityConfig.java
package com.sena.crud_basic.config;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.sena.crud_basic.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class securityConfig {

    private final AuthenticationProvider authProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
          .csrf(cs -> cs.disable())
          .authorizeHttpRequests(auth -> auth
              .requestMatchers("/api/v1/public/**").permitAll()
              .anyRequest().authenticated()
          )
          .sessionManagement(sess -> sess
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          )
          .authenticationProvider(authProvider)
          .addFilterBefore(
              jwtAuthenticationFilter,
              UsernamePasswordAuthenticationFilter.class
          );

        return http.build();
    }
}
