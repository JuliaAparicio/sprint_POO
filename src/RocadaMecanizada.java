public class RocadaMecanizada extends IntervencaoOperacional {

    @Override
    public void executarServico(TrechoRodovia trecho) {

        System.out.println(
                "Serviço executado: Roçada Mecanizada"
        );
    }

    @Override
    public String getTipoIntervencao() {

        return "Roçada Mecanizada";
    }
}