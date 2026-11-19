package br.com.colheitasmart.controller;

import br.com.colheitasmart.bo.LoteBO;
import br.com.colheitasmart.dto.*;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/lotes")
public class LoteController {
    private final LoteBO bo;
    public LoteController(LoteBO bo) { this.bo = bo; }

    @GetMapping
    public ResponseEntity<List<LoteSaidaDTO>> listar() { return ResponseEntity.ok(bo.listar()); }

    @GetMapping("/{id}")
    public ResponseEntity<LoteSaidaDTO> buscar(@PathVariable Long id) { return ResponseEntity.ok(bo.buscar(id)); }

    @PostMapping
    public ResponseEntity<LoteSaidaDTO> cadastrar(@Valid @RequestBody LoteEntradaDTO entrada) {
        LoteSaidaDTO saida = bo.cadastrar(entrada);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/lotes/{id}")
            .buildAndExpand(saida.getCdLote()).toUri();
        return ResponseEntity.created(location).body(saida);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoteSaidaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody LoteEntradaDTO entrada) {
        return ResponseEntity.ok(bo.atualizar(id, entrada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        bo.remover(id);
        return ResponseEntity.noContent().build();
    }
}
