public class TrechoRodovia
        implements MonitoravelViaIoT {

    private String nomeRodovia;
    private double quilometroInicial;
    private double quilometroFinal;
    private double nivelVegetacao;
    private String tipoTerreno;

    public TrechoRodovia(
            String nomeRodovia,
            double quilometroInicial,
            double quilometroFinal,
            double nivelVegetacao,
            String tipoTerreno
    ) {

        this.nomeRodovia = nomeRodovia;

        this.quilometroInicial =
                quilometroInicial;

        if (quilometroFinal <= quilometroInicial) {

            this.quilometroFinal =
                    quilometroInicial + 1;

        } else {

            this.quilometroFinal =
                    quilometroFinal;
        }

        this.tipoTerreno = tipoTerreno;

        setNivelVegetacao(
                nivelVegetacao
        );
    }

    public void registrarCrescimento(
            double taxa
    ) {

        if (taxa < 0) {

            System.out.println(
                    "A taxa não pode ser negativa."
            );

            return;
        }

        nivelVegetacao += taxa;
    }

    public void atualizarCrescimento() {

        if (
                tipoTerreno.equalsIgnoreCase(
                        "UMIDO"
                )
        ) {

            registrarCrescimento(10);

        } else {

            registrarCrescimento(5);
        }
    }

    @Override
    public void transmitirDadosSensor() {

        atualizarCrescimento();

        System.out.println(
                "Dados recebidos via sensor IoT."
        );
    }

    public boolean trechoCritico() {

        return nivelVegetacao >= 30;
    }

    public IntervencaoOperacional definirIntervencao() {

        if (nivelVegetacao >= 30) {

            return new RocadaMecanizada();

        } else {

            return new Pulverizacao();
        }
    }

    public void exibirInformacoes() {

        System.out.println(
                "\n===== TRECHO ====="
        );

        System.out.println(
                "Rodovia: " + nomeRodovia
        );

        System.out.println(
                "KM Inicial: "
                        + quilometroInicial
        );

        System.out.println(
                "KM Final: "
                        + quilometroFinal
        );

        System.out.println(
                "Vegetação: "
                        + nivelVegetacao
                        + " cm"
        );

        System.out.println(
                "Terreno: "
                        + tipoTerreno
        );

        if (trechoCritico()) {

            System.out.println(
                    "Status: CRÍTICO"
            );

        } else {

            System.out.println(
                    "Status: NORMAL"
            );
        }
    }

    public String getNomeRodovia() {
        return nomeRodovia;
    }

    public double getQuilometroInicial() {
        return quilometroInicial;
    }

    public double getQuilometroFinal() {
        return quilometroFinal;
    }

    public double getNivelVegetacao() {
        return nivelVegetacao;
    }

    public void setNivelVegetacao(
            double nivelVegetacao
    ) {

        if (nivelVegetacao < 0) {

            this.nivelVegetacao = 0;

        } else {

            this.nivelVegetacao =
                    nivelVegetacao;
        }
    }
}