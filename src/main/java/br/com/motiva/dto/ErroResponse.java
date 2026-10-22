package br.com.motiva.dto;

import java.time.LocalDateTime;
import java.util.Map;

/** Corpo padrão de erro devolvido pelo GlobalExceptionHandler. */
public record ErroResponse(
        LocalDateTime timestamp,
        int status,
        String erro,
        String mensagem,
        String caminho,
        Map<String, String> campos
) {
}
