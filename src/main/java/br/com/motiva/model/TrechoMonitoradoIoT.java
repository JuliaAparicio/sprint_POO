package br.com.motiva.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 * Único tipo de trecho capaz de transmitir dados via sensor
 * (correção arquitetural da Sprint 2, mantida aqui com JPA).
 */
@Entity
@DiscriminatorValue("1")
public class TrechoMonitoradoIoT extends TrechoRodovia implements MonitoravelViaIoT {

    protected TrechoMonitoradoIoT() {
    }

    public TrechoMonitoradoIoT(String nomeRodovia, double quilometroInicial, double quilometroFinal,
                               double nivelVegetacao, String tipoTerreno) {
        super(nomeRodovia, quilometroInicial, quilometroFinal, nivelVegetacao, tipoTerreno);
    }

    /** Simula a leitura do sensor: aplica o crescimento conforme o terreno. */
    @Override
    public void transmitirDadosSensor() {
        atualizarCrescimento();
    }
}
