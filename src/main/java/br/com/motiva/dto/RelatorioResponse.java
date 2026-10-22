package br.com.motiva.dto;

import br.com.motiva.model.RelatorioPrioridade;

import java.time.LocalDateTime;

public record RelatorioResponse(
        Long id,
        LocalDateTime dataGeracao,
        int qtUrgente,
        int qtCritico,
        int qtAtencao,
        int qtNormal,
        String resumo
) {
    public static RelatorioResponse from(RelatorioPrioridade relatorio) {
        return new RelatorioResponse(
                relatorio.getId(),
                relatorio.getDataGeracao(),
                relatorio.getQtUrgente(),
                relatorio.getQtCritico(),
                relatorio.getQtAtencao(),
                relatorio.getQtNormal(),
                relatorio.getResumo()
        );
    }
}
