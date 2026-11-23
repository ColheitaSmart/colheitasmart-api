
package br.com.colheitasmart.controller;

import br.com.colheitasmart.bo.LoteBO;
import br.com.colheitasmart.dto.LoteEntradaDTO;
import br.com.colheitasmart.dto.LoteSaidaDTO;
import br.com.colheitasmart.model.Lote;

import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/lotes")
public class LoteController {

    private final LoteBO bo;

    public LoteController(LoteBO bo) {
        this.bo = bo;
    }

    private Long agricultorLogado(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED, "Nao autenticado."
            );
        }

        try {
            return Long.valueOf(auth.getName());
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "Identidade invalida."
            );
        }
    }

    private void verificarDono(Long idLote, Long idAgricultor) {
        Lote lote = bo.buscarEntidade(idLote);

        if (!idAgricultor.equals(lote.getCdAgricultor())) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Voce nao pode acessar este lote."
            );
        }
    }

    @GetMapping
    public ResponseEntity<List<LoteSaidaDTO>> listar(
            Authentication auth) {

        Long id = agricultorLogado(auth);

        return ResponseEntity.ok(
            bo.listarPorVinculo("agricultor", id)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoteSaidaDTO> buscar(
            @PathVariable Long id,
            Authentication auth) {

        verificarDono(id, agricultorLogado(auth));
        return ResponseEntity.ok(bo.buscar(id));
    }

    @PostMapping
    public ResponseEntity<LoteSaidaDTO> cadastrar(
            @Valid @RequestBody LoteEntradaDTO entrada,
            Authentication auth) {

        Long id = agricultorLogado(auth);

        if (!id.equals(entrada.getCdAgricultor())) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Agricultor diferente do usuario logado."
            );
        }

        LoteSaidaDTO saida = bo.cadastrar(entrada);

        URI location = ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path("/lotes/{id}")
            .buildAndExpand(saida.getCdLote())
            .toUri();

        return ResponseEntity.created(location).body(saida);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoteSaidaDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody LoteEntradaDTO entrada,
            Authentication auth) {

        Long agricultorId = agricultorLogado(auth);
        verificarDono(id, agricultorId);

        if (!agricultorId.equals(entrada.getCdAgricultor())) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Agricultor diferente do usuario logado."
            );
        }

        return ResponseEntity.ok(bo.atualizar(id, entrada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(
            @PathVariable Long id,
            Authentication auth) {

        verificarDono(id, agricultorLogado(auth));
        bo.remover(id);

        return ResponseEntity.noContent().build();
    }
}
