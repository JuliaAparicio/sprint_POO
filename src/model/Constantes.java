package model;

public final class Constantes {

    private Constantes() {
    }

    // Taxas de crescimento da vegetação (cm), conforme tipo de terreno
    public static final double TAXA_CRESCIMENTO_TERRENO_UMIDO = 10.0;
    public static final double TAXA_CRESCIMENTO_TERRENO_SECO = 5.0;

    // Limiares (cm) que definem o nível de intervenção necessário
    public static final double LIMIAR_ATENCAO = 15.0;  // abaixo disso: sem intervenção
    public static final double LIMIAR_CRITICO = 30.0;  // igual/acima disso: roçada mecanizada
    public static final double LIMIAR_URGENTE = 45.0;  // igual/acima disso: roçada mecanizada urgente

    public static final String TERRENO_UMIDO = "UMIDO";
}
