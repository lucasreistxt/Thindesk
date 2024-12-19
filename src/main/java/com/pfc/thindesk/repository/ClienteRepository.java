package com.pfc.thindesk.repository;

import com.pfc.thindesk.entity.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClienteRepository extends MongoRepository<Cliente, String> {

    // Método para buscar clientes por empresa
    List<Cliente> findByEmpresa(String empresa);
}
