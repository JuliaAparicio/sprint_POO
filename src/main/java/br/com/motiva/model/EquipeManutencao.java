package br.com.motiva.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "EQUIPE_MANUTENCAO")
public class EquipeManutencao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqEquipeManutencao")
    @SequenceGenerator(name = "seqEquipeManutencao", sequenceName = "SEQ_EQUIPE_MANUTENCAO", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOME_EQUIPE", nullable = false, length = 100)
    private String nomeEquipe;

    @Column(name = "RESPONSAVEL", nullable = false, length = 100)
    private String responsavel;

    protected EquipeManutencao() {
    }

    public EquipeManutencao(String nomeEquipe, String responsavel) {
        atualizarDados(nomeEquipe, responsavel);
    }

    public void atualizarDados(String nomeEquipe, String responsavel) {
        this.nomeEquipe = nomeEquipe;
        this.responsavel = responsavel;
    }

    public Long getId() {
        return id;
    }

    public String getNomeEquipe() {
        return nomeEquipe;
    }

    public String getResponsavel() {
        return responsavel;
    }
}
