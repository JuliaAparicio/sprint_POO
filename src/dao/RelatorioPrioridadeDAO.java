package dao;

import db.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


record RelatorioPrioridadeRegistro(
        Long id,
        LocalDateTime dataGeracao,
        int qtUrgente,
        int qtCritico,
        int qtAtencao,
        int qtNormal,
        String resumo
) {
}

public class RelatorioPrioridadeDAO {

    private static final String SQL_INSERIR =
            "INSERT INTO RELATORIO_PRIORIDADE "
                    + "(DATA_GERACAO, QT_URGENTE, QT_CRITICO, QT_ATENCAO, QT_NORMAL, RESUMO) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT ID, DATA_GERACAO, QT_URGENTE, QT_CRITICO, QT_ATENCAO, QT_NORMAL, RESUMO "
                    + "FROM RELATORIO_PRIORIDADE WHERE ID = ?";

    private static final String SQL_LISTAR_TODAS =
            "SELECT ID, DATA_GERACAO, QT_URGENTE, QT_CRITICO, QT_ATENCAO, QT_NORMAL, RESUMO "
                    + "FROM RELATORIO_PRIORIDADE ORDER BY ID";

    private static final String SQL_ATUALIZAR =
            "UPDATE RELATORIO_PRIORIDADE SET DATA_GERACAO = ?, QT_URGENTE = ?, QT_CRITICO = ?, "
                    + "QT_ATENCAO = ?, QT_NORMAL = ?, RESUMO = ? WHERE ID = ?";

    private static final String SQL_DELETAR =
            "DELETE FROM RELATORIO_PRIORIDADE WHERE ID = ?";

    public RelatorioPrioridadeDAO() {
    }

    public Long inserir(RelatorioPrioridadeRegistro relatorio) throws SQLException {
        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conn.prepareStatement(SQL_INSERIR, new String[]{"ID"})) {
            preencherParametros(stmt, relatorio);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return null;
    }

    /**
     * Atalho usado pelo GeradorRelatorio para persistir o resultado de uma
     * geração de relatório, sem que o serviço precise montar o record.
     */
    public Long salvarRelatorio(int qtUrgente, int qtCritico, int qtAtencao, int qtNormal, String resumo)
            throws SQLException {

        RelatorioPrioridadeRegistro registro = new RelatorioPrioridadeRegistro(
                null,
                LocalDateTime.now(),
                qtUrgente,
                qtCritico,
                qtAtencao,
                qtNormal,
                resumo
        );
        return inserir(registro);
    }

    public RelatorioPrioridadeRegistro buscarPorId(Long id) throws SQLException {
        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conn.prepareStatement(SQL_BUSCAR_POR_ID)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    public List<RelatorioPrioridadeRegistro> listarTodas() throws SQLException {
        Connection conn = ConexaoBD.getInstancia().conectar();
        List<RelatorioPrioridadeRegistro> relatorios = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(SQL_LISTAR_TODAS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                relatorios.add(mapear(rs));
            }
        }
        return relatorios;
    }

    public void atualizar(RelatorioPrioridadeRegistro relatorio) throws SQLException {
        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conn.prepareStatement(SQL_ATUALIZAR)) {
            preencherParametros(stmt, relatorio);
            stmt.setLong(7, relatorio.id());
            stmt.executeUpdate();
        }
    }

    public void deletar(Long id) throws SQLException {
        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conn.prepareStatement(SQL_DELETAR)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    private void preencherParametros(PreparedStatement stmt, RelatorioPrioridadeRegistro relatorio)
            throws SQLException {
        stmt.setTimestamp(1, Timestamp.valueOf(relatorio.dataGeracao()));
        stmt.setInt(2, relatorio.qtUrgente());
        stmt.setInt(3, relatorio.qtCritico());
        stmt.setInt(4, relatorio.qtAtencao());
        stmt.setInt(5, relatorio.qtNormal());
        stmt.setString(6, relatorio.resumo());
    }

    private RelatorioPrioridadeRegistro mapear(ResultSet rs) throws SQLException {
        return new RelatorioPrioridadeRegistro(
                rs.getLong("ID"),
                rs.getTimestamp("DATA_GERACAO").toLocalDateTime(),
                rs.getInt("QT_URGENTE"),
                rs.getInt("QT_CRITICO"),
                rs.getInt("QT_ATENCAO"),
                rs.getInt("QT_NORMAL"),
                rs.getString("RESUMO")
        );
    }
}
