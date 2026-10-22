package br.com.motiva.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

/** Contrato de entrada (POST/PUT /api/trechos). */
public record TrechoRequest(

        @NotBlank(message = "O nome da rodovia é obrigatório.")
        @Size(max = 50, message = "O nome da rodovia deve ter no máximo 50 caracteres.")
        String nomeRodovia,

        @NotNull(message = "O km inicial é obrigatório.")
        @PositiveOrZero(message = "O km inicial não pode ser negativo.")
        Double quilometroInicial,

        @NotNull(message = "O km final é obrigatório.")
        @Positive(message = "O km final deve ser maior que zero.")
        Double quilometroFinal,

        @NotNull(message = "O nível de vegetação é obrigatório.")
        @PositiveOrZero(message = "O nível de vegetação não pode ser negativo.")
        Double nivelVegetacao,

        @NotBlank(message = "O tipo de terreno é obrigatório.")
        @Pattern(regexp = "(?i)SECO|UMIDO", message = "O tipo de terreno deve ser SECO ou UMIDO.")
        String tipoTerreno,

        /* Opcional: true cria um TrechoMonitoradoIoT. Padrão: false. */
        Boolean monitoradoIot
) {
}
