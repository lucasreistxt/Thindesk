package com.pfc.thindesk.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pfc.thindesk.entity.Categoria;

@Repository
public interface CategoriaRepository extends MongoRepository<Categoria, String> {

    // Método para buscar categorias por nome
    List<Categoria> findByDescricaoContaining(String descricao);
    List<Categoria> findByTipoContaining(String tipo);
}
