package model;

public class RocadaMecanizada extends IntervencaoOperacional {

    private final boolean urgente;

    public RocadaMecanizada() {
        this(false);
    }

    public RocadaMecanizada(boolean urgente) {
        this.urgente = urgente;
    }

    public boolean isUrgente() {
        return urgente;
    }

    @Override
    public void executarServico(TrechoRodovia trecho) {
        System.out.println("Serviço executado: Roçada Mecanizada"
                + (urgente ? " (URGENTE)" : ""));
    }

    @Override
    public String getTipoIntervencao() {
        return "Roçada Mecanizada" + (urgente ? " (Urgente)" : "");
    }
}
