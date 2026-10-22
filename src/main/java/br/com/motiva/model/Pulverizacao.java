package br.com.motiva.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue(Pulverizacao.CODIGO)
public class Pulverizacao extends IntervencaoOperacional {

    public static final String CODIGO = "PULVERIZACAO";

    public Pulverizacao() {
        super(CODIGO);
    }

    @Override
    public String getDescricao() {
        return "Pulverização";
    }

    @Override
    public String descreverServico() {
        return "Aplicar pulverização para conter o crescimento da vegetação.";
    }
}
