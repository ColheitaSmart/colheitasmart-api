
package br.com.colheitasmart.bo;

import br.com.colheitasmart.dao.*;
import br.com.colheitasmart.dto.*;
import br.com.colheitasmart.model.Lote;
import br.com.colheitasmart.exception.*;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class LoteBO {

    private final LoteDAO dao;
    private final ModelMapper mapper;
    private final AgricultorDAO agricultorDAO;
    private final CulturaDAO culturaDAO;

    public LoteBO(
            LoteDAO dao,
            ModelMapper mapper,
            AgricultorDAO agricultorDAO,
            CulturaDAO culturaDAO) {

        this.dao = dao;
        this.mapper = mapper;
        this.agricultorDAO = agricultorDAO;
        this.culturaDAO = culturaDAO;
    }

    public List<LoteSaidaDTO> listar() {
        return dao.listar()
                .stream()
                .map(this::saida)
                .toList();
    }

    public Lote buscarEntidade(Long id) {
        return exigir(id);
    }

    public LoteSaidaDTO buscar(Long id) {
        return saida(exigir(id));
    }

    public LoteSaidaDTO cadastrar(LoteEntradaDTO entrada) {
        validar(entrada);

        Lote item = mapper.map(entrada, Lote.class);

        return saida(dao.cadastrar(item));
    }

    public LoteSaidaDTO atualizar(
            Long id,
            LoteEntradaDTO entrada) {

        exigir(id);
        validar(entrada);

        Lote item = mapper.map(entrada, Lote.class);
        item.setCdLote(id);

        if (!dao.atualizar(id, item)) {
            throw new RecursoNaoEncontradoException(
                "Lote nao encontrado."
            );
        }

        return saida(item);
    }

    public void remover(Long id) {
        if (!dao.remover(id)) {
            throw new RecursoNaoEncontradoException(
                "Lote nao encontrado."
            );
        }
    }

    private Lote exigir(Long id) {
        return dao.buscarPorId(id)
                .orElseThrow(() ->
                    new RecursoNaoEncontradoException(
                        "Lote nao encontrado."
                    )
                );
    }

    private LoteSaidaDTO saida(Lote item) {
        return mapper.map(item, LoteSaidaDTO.class);
    }

    private void validar(LoteEntradaDTO entrada) {

        if (entrada.getQtPesoDisponivelKg()
                .compareTo(entrada.getQtPesoInicialKg()) > 0) {

            throw new RegraNegocioException(
                "Peso disponivel nao pode superar o peso inicial."
            );
        }

        if (agricultorDAO
                .buscarPorId(entrada.getCdAgricultor())
                .isEmpty()) {

            throw new RecursoNaoEncontradoException(
                "Agricultor nao encontrado."
            );
        }

        if (culturaDAO
                .buscarPorId(entrada.getCdCultura())
                .isEmpty()) {

            throw new RecursoNaoEncontradoException(
                "Cultura nao encontrada."
            );
        }
    }

    public List<LoteSaidaDTO> listarPorVinculo(
            String vinculo,
            Long id) {

        exigirVinculo(vinculo, id);

        return dao.listarPorVinculo(vinculo, id)
                .stream()
                .map(this::saida)
                .toList();
    }

    public LoteSaidaDTO cadastrarNoVinculo(
            String vinculo,
            Long id,
            LoteEntradaDTO entrada) {

        exigirVinculo(vinculo, id);

        Long recebido = vinculo.equals("agricultor")
                ? entrada.getCdAgricultor()
                : entrada.getCdCultura();

        if (!id.equals(recebido)) {
            throw new RegraNegocioException(
                "O vinculo do JSON deve corresponder ao ID da rota."
            );
        }

        return cadastrar(entrada);
    }

    private void exigirVinculo(
            String vinculo,
            Long id) {

        boolean existe = vinculo.equals("agricultor")
                ? agricultorDAO.buscarPorId(id).isPresent()
                : culturaDAO.buscarPorId(id).isPresent();

        if (!existe) {
            throw new RecursoNaoEncontradoException(
                "Vinculo nao encontrado."
            );
        }
    }
}
