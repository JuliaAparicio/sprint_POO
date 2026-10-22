package br.com.motiva.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Criação de intervenção (POST /api/intervencoes).
 * O TIPO não é informado: quem decide é o motor de regras (IntervencaoService),
 * com base no nível de vegetação do trecho.
 */
public record IntervencaoRequest(

        @NotNull(message = "O ID do trecho é obrigatório.")
        @Positive(message = "O ID do trecho deve ser positivo.")
        Long trechoId,

        @NotNull(message = "O ID da equipe é obrigatório.")
        @Positive(message = "O ID da equipe deve ser positivo.")
        Long equipeId,

        /* Opcional: se não informada, usa a data/hora atual. */
        LocalDateTime dataExecucao,

        @Size(max = 200, message = "A observação deve ter no máximo 200 caracteres.")
        String observacao
) {
}
