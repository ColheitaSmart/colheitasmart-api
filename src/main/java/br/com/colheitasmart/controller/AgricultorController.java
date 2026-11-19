package br.com.colheitasmart.controller;

import br.com.colheitasmart.bo.AgricultorBO;
import br.com.colheitasmart.dto.*;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/agricultores")
public class AgricultorController {
    private final AgricultorBO bo;
    public AgricultorController(AgricultorBO bo) { this.bo = bo; }

    @GetMapping
    public ResponseEntity<List<AgricultorSaidaDTO>> listar() { return ResponseEntity.ok(bo.listar()); }

    @GetMapping("/{id}")
    public ResponseEntity<AgricultorSaidaDTO> buscar(@PathVariable Long id) { return ResponseEntity.ok(bo.buscar(id)); }

    @PostMapping
    public ResponseEntity<AgricultorSaidaDTO> cadastrar(@Valid @RequestBody AgricultorEntradaDTO entrada) {
        AgricultorSaidaDTO saida = bo.cadastrar(entrada);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/agricultores/{id}")
            .buildAndExpand(saida.getCdAgricultor()).toUri();
        return ResponseEntity.created(location).body(saida);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgricultorSaidaDTO> atualizar(@PathVariable Long id, @Valid @RequestBody AgricultorEntradaDTO entrada) {
        return ResponseEntity.ok(bo.atualizar(id, entrada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        bo.remover(id);
        return ResponseEntity.noContent().build();
    }
}
