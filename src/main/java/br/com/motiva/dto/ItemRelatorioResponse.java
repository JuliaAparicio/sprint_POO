package br.com.motiva.dto;

import br.com.motiva.model.NivelPrioridade;

/** Uma linha do relatório: o trecho e a intervenção recomendada pelo motor. */
public record ItemRelatorioResponse(
        Long trechoId,
        String nomeRodovia,
        double quilometroInicial,
        double quilometroFinal,
        double nivelVegetacao,
        NivelPrioridade classificacao,
        String intervencaoRecomendada
) {
}
