package br.com.colheitasmart.dto;
import java.time.Instant;
import java.util.Map;
public record ErroDTO(Instant instante, int status, String mensagem, String caminho, Map<String, String> campos) {}
