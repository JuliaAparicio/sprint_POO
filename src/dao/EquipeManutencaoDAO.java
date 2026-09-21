package dao;

import db.ConexaoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

record EquipeManutencaoRegistro(Long id, String nomeEquipe, String responsavel) {
}

public class EquipeManutencaoDAO {

    private static final String SQL_INSERIR =
            "INSERT INTO EQUIPE_MANUTENCAO (NOME_EQUIPE, RESPONSAVEL) VALUES (?, ?)";

    private static final String SQL_BUSCAR_POR_ID =
            "SELECT ID, NOME_EQUIPE, RESPONSAVEL FROM EQUIPE_MANUTENCAO WHERE ID = ?";

    private static final String SQL_LISTAR_TODAS =
            "SELECT ID, NOME_EQUIPE, RESPONSAVEL FROM EQUIPE_MANUTENCAO ORDER BY ID";

    private static final String SQL_ATUALIZAR =
            "UPDATE EQUIPE_MANUTENCAO SET NOME_EQUIPE = ?, RESPONSAVEL = ? WHERE ID = ?";

    private static final String SQL_DELETAR =
            "DELETE FROM EQUIPE_MANUTENCAO WHERE ID = ?";

    public EquipeManutencaoDAO() {
    }

    public Long inserir(EquipeManutencaoRegistro equipe) throws SQLException {
        Connection conn = ConexaoBD.getInstancia().conectar();

        // try-with-resources fecha PreparedStatement/ResultSet automaticamente,
        // mesmo em caso de exceção (equivalente ao finally + close() manual).
        try (PreparedStatement stmt = conn.prepareStatement(SQL_INSERIR, new String[]{"ID"})) {
            stmt.setString(1, equipe.nomeEquipe());
            stmt.setString(2, equipe.responsavel());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getLong(1);
                }
            }
        }
        return null;
    }

    public EquipeManutencaoRegistro buscarPorId(Long id) throws SQLException {
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

    public List<EquipeManutencaoRegistro> listarTodas() throws SQLException {
        Connection conn = ConexaoBD.getInstancia().conectar();
        List<EquipeManutencaoRegistro> equipes = new ArrayList<>();

        try (PreparedStatement stmt = conn.prepareStatement(SQL_LISTAR_TODAS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                equipes.add(mapear(rs));
            }
        }
        return equipes;
    }

    public void atualizar(EquipeManutencaoRegistro equipe) throws SQLException {
        Connection conn = ConexaoBD.getInstancia().conectar();

        try (PreparedStatement stmt = conn.prepareStatement(SQL_ATUALIZAR)) {
            stmt.setString(1, equipe.nomeEquipe());
            stmt.setString(2, equipe.responsavel());
            stmt.setLong(3, equipe.id());
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

    private EquipeManutencaoRegistro mapear(ResultSet rs) throws SQLException {
        return new EquipeManutencaoRegistro(
                rs.getLong("ID"),
                rs.getString("NOME_EQUIPE"),
                rs.getString("RESPONSAVEL")
        );
    }
}
