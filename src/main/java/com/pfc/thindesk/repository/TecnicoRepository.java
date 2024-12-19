package com.pfc.thindesk.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pfc.thindesk.entity.Tecnico;

@Repository
public interface TecnicoRepository extends MongoRepository<Tecnico, String> {

    List<Tecnico> findByNomeContaining(String nome);
    List<Tecnico> findByEspecializacaoContaining(String especializacao);
    List<Tecnico> findByStatus(String status);
}
