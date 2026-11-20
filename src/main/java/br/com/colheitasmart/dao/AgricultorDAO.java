
package br.com.colheitasmart.dao;

import br.com.colheitasmart.model.Agricultor;
import br.com.colheitasmart.exception.PersistenciaException;

import java.sql.*;
import java.util.*;
import javax.sql.DataSource;

import org.springframework.stereotype.Repository;

@Repository
public class AgricultorDAO {

    private final DataSource dataSource;

    public AgricultorDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private Agricultor mapear(ResultSet rs) throws SQLException {
        Agricultor item = new Agricultor();

        item.setCdAgricultor(rs.getLong("cd_agricultor"));
        item.setNmAgricultor(rs.getString("nm_agricultor"));
        item.setDsEmail(rs.getString("ds_email"));
        item.setNrTelefone(rs.getString("nr_telefone"));
        item.setNmCidade(rs.getString("nm_cidade"));

        return item;
    }

    private Agricultor mapearComSenha(ResultSet rs) throws SQLException {
        Agricultor item = mapear(rs);
        item.setDsSenhaHash(rs.getString("ds_senha_hash"));
        return item;
    }

    public List<Agricultor> listar() {
        String sql = """
            SELECT cd_agricultor, nm_agricultor, ds_email,
                   nr_telefone, nm_cidade
            FROM tb_agricultor
            ORDER BY cd_agricultor
            """;

        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            List<Agricultor> lista = new ArrayList<>();

            while (rs.next()) {
                lista.add(mapear(rs));
            }

            return lista;

        } catch (SQLException ex) {
            throw new PersistenciaException(ex);
        }
    }

    public Optional<Agricultor> buscarPorId(Long id) {
        String sql = """
            SELECT cd_agricultor, nm_agricultor, ds_email,
                   nr_telefone, nm_cidade
            FROM tb_agricultor
            WHERE cd_agricultor = ?
            """;

        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next()
                    ? Optional.of(mapear(rs))
                    : Optional.empty();
            }

        } catch (SQLException ex) {
            throw new PersistenciaException(ex);
        }
    }

    // Usado exclusivamente pela autenticacao.
    public Optional<Agricultor> buscarPorEmail(String email) {
        String sql = """
            SELECT cd_agricultor, nm_agricultor, ds_email,
                   nr_telefone, nm_cidade, ds_senha_hash
            FROM tb_agricultor
            WHERE LOWER(ds_email) = LOWER(?)
            """;

        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, email.trim());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next()
                    ? Optional.of(mapearComSenha(rs))
                    : Optional.empty();
            }

        } catch (SQLException ex) {
            throw new PersistenciaException(ex);
        }
    }

    public Agricultor cadastrar(Agricultor item) {
        String sql = """
            INSERT INTO tb_agricultor (
                cd_agricultor,
                nm_agricultor,
                ds_email,
                nr_telefone,
                nm_cidade,
                ds_senha_hash
            )
            VALUES (
                sq_tb_agricultor.NEXTVAL,
                ?, ?, ?, ?, ?
            )
            """;

        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(
                 sql, new String[]{"CD_AGRICULTOR"})) {

            ps.setString(1, item.getNmAgricultor());
            ps.setString(2, item.getDsEmail());
            ps.setString(3, item.getNrTelefone());
            ps.setString(4, item.getNmCidade());
            ps.setString(5, item.getDsSenhaHash());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) {
                    throw new SQLException("Chave nao retornada");
                }

                item.setCdAgricultor(keys.getLong(1));
            }

            return item;

        } catch (SQLException ex) {
            throw new PersistenciaException(ex);
        }
    }

    public boolean atualizar(Long id, Agricultor item) {
        String sql = """
            UPDATE tb_agricultor
            SET nm_agricultor = ?,
                ds_email = ?,
                nr_telefone = ?,
                nm_cidade = ?
            WHERE cd_agricultor = ?
            """;

        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, item.getNmAgricultor());
            ps.setString(2, item.getDsEmail());
            ps.setString(3, item.getNrTelefone());
            ps.setString(4, item.getNmCidade());
            ps.setLong(5, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            throw new PersistenciaException(ex);
        }
    }

    public boolean remover(Long id) {
        String sql =
            "DELETE FROM tb_agricultor WHERE cd_agricultor = ?";

        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setLong(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            throw new PersistenciaException(ex);
        }
    }
}
