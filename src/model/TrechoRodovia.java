package model;

public class TrechoRodovia {

    private Long id;
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
        this.quilometroInicial = quilometroInicial;

        if (quilometroFinal <= quilometroInicial) {
            this.quilometroFinal = quilometroInicial + 1;
        } else {
            this.quilometroFinal = quilometroFinal;
        }

        this.tipoTerreno = tipoTerreno;
        setNivelVegetacao(nivelVegetacao);
    }

    public void registrarCrescimento(double taxa) {

        if (taxa < 0) {
            System.out.println("A taxa não pode ser negativa.");
            return;
        }

        nivelVegetacao += taxa;
    }

    public void atualizarCrescimento() {

        if (tipoTerreno.equalsIgnoreCase(Constantes.TERRENO_UMIDO)) {
            registrarCrescimento(Constantes.TAXA_CRESCIMENTO_TERRENO_UMIDO);
        } else {
            registrarCrescimento(Constantes.TAXA_CRESCIMENTO_TERRENO_SECO);
        }
    }

    public boolean trechoCritico() {
        return nivelVegetacao >= Constantes.LIMIAR_CRITICO;
    }

    public void exibirInformacoes() {

        System.out.println("\n===== TRECHO =====");
        System.out.println("Rodovia: " + nomeRodovia);
        System.out.println("KM Inicial: " + quilometroInicial);
        System.out.println("KM Final: " + quilometroFinal);
        System.out.println("Vegetação: " + nivelVegetacao + " cm");
        System.out.println("Terreno: " + tipoTerreno);

        if (trechoCritico()) {
            System.out.println("Status: CRÍTICO");
        } else {
            System.out.println("Status: NORMAL");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setNivelVegetacao(double nivelVegetacao) {
        if (nivelVegetacao < 0) {
            this.nivelVegetacao = 0;
        } else {
            this.nivelVegetacao = nivelVegetacao;
        }
    }

    public String getTipoTerreno() {
        return tipoTerreno;
    }
}
