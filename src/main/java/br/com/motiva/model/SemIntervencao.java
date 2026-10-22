package br.com.motiva.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(SemIntervencao.CODIGO)
public class SemIntervencao extends IntervencaoOperacional {

    public static final String CODIGO = "SEM_INTERVENCAO";

    public SemIntervencao() {
        super(CODIGO);
    }

    @Override
    public String getDescricao() {
        return "Sem Intervenção";
    }

    @Override
    public String descreverServico() {
        return "Nenhuma intervenção necessária no momento.";
    }
}
