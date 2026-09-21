package model;

public class EquipeManutencao {

    private Long id;
    private String nomeEquipe;
    private String responsavel;

    public EquipeManutencao(String nomeEquipe, String responsavel) {
        this.nomeEquipe = nomeEquipe;
        this.responsavel = responsavel;
    }

    public void associarTrecho(TrechoRodovia trecho) {

        if (!trecho.trechoCritico()) {
            System.out.println("\nTrecho não necessita intervenção imediata.");
            return;
        }

        System.out.println("\n===== EQUIPE ASSOCIADA =====");
        System.out.println("Equipe: " + nomeEquipe);
        System.out.println("Responsável: " + responsavel);
        System.out.println("Trecho crítico: " + trecho.getNomeRodovia());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeEquipe() {
        return nomeEquipe;
    }

    public String getResponsavel() {
        return responsavel;
    }
}
