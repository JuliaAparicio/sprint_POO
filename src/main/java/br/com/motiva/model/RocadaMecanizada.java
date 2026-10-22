package br.com.motiva.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(RocadaMecanizada.CODIGO)
public class RocadaMecanizada extends IntervencaoOperacional {

    public static final String CODIGO = "ROCADA_MECANIZADA";

    @Column(name = "URGENTE")
    private boolean urgente;

    protected RocadaMecanizada() {
        super(CODIGO);
    }

    public RocadaMecanizada(boolean urgente) {
        super(CODIGO);
        this.urgente = urgente;
    }

    public boolean isUrgente() {
        return urgente;
    }

    @Override
    public String getDescricao() {
        return "Roçada Mecanizada" + (urgente ? " (Urgente)" : "");
    }

    @Override
    public String descreverServico() {
        return urgente
                ? "Executar roçada mecanizada com URGÊNCIA: vegetação acima do limite de segurança."
                : "Executar roçada mecanizada.";
    }
}
