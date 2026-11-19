package br.com.colheitasmart.bo;

import br.com.colheitasmart.dao.*;
import br.com.colheitasmart.dto.*;
import br.com.colheitasmart.model.Cultura;
import br.com.colheitasmart.exception.*;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class CulturaBO {
    private final CulturaDAO dao;
    private final ModelMapper mapper;

    public CulturaBO(CulturaDAO dao, ModelMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;

    }
    public List<CulturaSaidaDTO> listar() {
        return dao.listar().stream().map(this::saida).toList();
    }
    public CulturaSaidaDTO buscar(Long id) { return saida(exigir(id)); }
    public CulturaSaidaDTO cadastrar(CulturaEntradaDTO entrada) {

        return saida(dao.cadastrar(mapper.map(entrada, Cultura.class)));
    }
    public CulturaSaidaDTO atualizar(Long id, CulturaEntradaDTO entrada) {
        exigir(id);

        Cultura item = mapper.map(entrada, Cultura.class);
        item.setCdCultura(id);
        if (!dao.atualizar(id, item)) throw new RecursoNaoEncontradoException("Cultura nao encontrado.");
        return saida(item);
    }
    public void remover(Long id) {
        if (!dao.remover(id)) throw new RecursoNaoEncontradoException("Cultura nao encontrado.");
    }
    private Cultura exigir(Long id) {
        return dao.buscarPorId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Cultura nao encontrado."));
    }
    private CulturaSaidaDTO saida(Cultura item) { return mapper.map(item, CulturaSaidaDTO.class); }

}
