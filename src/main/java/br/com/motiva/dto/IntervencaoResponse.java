package br.com.motiva.dto;

import br.com.motiva.model.IntervencaoOperacional;
import br.com.motiva.model.RocadaMecanizada;

import java.time.LocalDateTime;

public record IntervencaoResponse(
        Long id,
        String tipo,
        String descricao,
        boolean urgente,
        String servico,
        LocalDateTime dataExecucao,
        String observacao,
        Long trechoId,
        String nomeRodovia,
        Long equipeId,
        String nomeEquipe
) {
    public static IntervencaoResponse from(IntervencaoOperacional intervencao) {
        boolean urgente = intervencao instanceof RocadaMecanizada rocada && rocada.isUrgente();
        return new IntervencaoResponse(
                intervencao.getId(),
                intervencao.getTipo(),
                intervencao.getDescricao(),        // polimorfismo: cada subclasse responde
                urgente,
                intervencao.descreverServico(),    // polimorfismo
                intervencao.getDataExecucao(),
                intervencao.getObservacao(),
                intervencao.getTrecho().getId(),
                intervencao.getTrecho().getNomeRodovia(),
                intervencao.getEquipe().getId(),
                intervencao.getEquipe().getNomeEquipe()
        );
    }
}
