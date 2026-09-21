package service;

import dao.RelatorioPrioridadeDAO;
import model.IntervencaoOperacional;
import model.Pulverizacao;
import model.RocadaMecanizada;
import model.TrechoRodovia;

import java.sql.SQLException;

public class GeradorRelatorio {

    private final RelatorioPrioridadeDAO relatorioDAO;
    private final IntervencaoService intervencaoService;

    public GeradorRelatorio() {
        this(new RelatorioPrioridadeDAO());
    }

    public GeradorRelatorio(RelatorioPrioridadeDAO relatorioDAO) {
        this.relatorioDAO = relatorioDAO;
        this.intervencaoService = new IntervencaoService();
    }

    public void gerarRelatorio(TrechoRodovia[] trechos) {

        imprimirCabecalho();

        int qtUrgente = 0;
        int qtCritico = 0;
        int qtAtencao = 0;
        int qtNormal = 0;

        for (TrechoRodovia trecho : trechos) {

            IntervencaoOperacional intervencao = intervencaoService.decidirIntervencao(trecho);

            imprimirLinhaTrecho(trecho, intervencao);

            if (intervencao instanceof RocadaMecanizada rocada) {
                if (rocada.isUrgente()) {
                    qtUrgente++;
                } else {
                    qtCritico++;
                }
            } else if (intervencao instanceof Pulverizacao) {
                qtAtencao++;
            } else {
                qtNormal++;
            }
        }

        String resumo = montarResumo(qtUrgente, qtCritico, qtAtencao, qtNormal);
        imprimirResumo(resumo);

        // NOVO: salvar o resultado no banco
        try {
            Long id = relatorioDAO.salvarRelatorio(qtUrgente, qtCritico, qtAtencao, qtNormal, resumo);
            System.out.println("Relatório salvo no histórico do banco (ID " + id + ").");
        } catch (SQLException e) {
            System.err.println("Não foi possível salvar o relatório no banco: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void imprimirCabecalho() {
        System.out.println("\n===== RELATÓRIO DE PRIORIDADE =====");
    }

    private void imprimirLinhaTrecho(TrechoRodovia trecho, IntervencaoOperacional intervencao) {
        System.out.println("\nRodovia: " + trecho.getNomeRodovia());
        System.out.println("KM Inicial: " + trecho.getQuilometroInicial());
        System.out.println("KM Final: " + trecho.getQuilometroFinal());
        System.out.println("Vegetação: " + trecho.getNivelVegetacao() + " cm");
        System.out.println("Intervenção: " + intervencao.getTipoIntervencao());

        intervencao.executarServico(trecho);
    }

    private String montarResumo(int qtUrgente, int qtCritico, int qtAtencao, int qtNormal) {
        return String.format(
                "Urgente: %d | Crítico: %d | Atenção: %d | Normal: %d",
                qtUrgente, qtCritico, qtAtencao, qtNormal
        );
    }

    private void imprimirResumo(String resumo) {
        System.out.println("\n===== RESUMO =====");
        System.out.println(resumo);
    }
}
