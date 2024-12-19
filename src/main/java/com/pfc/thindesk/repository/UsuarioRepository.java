package com.pfc.thindesk.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.pfc.thindesk.entity.Usuario;

@Repository
public interface UsuarioRepository extends MongoRepository<Usuario, String> {
    List<Usuario> findByTipoUsuario(String tipoUsuario);
    Optional<Usuario> findByEmailAndSenha(String email, String senha);
    Optional<Usuario> findByEmail(String email);   
    
}
