package br.com.motiva.dto;

import br.com.motiva.model.NivelPrioridade;
import br.com.motiva.model.TrechoRodovia;

/** Contrato de saída: a entidade nunca é exposta diretamente no JSON. */
public record TrechoResponse(
        Long id,
        String nomeRodovia,
        double quilometroInicial,
        double quilometroFinal,
        double nivelVegetacao,
        String tipoTerreno,
        boolean monitoradoIot,
        NivelPrioridade classificacao
) {
    public static TrechoResponse from(TrechoRodovia trecho, NivelPrioridade classificacao) {
        return new TrechoResponse(
                trecho.getId(),
                trecho.getNomeRodovia(),
                trecho.getQuilometroInicial(),
                trecho.getQuilometroFinal(),
                trecho.getNivelVegetacao(),
                trecho.getTipoTerreno(),
                trecho.isMonitoradoIot(),
                classificacao
        );
    }
}
