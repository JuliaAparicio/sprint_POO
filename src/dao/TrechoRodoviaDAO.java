package dao;

import db.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Linha da tabela TRECHO_RODOVIA.
 */
record TrechoRodoviaRegistro(
        Long id,
        String nomeRodovia,
        double quilometroInicial,
        double quilometroFinal,
        double nivelVegetacao,
        String tipoTerreno,
        boolean monitoradoIot
) {
}

public class TrechoRodoviaDAO {

    private static final String SQL_INSERIR =
            "INSERT INTO TRECHO_RODOVIA "
                    + "(NOME_RODOVIA, KM_INICIAL, KM_FINAL, NIVEL_VEGETACAO, TIPO_TERRENO, MONITORADO_IOT) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT ID, NOME_RODOVIA, KM_INICIAL, KM_FINAL, NIVEL_VEGETACAO, TIPO_TERRENO, MONITORADO_IOT "
                    + "FROM TRECHO_RODOVIA WHERE ID = ?";

    private static final String SQL_LISTAR_TODAS =
            "SELECT ID, NOME_RODOVIA, KM_INICIAL, KM_FINAL, NIVEL_VEGETACAO, TIPO_TERRENO, MONITORADO_IOT "
                    + "FROM TRECHO_RODOVIA ORDER BY ID";

    private static final String SQL_ATUALIZAR =
            "UPDATE TRECHO_RODOVIA SET NOME_RODOVIA = ?, KM_INICIAL = ?, KM_FINAL = ?, "
                    + "NIVEL_VEGETACAO = ?, TIPO_TERRENO = ?, MONITORADO_IOT = ? WHERE ID = ?";

    private static final String SQL_DELETAR =
            "DELETE FROM TRECHO_RODOVIA WHERE ID = ?";

    public TrechoRodoviaDAO() {
    }

    public Long inserir(TrechoRodoviaRegistro trecho) throws SQLException {
        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conn.prepareStatement(SQL_INSERIR, new String[]{"ID"})) {
            preencherParametros(stmt, trecho);
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return null;
    }

    public TrechoRodoviaRegistro buscarPorId(Long id) throws SQLException {
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

    public List<TrechoRodoviaRegistro> listarTodas() throws SQLException {
        Connection conn = ConexaoBD.getInstancia().conectar();
        List<TrechoRodoviaRegistro> trechos = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(SQL_LISTAR_TODAS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                trechos.add(mapear(rs));
            }
        }
        return trechos;
    }

    public void atualizar(TrechoRodoviaRegistro trecho) throws SQLException {
        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conn.prepareStatement(SQL_ATUALIZAR)) {
            preencherParametros(stmt, trecho);
            stmt.setLong(7, trecho.id());
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

    private void preencherParametros(PreparedStatement stmt, TrechoRodoviaRegistro trecho) throws SQLException {
        stmt.setString(1, trecho.nomeRodovia());
        stmt.setDouble(2, trecho.quilometroInicial());
        stmt.setDouble(3, trecho.quilometroFinal());
        stmt.setDouble(4, trecho.nivelVegetacao());
        stmt.setString(5, trecho.tipoTerreno());
        stmt.setInt(6, trecho.monitoradoIot() ? 1 : 0);
    }

    private TrechoRodoviaRegistro mapear(ResultSet rs) throws SQLException {
        return new TrechoRodoviaRegistro(
                rs.getLong("ID"),
                rs.getString("NOME_RODOVIA"),
                rs.getDouble("KM_INICIAL"),
                rs.getDouble("KM_FINAL"),
                rs.getDouble("NIVEL_VEGETACAO"),
                rs.getString("TIPO_TERRENO"),
                rs.getInt("MONITORADO_IOT") == 1
        );
    }
}
