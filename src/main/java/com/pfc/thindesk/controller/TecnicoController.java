package com.pfc.thindesk.controller;

import com.pfc.thindesk.entity.Tecnico;
import com.pfc.thindesk.service.TecnicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tecnicos")
public class TecnicoController {

     @Autowired
    private TecnicoService tecnicoService;

    @GetMapping
    public List<Tecnico> listarTecnicos() {
        return tecnicoService.listarTecnicos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tecnico> buscarTecnicoPorId(@PathVariable String id) {
        Tecnico tecnico = tecnicoService.buscarTecnicoPorId(id)
                .orElseThrow(() -> new RuntimeException("Tecnico não encontrado com ID: " + id));
        return ResponseEntity.ok(tecnico);
    }

    @PostMapping
    public ResponseEntity<Tecnico> criarTecnico(@RequestBody Tecnico tecnico) {
        Tecnico novoTecnico = tecnicoService.criarTecnico(tecnico);
        return ResponseEntity.ok(novoTecnico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tecnico> atualizarTecnico(@PathVariable String id, @RequestBody Tecnico tecnicoAtualizado) {
        Tecnico tecnico = tecnicoService.atualizarTecnico(id, tecnicoAtualizado);
        return ResponseEntity.ok(tecnico);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTecnico(@PathVariable String id) {
        tecnicoService.deletarTecnico(id);
        return ResponseEntity.noContent().build();
    }
}
