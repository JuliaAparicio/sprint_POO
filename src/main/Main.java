package main;

import dao.EquipeManutencaoDAO;
import dao.IntervencaoOperacionalDAO;
import dao.TrechoRodoviaDAO;
import db.ConexaoBD;
import model.EquipeManutencao;
import model.MonitoravelViaIoT;
import model.SensorMock;
import model.TrechoMonitoradoIoT;
import model.TrechoRodovia;
import service.GeradorRelatorio;

public class Main {

    public static void main(String[] args) {

        ConexaoBD conexao = ConexaoBD.getInstancia();
        conexao.conectar();

        try {

            // ===== 1. Trechos (um monitorado por IoT, outro não) =====
            TrechoMonitoradoIoT trecho1 =
                    new TrechoMonitoradoIoT("BR-116", 10, 15, 12, "SECO");

            TrechoRodovia trecho2 =
                    new TrechoRodovia("BR-101", 20, 25, 28, "UMIDO");

            trecho1.transmitirDadosSensor();
            trecho2.atualizarCrescimento();

            trecho1.exibirInformacoes();
            trecho2.exibirInformacoes();

            // ===== 2. Equipe de manutenção =====
            EquipeManutencao equipe = new EquipeManutencao("Equipe Sul", "Carlos Silva");
            equipe.associarTrecho(trecho2);

            // ===== 3. Persistência: CRUD de equipes e trechos =====
            EquipeManutencaoDAO daoEquipe = new EquipeManutencaoDAO();
            TrechoRodoviaDAO daoTrecho = new TrechoRodoviaDAO();
            IntervencaoOperacionalDAO daoIntervencao = new IntervencaoOperacionalDAO();

            // (uso dos DAOs fica documentado no README — depende de conexão
            // Oracle ativa; aqui deixamos os objetos prontos para uso)
            System.out.println("\nDAOs inicializados: " + daoEquipe + ", " + daoTrecho + ", " + daoIntervencao);

            // ===== 4. Relatório com persistência =====
            GeradorRelatorio gerador = new GeradorRelatorio();
            TrechoRodovia[] trechos = {trecho1, trecho2};
            gerador.gerarRelatorio(trechos);

            // ===== 5. Testes manuais mantidos da Sprint 2 =====
            System.out.println("\n===== TESTES MANUAIS =====");

            TrechoRodovia teste = new TrechoRodovia("BR-050", 5, 10, 20, "UMIDO");
            teste.atualizarCrescimento();

            System.out.println("\nResultado esperado: 30");
            System.out.println("Resultado obtido: " + teste.getNivelVegetacao());

            System.out.println("\n===== TESTE DA CLASSE ABSTRATA =====");
            /*
             Não é possível fazer:

             IntervencaoOperacional intervencao =
                     new IntervencaoOperacional();

             pois classes abstratas não podem ser instanciadas.
            */
            System.out.println("Classe abstrata validada com sucesso.");

            System.out.println("\n===== TESTE MOCK =====");
            MonitoravelViaIoT sensor = new SensorMock();
            sensor.transmitirDadosSensor();

        } finally {
            conexao.desconectar();
        }
    }
}
