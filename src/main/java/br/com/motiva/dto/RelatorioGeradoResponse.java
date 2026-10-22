package br.com.motiva.dto;

import java.util.List;

/** Resposta do POST /api/relatorios: o registro salvo no histórico + o detalhamento por trecho. */
public record RelatorioGeradoResponse(RelatorioResponse relatorio, List<ItemRelatorioResponse> itens) {
}
