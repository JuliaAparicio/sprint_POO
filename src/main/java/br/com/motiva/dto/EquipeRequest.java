package br.com.motiva.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record EquipeRequest(

        @NotBlank(message = "O nome da equipe é obrigatório.")
        @Size(max = 100, message = "O nome da equipe deve ter no máximo 100 caracteres.")
        String nomeEquipe,

        @NotBlank(message = "O responsável é obrigatório.")
        @Size(max = 100, message = "O nome do responsável deve ter no máximo 100 caracteres.")
        String responsavel
) {
}
