package br.com.colheitasmart.dao;

import br.com.colheitasmart.model.Lote;
import br.com.colheitasmart.exception.PersistenciaException;
import java.sql.*;
import java.util.*;
import javax.sql.DataSource;
import org.springframework.stereotype.Repository;

@Repository
public class LoteDAO {
    private final DataSource dataSource;

    public LoteDAO(DataSource dataSource) { this.dataSource = dataSource; }

    private Lote mapear(ResultSet rs) throws SQLException {
        Lote item = new Lote();
        item.setCdLote(rs.getLong("cd_lote"));
        item.setCdAgricultor(rs.getLong("cd_agricultor"));
        item.setCdCultura(rs.getLong("cd_cultura"));
        item.setQtPesoInicialKg(rs.getBigDecimal("qt_peso_inicial_kg"));
        item.setQtPesoDisponivelKg(rs.getBigDecimal("qt_peso_disponivel_kg"));
        item.setDtColheita(rs.getDate("dt_colheita").toLocalDate());
        item.setDsArmazenamento(rs.getString("ds_armazenamento"));
        item.setVlPrecoKg(rs.getBigDecimal("vl_preco_kg"));
        item.setDsStatus(rs.getString("ds_status"));
        return item;
    }

    public List<Lote> listar() {
        String sql = "SELECT cd_lote, cd_agricultor, cd_cultura, qt_peso_inicial_kg, qt_peso_disponivel_kg, dt_colheita, ds_armazenamento, vl_preco_kg, ds_status FROM tb_lote ORDER BY cd_lote";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            List<Lote> lista = new ArrayList<>();
            while (rs.next()) lista.add(mapear(rs));
            return lista;
        } catch (SQLException ex) { throw new PersistenciaException(ex); }
    }

    public Optional<Lote> buscarPorId(Long id) {
        String sql = "SELECT cd_lote, cd_agricultor, cd_cultura, qt_peso_inicial_kg, qt_peso_disponivel_kg, dt_colheita, ds_armazenamento, vl_preco_kg, ds_status FROM tb_lote WHERE cd_lote = ?";
        try (Connection c = dataSource.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException ex) { throw new PersistenciaException(ex); }
    }

    public Lote cadastrar(Lote item) {
        String sql = "INSERT INTO tb_lote (cd_lote, cd_agricultor, cd_cultura, qt_peso_inicial_kg, qt_peso_disponivel_kg, dt_colheita, ds_armazenamento, vl_preco_kg, ds_status) VALUES (sq_tb_lote.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement(sql, new String[]{"CD_LOTE"})) {
            ps.setLong(1, item.getCdAgricultor());
            ps.setLong(2, item.getCdCultura());
            ps.setBigDecimal(3, item.getQtPesoInicialKg());
            ps.setBigDecimal(4, item.getQtPesoDisponivelKg());
            ps.setDate(5, java.sql.Date.valueOf(item.getDtColheita()));
            ps.setString(6, item.getDsArmazenamento());
            ps.setBigDecimal(7, item.getVlPrecoKg());
            ps.setString(8, item.getDsStatus());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (!keys.next()) throw new SQLException("Chave nao retornada");
                item.setCdLote(keys.getLong(1));
            }
            return item;
        } catch (SQLException ex) { throw new PersistenciaException(ex); }
    }

    public boolean atualizar(Long id, Lote item) {
        String sql = "UPDATE tb_lote SET cd_agricultor = ?, cd_cultura = ?, qt_peso_inicial_kg = ?, qt_peso_disponivel_kg = ?, dt_colheita = ?, ds_armazenamento = ?, vl_preco_kg = ?, ds_status = ? WHERE cd_lote = ?";
        try (Connection c = dataSource.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, item.getCdAgricultor());
            ps.setLong(2, item.getCdCultura());
            ps.setBigDecimal(3, item.getQtPesoInicialKg());
            ps.setBigDecimal(4, item.getQtPesoDisponivelKg());
            ps.setDate(5, java.sql.Date.valueOf(item.getDtColheita()));
            ps.setString(6, item.getDsArmazenamento());
            ps.setBigDecimal(7, item.getVlPrecoKg());
            ps.setString(8, item.getDsStatus());
            ps.setLong(9, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { throw new PersistenciaException(ex); }
    }

    public boolean remover(Long id) {
        try (Connection c = dataSource.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM tb_lote WHERE cd_lote = ?")) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) { throw new PersistenciaException(ex); }
    }

    public List<Lote> listarPorVinculo(String vinculo, Long id) {
        // A coluna vem de uma lista fechada; nunca e recebida como SQL do cliente.
        String coluna = switch (vinculo) {
            case "agricultor" -> "cd_agricultor";
            case "cultura" -> "cd_cultura";
            default -> throw new IllegalArgumentException("Vinculo invalido");
        };
        String sql = "SELECT cd_lote, cd_agricultor, cd_cultura, qt_peso_inicial_kg, qt_peso_disponivel_kg, dt_colheita, ds_armazenamento, vl_preco_kg, ds_status FROM tb_lote WHERE " + coluna + " = ? ORDER BY cd_lote";
        try (Connection c = dataSource.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                List<Lote> lista = new ArrayList<>();
                while (rs.next()) lista.add(mapear(rs));
                return lista;
            }
        } catch (SQLException ex) { throw new PersistenciaException(ex); }
    }

}
