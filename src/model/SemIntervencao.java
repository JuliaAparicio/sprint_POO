package model;


public class SemIntervencao extends IntervencaoOperacional {

    @Override
    public void executarServico(TrechoRodovia trecho) {
        System.out.println("Nenhuma intervenção necessária no momento.");
    }

    @Override
    public String getTipoIntervencao() {
        return "Sem Intervenção";
    }
}
