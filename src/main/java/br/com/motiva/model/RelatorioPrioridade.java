package br.com.motiva.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/** Histórico dos relatórios de prioridade gerados. */
@Entity
@Table(name = "RELATORIO_PRIORIDADE")
public class RelatorioPrioridade {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqRelatorioPrioridade")
    @SequenceGenerator(name = "seqRelatorioPrioridade", sequenceName = "SEQ_RELATORIO_PRIORIDADE",
            allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DATA_GERACAO", nullable = false)
    private LocalDateTime dataGeracao;

    @Column(name = "QT_URGENTE", nullable = false)
    private int qtUrgente;

    @Column(name = "QT_CRITICO", nullable = false)
    private int qtCritico;

    @Column(name = "QT_ATENCAO", nullable = false)
    private int qtAtencao;

    @Column(name = "QT_NORMAL", nullable = false)
    private int qtNormal;

    @Column(name = "RESUMO", length = 500)
    private String resumo;

    protected RelatorioPrioridade() {
    }

    public RelatorioPrioridade(int qtUrgente, int qtCritico, int qtAtencao, int qtNormal, String resumo) {
        this.dataGeracao = LocalDateTime.now();
        this.qtUrgente = qtUrgente;
        this.qtCritico = qtCritico;
        this.qtAtencao = qtAtencao;
        this.qtNormal = qtNormal;
        this.resumo = resumo;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDataGeracao() {
        return dataGeracao;
    }

    public int getQtUrgente() {
        return qtUrgente;
    }

    public int getQtCritico() {
        return qtCritico;
    }

    public int getQtAtencao() {
        return qtAtencao;
    }

    public int getQtNormal() {
        return qtNormal;
    }

    public String getResumo() {
        return resumo;
    }
}
