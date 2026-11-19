package br.com.colheitasmart.controller;

import br.com.colheitasmart.bo.CulturaBO;
import br.com.colheitasmart.dto.*;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/culturas")
public class CulturaController {
    private final CulturaBO bo;
    public CulturaController(CulturaBO bo) { this.bo = bo; }

    @GetMapping
    public ResponseEntity<List<CulturaSaidaDTO>> listar() { return ResponseEntity.ok(bo.listar()); }

    @GetMapping("/{id}")
    public ResponseEntity<CulturaSaidaDTO> buscar(@PathVariable Long id) { return ResponseEntity.ok(bo.buscar(id)); }

    @PostMapping
    public ResponseEntity<CulturaSaidaDTO> cadastrar(@Valid @RequestBody CulturaEntradaDTO entrada) {
        CulturaSaidaDTO saida = bo.cadastrar(entrada);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/culturas/{id}")
            .buildAndExpand(saida.getCdCultura()).toUri();
        return ResponseEntity.created(location).body(saida);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CulturaSaidaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody CulturaEntradaDTO entrada) {
        return ResponseEntity.ok(bo.atualizar(id, entrada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        bo.remover(id);
        return ResponseEntity.noContent().build();
    }
}
