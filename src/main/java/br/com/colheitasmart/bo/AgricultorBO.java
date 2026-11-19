package br.com.colheitasmart.bo;

import br.com.colheitasmart.dao.*;
import br.com.colheitasmart.dto.*;
import br.com.colheitasmart.model.Agricultor;
import br.com.colheitasmart.exception.*;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class AgricultorBO {
    private final AgricultorDAO dao;
    private final ModelMapper mapper;

    public AgricultorBO(AgricultorDAO dao, ModelMapper mapper) {
        this.dao = dao;
        this.mapper = mapper;

    }
    public List<AgricultorSaidaDTO> listar() {
        return dao.listar().stream().map(this::saida).toList();
    }
    public AgricultorSaidaDTO buscar(Long id) { return saida(exigir(id)); }
    public AgricultorSaidaDTO cadastrar(AgricultorEntradaDTO entrada) {

        return saida(dao.cadastrar(mapper.map(entrada, Agricultor.class)));
    }
    public AgricultorSaidaDTO atualizar(Long id, AgricultorEntradaDTO entrada) {
        exigir(id);

        Agricultor item = mapper.map(entrada, Agricultor.class);
        item.setCdAgricultor(id);
        if (!dao.atualizar(id, item)) throw new RecursoNaoEncontradoException("Agricultor nao encontrado.");
        return saida(item);
    }
    public void remover(Long id) {
        if (!dao.remover(id)) throw new RecursoNaoEncontradoException("Agricultor nao encontrado.");
    }
    private Agricultor exigir(Long id) {
        return dao.buscarPorId(id).orElseThrow(() -> new RecursoNaoEncontradoException("Agricultor nao encontrado."));
    }
    private AgricultorSaidaDTO saida(Agricultor item) { return mapper.map(item, AgricultorSaidaDTO.class); }

}
