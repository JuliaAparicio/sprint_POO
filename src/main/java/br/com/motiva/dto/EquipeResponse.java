package br.com.motiva.dto;

import br.com.motiva.model.EquipeManutencao;

public record EquipeResponse(Long id, String nomeEquipe, String responsavel) {

    public static EquipeResponse from(EquipeManutencao equipe) {
        return new EquipeResponse(equipe.getId(), equipe.getNomeEquipe(), equipe.getResponsavel());
    }
}
