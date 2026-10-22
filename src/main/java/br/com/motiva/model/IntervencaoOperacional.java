package br.com.motiva.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Classe abstrata da Sprint 2, agora mapeada com JPA.
 *
 * Herança SINGLE_TABLE: as três subclasses (SemIntervencao, Pulverizacao,
 * RocadaMecanizada) ficam na tabela INTERVENCAO_OPERACIONAL, e a coluna
 * TIPO_INTERVENCAO é o discriminador que diz ao Hibernate qual subclasse
 * instanciar ao ler cada linha. O polimorfismo continua funcionando.
 */
@Entity
@Table(name = "INTERVENCAO_OPERACIONAL")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TIPO_INTERVENCAO", discriminatorType = DiscriminatorType.STRING, length = 50)
public abstract class IntervencaoOperacional {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqIntervencaoOperacional")
    @SequenceGenerator(name = "seqIntervencaoOperacional", sequenceName = "SEQ_INTERVENCAO_OPERACIONAL",
            allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    /**
     * Leitura do discriminador como atributo (somente leitura), para permitir
     * a derived query findByTipo(). Quem grava a coluna é o próprio Hibernate.
     */
    @Column(name = "TIPO_INTERVENCAO", insertable = false, updatable = false)
    private String tipo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_TRECHO", nullable = false)
    private TrechoRodovia trecho;

    /** Vínculo com a equipe (pendência do feedback da Sprint 3). */
    @ManyToOne(optional = false)
    @JoinColumn(name = "ID_EQUIPE", nullable = false)
    private EquipeManutencao equipe;

    @Column(name = "DATA_EXECUCAO", nullable = false)
    private LocalDateTime dataExecucao;

    @Column(name = "OBSERVACAO", length = 200)
    private String observacao;

    protected IntervencaoOperacional() {
    }

    protected IntervencaoOperacional(String tipo) {
        this.tipo = tipo;
    }

    /** Nome legível do tipo de intervenção (ex.: "Pulverização"). */
    public abstract String getDescricao();

    /** O que a equipe deve fazer no trecho (antes era um System.out.println). */
    public abstract String descreverServico();

    public void registrar(TrechoRodovia trecho, EquipeManutencao equipe,
                          LocalDateTime dataExecucao, String observacao) {
        this.trecho = trecho;
        this.equipe = equipe;
        this.dataExecucao = dataExecucao;
        this.observacao = observacao;
    }

    public void atualizarExecucao(EquipeManutencao equipe, LocalDateTime dataExecucao, String observacao) {
        this.equipe = equipe;
        this.dataExecucao = dataExecucao;
        this.observacao = observacao;
    }

    public Long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public TrechoRodovia getTrecho() {
        return trecho;
    }

    public EquipeManutencao getEquipe() {
        return equipe;
    }

    public LocalDateTime getDataExecucao() {
        return dataExecucao;
    }

    public String getObservacao() {
        return observacao;
    }
}
