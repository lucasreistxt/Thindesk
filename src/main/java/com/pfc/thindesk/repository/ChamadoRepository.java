package com.pfc.thindesk.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pfc.thindesk.entity.Chamado;

@Repository
public interface ChamadoRepository extends MongoRepository<Chamado, String> {
    List<Chamado> findByDescricaoContaining(String descricao);
    List<Chamado> findByStatus(String status);
    List<Chamado> findByDescricaoContainingAndStatus(String descricao, String status);

    public List<Chamado> findByUsuarioSolicitanteId(String usuarioSolicitanteId);

    public List<Chamado> findByTecnicoResponsavelId(String tecnicoResponsavelId);
}