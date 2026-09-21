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

record IntervencaoRegistro(
        Long id,
        Long idTrecho,
        String tipoIntervencao,
        LocalDateTime dataExecucao,
        String observacao
) {
}

public class IntervencaoOperacionalDAO {

    private static final String SQL_INSERIR =
            "INSERT INTO INTERVENCAO_OPERACIONAL (ID_TRECHO, TIPO_INTERVENCAO, DATA_EXECUCAO, OBSERVACAO) "
                    + "VALUES (?, ?, ?, ?)";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT ID, ID_TRECHO, TIPO_INTERVENCAO, DATA_EXECUCAO, OBSERVACAO "
                    + "FROM INTERVENCAO_OPERACIONAL WHERE ID = ?";

    private static final String SQL_LISTAR_TODAS =
            "SELECT ID, ID_TRECHO, TIPO_INTERVENCAO, DATA_EXECUCAO, OBSERVACAO "
                    + "FROM INTERVENCAO_OPERACIONAL ORDER BY ID";

    private static final String SQL_ATUALIZAR =
            "UPDATE INTERVENCAO_OPERACIONAL SET ID_TRECHO = ?, TIPO_INTERVENCAO = ?, "
                    + "DATA_EXECUCAO = ?, OBSERVACAO = ? WHERE ID = ?";

    private static final String SQL_DELETAR =
            "DELETE FROM INTERVENCAO_OPERACIONAL WHERE ID = ?";

    public IntervencaoOperacionalDAO() {
    }

    public Long inserir(IntervencaoRegistro intervencao) throws SQLException {
        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conn.prepareStatement(SQL_INSERIR, new String[]{"ID"})) {
            stmt.setLong(1, intervencao.idTrecho());
            stmt.setString(2, intervencao.tipoIntervencao());
            stmt.setTimestamp(3, Timestamp.valueOf(intervencao.dataExecucao()));
            stmt.setString(4, intervencao.observacao());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return null;
    }

    public IntervencaoRegistro buscarPorId(Long id) throws SQLException {
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

    public List<IntervencaoRegistro> listarTodas() throws SQLException {
        Connection conn = ConexaoBD.getInstancia().conectar();
        List<IntervencaoRegistro> intervencoes = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(SQL_LISTAR_TODAS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                intervencoes.add(mapear(rs));
            }
        }
        return intervencoes;
    }

    public void atualizar(IntervencaoRegistro intervencao) throws SQLException {
        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conn.prepareStatement(SQL_ATUALIZAR)) {
            stmt.setLong(1, intervencao.idTrecho());
            stmt.setString(2, intervencao.tipoIntervencao());
            stmt.setTimestamp(3, Timestamp.valueOf(intervencao.dataExecucao()));
            stmt.setString(4, intervencao.observacao());
            stmt.setLong(5, intervencao.id());
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

    private IntervencaoRegistro mapear(ResultSet rs) throws SQLException {
        return new IntervencaoRegistro(
                rs.getLong("ID"),
                rs.getLong("ID_TRECHO"),
                rs.getString("TIPO_INTERVENCAO"),
                rs.getTimestamp("DATA_EXECUCAO").toLocalDateTime(),
                rs.getString("OBSERVACAO")
        );
    }
}
