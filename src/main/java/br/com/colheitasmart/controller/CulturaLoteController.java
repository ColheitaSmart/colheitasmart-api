package br.com.colheitasmart.controller;

import br.com.colheitasmart.bo.LoteBO;
import br.com.colheitasmart.dto.*;
import jakarta.validation.Valid;
import java.util.List;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/culturas/{id}/lotes")
public class CulturaLoteController {
    private final LoteBO bo;
    public CulturaLoteController(LoteBO bo) { this.bo = bo; }

    @GetMapping
    public ResponseEntity<List<LoteSaidaDTO>> listar(@PathVariable Long id) {
        return ResponseEntity.ok(bo.listarPorVinculo("cultura", id));
    }

    @PostMapping
    public ResponseEntity<LoteSaidaDTO> cadastrar(@PathVariable Long id, @Valid @RequestBody LoteEntradaDTO entrada) {
        LoteSaidaDTO saida = bo.cadastrarNoVinculo("cultura", id, entrada);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/lotes/{id}")
            .buildAndExpand(saida.getCdLote()).toUri();
        return ResponseEntity.created(location).body(saida);
    }
}
