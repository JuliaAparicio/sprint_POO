package br.com.motiva.model;

/**
 * Classificação de prioridade de um trecho, definida pelo motor de regras
 * (IntervencaoService) a partir dos limiares de Constantes.
 */
public enum NivelPrioridade {
    URGENTE,
    CRITICO,
    ATENCAO,
    NORMAL
}
