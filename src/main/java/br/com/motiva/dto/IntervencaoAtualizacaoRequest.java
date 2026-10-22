package br.com.motiva.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Atualização de intervenção (PUT /api/intervencoes/{id}).
 * Trecho e tipo não mudam: o tipo foi decidido pelo motor para aquele trecho.
 */
public record IntervencaoAtualizacaoRequest(

        @NotNull(message = "O ID da equipe é obrigatório.")
        @Positive(message = "O ID da equipe deve ser positivo.")
        Long equipeId,

        @NotNull(message = "A data de execução é obrigatória.")
        LocalDateTime dataExecucao,

        @Size(max = 200, message = "A observação deve ter no máximo 200 caracteres.")
        String observacao
) {
}
