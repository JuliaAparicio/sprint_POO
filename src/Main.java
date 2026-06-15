public class Main {

    public static void main(String[] args) {

        TrechoRodovia trecho1 =
                new TrechoRodovia(
                        "BR-116",
                        10,
                        15,
                        12,
                        "SECO"
                );

        TrechoRodovia trecho2 =
                new TrechoRodovia(
                        "BR-101",
                        20,
                        25,
                        28,
                        "UMIDO"
                );

        trecho1.transmitirDadosSensor();
        trecho2.transmitirDadosSensor();

        trecho1.exibirInformacoes();
        trecho2.exibirInformacoes();

        EquipeManutencao equipe =
                new EquipeManutencao(
                        "Equipe Sul",
                        "Carlos Silva"
                );

        equipe.associarTrecho(trecho2);

        TrechoRodovia[] trechos = {
                trecho1,
                trecho2
        };

        System.out.println(
                "\n===== RELATÓRIO DE PRIORIDADE ====="
        );

        for (TrechoRodovia trecho : trechos) {

            IntervencaoOperacional intervencao =
                    trecho.definirIntervencao();

            System.out.println(
                    "\nRodovia: "
                            + trecho.getNomeRodovia()
            );

            System.out.println(
                    "KM Inicial: "
                            + trecho.getQuilometroInicial()
            );

            System.out.println(
                    "KM Final: "
                            + trecho.getQuilometroFinal()
            );

            System.out.println(
                    "Vegetação: "
                            + trecho.getNivelVegetacao()
                            + " cm"
            );

            System.out.println(
                    "Intervenção: "
                            + intervencao.getTipoIntervencao()
            );

            intervencao.executarServico(
                    trecho
            );
        }

        System.out.println(
                "\n===== TESTES MANUAIS ====="
        );

        TrechoRodovia teste =
                new TrechoRodovia(
                        "BR-050",
                        5,
                        10,
                        20,
                        "UMIDO"
                );

        teste.transmitirDadosSensor();

        System.out.println(
                "\nResultado esperado: 30"
        );

        System.out.println(
                "Resultado obtido: "
                        + teste.getNivelVegetacao()
        );

        System.out.println(
                "\n===== TESTE DA CLASSE ABSTRATA ====="
        );

        /*
         Não é possível fazer:

         IntervencaoOperacional intervencao =
                 new IntervencaoOperacional();

         pois classes abstratas não podem
         ser instanciadas.
        */

        System.out.println(
                "Classe abstrata validada com sucesso."
        );

        System.out.println(
                "\n===== TESTE MOCK ====="
        );

        MonitoravelViaIoT sensor =
                new SensorMock();

        sensor.transmitirDadosSensor();
    }
}