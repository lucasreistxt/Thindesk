package com.pfc.thindesk.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pfc.thindesk.entity.Categoria;
import com.pfc.thindesk.repository.CategoriaRepository;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    // Método para salvar um nova categoria
    public void salvarCategoria(Categoria categoria) {
        categoriaRepository.save(categoria); // Salva a categoria na collection
    }
    
    // Método para criar uma nova categoria
    public Categoria criarCategoria(Categoria categoria) {
        // Adiciona validações, se necessário
        if (categoria.getNome() == null || categoria.getNome().isEmpty()) {
            throw new RuntimeException("O nome da categoria é obrigatório.");
        }
        return categoriaRepository.save(categoria);
    }

    // Método para atualizar uma categoria
    public Categoria atualizarCategoria(String id, Categoria categoriaAtualizada) {
        Optional<Categoria> categoriaExistente = categoriaRepository.findById(id);
        if (categoriaExistente.isPresent()) {
            Categoria categoria = categoriaExistente.get();
            categoria.setNome(categoriaAtualizada.getNome());
            categoria.setDescricao(categoriaAtualizada.getDescricao());
            return categoriaRepository.save(categoria);
        } else {
            throw new RuntimeException("Categoria com ID " + id + " não encontrada.");
        }
    }

    // Método para buscar uma categoria por ID
    public Optional<Categoria> buscarCategoriaPorId(String id) {
        return categoriaRepository.findById(id);
    }

    // Método para listar todas as categorias
    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    // Método para deletar uma categoria
    public void deletarCategoria(String id) {
        if (categoriaRepository.existsById(id)) {
            categoriaRepository.deleteById(id);
        } else {
            throw new RuntimeException("Categoria com ID " + id + " não encontrada.");
        }
    }

           // Método para pesquisar categorias por nome e descrição
           public List<Categoria> pesquisarCategorias(String nome, String tipo, String descricao) {
            if ((nome == null || nome.isEmpty()) && (tipo == null || tipo.isEmpty()) && (descricao == null || descricao.isEmpty())) {
                return categoriaRepository.findAll();
            }
    
            return categoriaRepository.findAll().stream()
                    .filter(categoria -> (nome == null || nome.isEmpty() || categoria.getNome().toLowerCase().contains(nome.toLowerCase())))
                    .filter(categoria -> (descricao == null || descricao.isEmpty() || categoria.getDescricao().toLowerCase().contains(descricao.toLowerCase())))
                    .collect(Collectors.toList());
        }

    // Método para listar tipos únicos de categorias
    public List<String> listarTiposUnicos() {
        return categoriaRepository.findAll().stream()
                .map(Categoria::getTipo)
                .distinct()
                .collect(Collectors.toList());
    }
}
