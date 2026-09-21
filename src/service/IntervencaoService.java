package service;

import model.Constantes;
import model.IntervencaoOperacional;
import model.Pulverizacao;
import model.RocadaMecanizada;
import model.SemIntervencao;
import model.TrechoRodovia;

public class IntervencaoService {

    public IntervencaoOperacional decidirIntervencao(TrechoRodovia trecho) {
        double nivel = trecho.getNivelVegetacao();

        if (nivel >= Constantes.LIMIAR_CRITICO) {
            boolean urgente = nivel >= Constantes.LIMIAR_URGENTE;
            return new RocadaMecanizada(urgente);
        }

        if (nivel >= Constantes.LIMIAR_ATENCAO) {
            return new Pulverizacao();
        }

        return new SemIntervencao();
    }
}
