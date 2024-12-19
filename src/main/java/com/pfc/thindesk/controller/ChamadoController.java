package com.pfc.thindesk.controller;

import com.pfc.thindesk.entity.Chamado;
import com.pfc.thindesk.service.ChamadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chamados")
public class ChamadoController {

    @Autowired
    private ChamadoService chamadoService;

    @GetMapping
    public List<Chamado> listarChamados() {
        return chamadoService.listarChamados();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chamado> buscarChamadoPorId(@PathVariable String id) {
        Chamado chamado = chamadoService.buscarChamadoPorId(id)
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado com ID: " + id));
        return ResponseEntity.ok(chamado);
    }

    @PostMapping
    public ResponseEntity<Chamado> criarChamado(@RequestBody Chamado chamado) {
        Chamado novoChamado = chamadoService.criarChamado(chamado);
        return ResponseEntity.ok(novoChamado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chamado> atualizarChamado(@PathVariable String id, @RequestBody Chamado chamadoAtualizado) {
        Chamado chamado = chamadoService.atualizarChamado(id, chamadoAtualizado);
        return ResponseEntity.ok(chamado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarChamado(@PathVariable String id) {
        chamadoService.deletarChamado(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/encerrar/{id}")
    public ResponseEntity<Void> encerrarChamado(@PathVariable String id) {
        chamadoService.encerrarChamado(id);
        return ResponseEntity.noContent().build();
    }
}
