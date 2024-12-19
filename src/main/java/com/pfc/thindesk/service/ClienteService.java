package com.pfc.thindesk.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pfc.thindesk.entity.Cliente;
import com.pfc.thindesk.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    
    // Método para salvar um novo técnico
    public void salvarCliente(Cliente cliente) {
        clienteRepository.save(cliente); // Salva o técnico na collection
    }


    // Método para criar um novo cliente
    public Cliente criarCliente(Cliente cliente) {
        if (cliente.getNome() == null || cliente.getNome().isEmpty()) {
            throw new RuntimeException("O nome do cliente é obrigatório.");
        }
        if (cliente.getContato() == null || cliente.getContato().isEmpty()) {
            throw new RuntimeException("O contato do cliente é obrigatório.");
        }
        return clienteRepository.save(cliente);
    }

// Método para atualizar um cliente
    public Cliente atualizarCliente(String id, Cliente clienteAtualizado) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            cliente.setNome(clienteAtualizado.getNome());
            cliente.setEmpresa(clienteAtualizado.getEmpresa());
            cliente.setContato(clienteAtualizado.getContato());
            cliente.setIsActive(true);
            
            return clienteRepository.save(cliente);
        } else {
            throw new RuntimeException("Cliente com ID " + id + " não encontrado.");
        }
    }

    // Método para buscar um cliente por ID
    public Optional<Cliente> buscarClientePorId(String id) {
        return clienteRepository.findById(id);
    }

    // Método para listar todos os clientes
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    // Método para deletar um cliente
    public void deletarCliente(String id) {
        Optional<Cliente> clienteExistente = clienteRepository.findById(id);
        if (clienteExistente.isPresent()) {
            Cliente cliente = clienteExistente.get();
            cliente.setIsActive(false); // Define isActive como false
            clienteRepository.save(cliente);
        } else {
            throw new RuntimeException("Cliente com ID " + id + " não encontrado.");
        }
    }


    // Método para pesquisar clientes por nome e empresa
    public List<Cliente> pesquisarClientes(String nome, String empresa) {
        if ((nome == null || nome.isEmpty()) && (empresa == null || empresa.isEmpty())) {
            return clienteRepository.findAll();
        }

        return clienteRepository.findAll().stream()
                .filter(cliente -> (nome == null || nome.isEmpty() || cliente.getNome().toLowerCase().contains(nome.toLowerCase())))
                .filter(cliente -> (empresa == null || empresa.isEmpty() || cliente.getEmpresa().toLowerCase().contains(empresa.toLowerCase())))
                .collect(Collectors.toList());
    }

    // Método para listar empresas únicas
    public List<String> listarEmpresasUnicas() {
        return clienteRepository.findAll().stream()
                .map(Cliente::getEmpresa)
                .distinct()
                .collect(Collectors.toList());
    }
}
