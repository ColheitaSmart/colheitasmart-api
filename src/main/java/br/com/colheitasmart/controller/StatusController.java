package br.com.colheitasmart.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {
    @GetMapping("/status")
    public Map<String, String> consultar() {
        return Map.of("aplicacao", "ColheitaSmart", "status", "API iniciada");
    }
}
