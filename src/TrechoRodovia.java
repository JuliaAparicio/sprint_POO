public class TrechoRodovia {

    private String nomeRodovia;
    private double quilometroInicial;
    private double quilometroFinal;
    private double nivelVegetacao;

    public TrechoRodovia(String nomeRodovia,
                         double quilometroInicial,
                         double quilometroFinal,
                         double nivelVegetacao) {

        this.nomeRodovia = nomeRodovia;
        this.quilometroInicial = quilometroInicial;
        this.quilometroFinal = quilometroFinal;

        setNivelVegetacao(nivelVegetacao);
    }

    public void registrarCrescimento(double taxa) {

        if (taxa < 0) {
            System.out.println("A taxa não pode ser negativa.");
            return;
        }

        this.nivelVegetacao += taxa;
    }

    public boolean trechoCritico() {
        return nivelVegetacao >= 30;
    }

    public void exibirInformacoes() {

        System.out.println("\n===== TRECHO =====");

        System.out.println("Rodovia: " + nomeRodovia);

        System.out.println("KM Inicial: " + quilometroInicial);

        System.out.println("KM Final: " + quilometroFinal);

        System.out.println("Vegetação: " + nivelVegetacao + " cm");

        if (trechoCritico()) {
            System.out.println("Status: CRÍTICO");
        } else {
            System.out.println("Status: Normal");
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

    public void setNivelVegetacao(double nivelVegetacao) {

        if (nivelVegetacao < 0) {

            this.nivelVegetacao = 0;

        } else {

            this.nivelVegetacao = nivelVegetacao;
        }
    }
}