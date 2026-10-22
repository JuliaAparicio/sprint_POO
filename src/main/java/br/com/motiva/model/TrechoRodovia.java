package br.com.motiva.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 * Trecho de rodovia monitorado.
 *
 * Herança SINGLE_TABLE: TrechoRodovia e TrechoMonitoradoIoT ficam na mesma
 * tabela TRECHO_RODOVIA. A coluna MONITORADO_IOT (já existente na Sprint 3)
 * vira o discriminador: 0 = trecho comum, 1 = trecho monitorado via IoT.
 */
@Entity
@Table(name = "TRECHO_RODOVIA")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "MONITORADO_IOT", discriminatorType = DiscriminatorType.INTEGER)
@DiscriminatorValue("0")
public class TrechoRodovia {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqTrechoRodovia")
    @SequenceGenerator(name = "seqTrechoRodovia", sequenceName = "SEQ_TRECHO_RODOVIA", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NOME_RODOVIA", nullable = false, length = 50)
    private String nomeRodovia;

    @Column(name = "KM_INICIAL", nullable = false)
    private double quilometroInicial;

    @Column(name = "KM_FINAL", nullable = false)
    private double quilometroFinal;

    @Column(name = "NIVEL_VEGETACAO", nullable = false)
    private double nivelVegetacao;

    @Column(name = "TIPO_TERRENO", nullable = false, length = 20)
    private String tipoTerreno;

    /** Construtor exigido pelo JPA. */
    protected TrechoRodovia() {
    }

    public TrechoRodovia(String nomeRodovia, double quilometroInicial, double quilometroFinal,
                         double nivelVegetacao, String tipoTerreno) {
        atualizarDados(nomeRodovia, quilometroInicial, quilometroFinal, nivelVegetacao, tipoTerreno);
    }

    /** Os dados já chegam validados pelo TrechoRodoviaService. */
    public void atualizarDados(String nomeRodovia, double quilometroInicial, double quilometroFinal,
                               double nivelVegetacao, String tipoTerreno) {
        this.nomeRodovia = nomeRodovia;
        this.quilometroInicial = quilometroInicial;
        this.quilometroFinal = quilometroFinal;
        this.nivelVegetacao = nivelVegetacao;
        this.tipoTerreno = tipoTerreno;
    }

    public void registrarCrescimento(double taxa) {
        if (taxa < 0) {
            throw new IllegalArgumentException("A taxa de crescimento não pode ser negativa.");
        }
        nivelVegetacao += taxa;
    }

    public void atualizarCrescimento() {
        if (Constantes.TERRENO_UMIDO.equalsIgnoreCase(tipoTerreno)) {
            registrarCrescimento(Constantes.TAXA_CRESCIMENTO_TERRENO_UMIDO);
        } else {
            registrarCrescimento(Constantes.TAXA_CRESCIMENTO_TERRENO_SECO);
        }
    }

    public boolean trechoCritico() {
        return nivelVegetacao >= Constantes.LIMIAR_CRITICO;
    }

    public boolean isMonitoradoIot() {
        return this instanceof MonitoravelViaIoT;
    }

    public Long getId() {
        return id;
    }

    public String getNomeRodovia() {
        return nomeRodovia;
    }

    public double getQuilometroInicial() {
        return quilometroInicial;
    }

    public double getQuilometroFinal() {
        return quilometroFinal;
    }

    public double getNivelVegetacao() {
        return nivelVegetacao;
    }

    public String getTipoTerreno() {
        return tipoTerreno;
    }
}
