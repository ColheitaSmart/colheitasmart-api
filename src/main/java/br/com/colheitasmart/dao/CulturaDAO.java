package br.com.colheitasmart.dao;

import br.com.colheitasmart.model.Cultura;
import br.com.colheitasmart.exception.PersistenciaException;
import java.sql.*;
import java.util.*;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

@Repository
public class CulturaDAO {
    private final DataSource dataSource;

    public CulturaDAO(DataSource dataSource) { this.dataSource = dataSource; }

    private Cultura mapear(ResultSet rs) throws SQLException {
        Cultura item = new Cultura();
        item.setCdCultura(rs.getLong("cd_cultura"));
        item.setNmCultura(rs.getString("nm_cultura"));
        item.setDsCategoria(rs.getString("ds_categoria"));
        return item;
    }

    public List<Cultura> listar() {
        String sql = "SELECT cd_cultura, nm_cultura, ds_categoria FROM tb_cultura ORDER BY cd_cultura";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Cultura> lista = new ArrayList<>();
            while (rs.next()) lista.add(mapear(rs));
            return lista;
        } catch (SQLException ex) { throw new PersistenciaException(ex); }
    }

    public Optional<Cultura> buscarPorId(Long id) {
        String sql = "SELECT cd_cultura, nm_cultura, ds_categoria FROM tb_cultura WHERE cd_cultura = ?";
        try (Connection c = dataSource.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException ex) { throw new PersistenciaException(ex); }
    }

    public Cultura cadastrar(Cultura item) {
        String sql = "INSERT INTO tb_cultura (cd_cultura, nm_cultura, ds_categoria) VALUES (sq_tb_cultura.NEXTVAL, ?, ?)";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, new String[]{"CD_CULTURA"})) {
            ps.setString(1, item.getNmCultura());
            ps.setString(2, item.getDsCategoria());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) throw new SQLException("Chave nao retornada");
                item.setCdCultura(keys.getLong(1));
            }
            return item;
        } catch (SQLException ex) { throw new PersistenciaException(ex); }
    }

    public boolean atualizar(Long id, Cultura item) {
        String sql = "UPDATE tb_cultura SET nm_cultura = ?, ds_categoria = ? WHERE cd_cultura = ?";
        try (Connection c = dataSource.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, item.getNmCultura());
            ps.setString(2, item.getDsCategoria());
            ps.setLong(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { throw new PersistenciaException(ex); }
    }

    public boolean remover(Long id) {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM tb_cultura WHERE cd_cultura = ?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { throw new PersistenciaException(ex); }
    }

}
