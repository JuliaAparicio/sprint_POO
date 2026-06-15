public class Pulverizacao extends IntervencaoOperacional {

    @Override
    public void executarServico(TrechoRodovia trecho) {

        System.out.println(
                "Serviço executado: Pulverização"
        );
    }

    @Override
    public String getTipoIntervencao() {

        return "Pulverização";
    }
}