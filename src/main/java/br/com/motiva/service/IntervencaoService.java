package br.com.motiva.service;

import br.com.motiva.model.Constantes;
import br.com.motiva.model.IntervencaoOperacional;
import br.com.motiva.model.NivelPrioridade;
import br.com.motiva.model.Pulverizacao;
import br.com.motiva.model.RocadaMecanizada;
import br.com.motiva.model.SemIntervencao;
import br.com.motiva.model.TrechoRodovia;
import org.springframework.stereotype.Service;

/**
 * Motor de regras (Sprints 2/3): classifica o trecho e decide a intervenção.
 * Vive no Service, nunca no Controller.
 */
@Service
public class IntervencaoService {

    public NivelPrioridade classificar(double nivelVegetacao) {
        if (nivelVegetacao >= Constantes.LIMIAR_URGENTE) {
            return NivelPrioridade.URGENTE;
        }
        if (nivelVegetacao >= Constantes.LIMIAR_CRITICO) {
            return NivelPrioridade.CRITICO;
        }
        if (nivelVegetacao >= Constantes.LIMIAR_ATENCAO) {
            return NivelPrioridade.ATENCAO;
        }
        return NivelPrioridade.NORMAL;
    }

    public NivelPrioridade classificar(TrechoRodovia trecho) {
        return classificar(trecho.getNivelVegetacao());
    }

    public IntervencaoOperacional decidirIntervencao(TrechoRodovia trecho) {
        return switch (classificar(trecho)) {
            case URGENTE -> new RocadaMecanizada(true);
            case CRITICO -> new RocadaMecanizada(false);
            case ATENCAO -> new Pulverizacao();
            case NORMAL -> new SemIntervencao();
        };
    }
}
