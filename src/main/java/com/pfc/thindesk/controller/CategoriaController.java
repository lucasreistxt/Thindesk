package com.pfc.thindesk.controller;

import com.pfc.thindesk.entity.Categoria;
import com.pfc.thindesk.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<Categoria> listarCategorias() {
        return categoriaService.listarCategorias();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarCategoriaPorId(@PathVariable String id) {
        Categoria categoria = categoriaService.buscarCategoriaPorId(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrado com ID: " + id));
        return ResponseEntity.ok(categoria);
    }

    @PostMapping
    public ResponseEntity<Categoria> criarCategoria(@RequestBody Categoria categoria) {
        Categoria novoCategoria = categoriaService.criarCategoria(categoria);
        return ResponseEntity.ok(novoCategoria);
    }

    @GetMapping("/api/categorias/{id}")
    public ResponseEntity<Categoria> atualizarCategoria(@PathVariable String id,
            @RequestBody Categoria categoriaAtualizado) {
        Categoria categoria = categoriaService.atualizarCategoria(id, categoriaAtualizado);
        return ResponseEntity.ok(categoria);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCategoria(@PathVariable String id) {
        categoriaService.deletarCategoria(id);
        return ResponseEntity.noContent().build();
    }
}
