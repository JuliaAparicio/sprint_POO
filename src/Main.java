public class Main {

    public static void main(String[] args) {

        TrechoRodovia trecho1 =
                new TrechoRodovia(
                        "BR-116",
                        10,
                        15,
                        12
                );

        TrechoRodovia trecho2 =
                new TrechoRodovia(
                        "BR-101",
                        20,
                        25,
                        28
                );

        trecho1.registrarCrescimento(5);

        trecho2.registrarCrescimento(10);

        trecho1.exibirInformacoes();

        trecho2.exibirInformacoes();

        EquipeManutencao equipe =
                new EquipeManutencao(
                        "Equipe Sul",
                        "Carlos Silva"
                );

        if (trecho2.trechoCritico()) {

            equipe.associarTrecho(trecho2);
        }

        /* TESTES MANUAIS */

        System.out.println("\n===== TESTES MANUAIS =====");

        /* TESTE 1 */

        TrechoRodovia teste1 =
                new TrechoRodovia(
                        "BR-050",
                        1,
                        5,
                        10
                );

        teste1.registrarCrescimento(5);

        System.out.println("\nTeste 1 - Crescimento");

        System.out.println(
                "Resultado esperado: 15"
        );

        System.out.println(
                "Resultado obtido: "
                        + teste1.getNivelVegetacao()
        );

        /* TESTE 2 */

        TrechoRodovia teste2 =
                new TrechoRodovia(
                        "BR-040",
                        2,
                        8,
                        10
                );

        teste2.setNivelVegetacao(-5);

        System.out.println("\nTeste 2 - Valor Negativo");

        System.out.println(
                "Resultado esperado: 0"
        );

        System.out.println(
                "Resultado obtido: "
                        + teste2.getNivelVegetacao()
        );
    }
}