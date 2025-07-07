package com.sena.crud_basic.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Component
public class CaptchaValidator {

    @Value("${recaptcha.secret}")
    private String recaptchaSecret;

    @Autowired
    private RestTemplate restTemplate;

    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean validateCaptcha(String token) {
        if (token == null || token.isEmpty()) {
            System.out.println("❌ Token de reCAPTCHA vacío o nulo");
            return false;
        }
        
        System.out.println("🔍 Validando reCAPTCHA token: " + token.substring(0, Math.min(token.length(), 20)) + "...");

        // 🔐 Crear el body con los datos requeridos por Google
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("secret", recaptchaSecret);
        params.add("response", token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            System.out.println("📡 Enviando solicitud a Google reCAPTCHA...");
            ResponseEntity<Map> response = restTemplate.postForEntity(VERIFY_URL, request, Map.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> body = response.getBody();
                
                if (body != null) {
                    System.out.println("📨 Respuesta de Google reCAPTCHA: " + body);
                    
                    if (Boolean.TRUE.equals(body.get("success"))) {
                        System.out.println("✅ Validación de reCAPTCHA exitosa");
                        return true;
                    } else {
                        System.err.println("⛔ Respuesta reCAPTCHA no exitosa");
                        if (body.containsKey("error-codes")) {
                            System.err.println("🔍 Códigos de error: " + body.get("error-codes"));
                        }
                    }
                } else {
                    System.err.println("❌ Respuesta vacía de Google reCAPTCHA");
                }
            } else {
                System.err.println("❌ Estado de respuesta inesperado: " + response.getStatusCode());
            }
        } catch (RestClientException e) {
            System.err.println("❌ Error en la solicitud a reCAPTCHA: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("❌ Validación de reCAPTCHA falló");
        return false;
    }
}