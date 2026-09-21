package model;


public class TrechoMonitoradoIoT extends TrechoRodovia implements MonitoravelViaIoT {

    public TrechoMonitoradoIoT(
            String nomeRodovia,
            double quilometroInicial,
            double quilometroFinal,
            double nivelVegetacao,
            String tipoTerreno
    ) {
        super(nomeRodovia, quilometroInicial, quilometroFinal, nivelVegetacao, tipoTerreno);
    }

    @Override
    public void transmitirDadosSensor() {
        atualizarCrescimento();
        System.out.println("Dados recebidos via sensor IoT.");
    }
}
