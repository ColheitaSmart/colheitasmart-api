
package br.com.colheitasmart.controller;

import br.com.colheitasmart.bo.LoteBO;
import br.com.colheitasmart.dto.LoteEntradaDTO;
import br.com.colheitasmart.dto.LoteSaidaDTO;

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
@RequestMapping("/agricultores/{id}/lotes")
public class AgricultorLoteController {

    private final LoteBO bo;

    public AgricultorLoteController(LoteBO bo) {
        this.bo = bo;
    }

    private void verificarAgricultor(
            Long id,
            Authentication autenticacao) {

        if (autenticacao == null ||
            !autenticacao.isAuthenticated()) {

            throw new ResponseStatusException(
                HttpStatus.UNAUTHORIZED,
                "Usuario nao autenticado."
            );
        }

        Long idAutenticado;

        try {
            idAutenticado =
                Long.valueOf(autenticacao.getName());
        } catch (NumberFormatException ex) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Identidade invalida."
            );
        }

        if (!id.equals(idAutenticado)) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN,
                "Voce nao pode acessar lotes de outro agricultor."
            );
        }
    }

    @GetMapping
    public ResponseEntity<List<LoteSaidaDTO>> listar(
            @PathVariable Long id,
            Authentication autenticacao) {

        verificarAgricultor(id, autenticacao);

        return ResponseEntity.ok(
            bo.listarPorVinculo("agricultor", id)
        );
    }

    @PostMapping
    public ResponseEntity<LoteSaidaDTO> cadastrar(
            @PathVariable Long id,
            @Valid @RequestBody LoteEntradaDTO entrada,
            Authentication autenticacao) {

        verificarAgricultor(id, autenticacao);

        LoteSaidaDTO saida =
            bo.cadastrarNoVinculo("agricultor", id, entrada);

        URI location =
            ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/lotes/{id}")
                .buildAndExpand(saida.getCdLote())
                .toUri();

        return ResponseEntity
            .created(location)
            .body(saida);
    }
}
