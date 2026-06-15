public class EquipeManutencao {

    private String nomeEquipe;
    private String responsavel;

    public EquipeManutencao(
            String nomeEquipe,
            String responsavel
    ) {

        this.nomeEquipe = nomeEquipe;
        this.responsavel = responsavel;
    }

    public void associarTrecho(
            TrechoRodovia trecho
    ) {

        if (!trecho.trechoCritico()) {

            System.out.println(
                    "\nTrecho não necessita intervenção imediata."
            );

            return;
        }

        System.out.println(
                "\n===== EQUIPE ASSOCIADA ====="
        );

        System.out.println(
                "Equipe: " + nomeEquipe
        );

        System.out.println(
                "Responsável: " + responsavel
        );

        System.out.println(
                "Trecho crítico: "
                        + trecho.getNomeRodovia()
        );
    }
}