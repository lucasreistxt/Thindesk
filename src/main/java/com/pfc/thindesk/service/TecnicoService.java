package com.pfc.thindesk.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pfc.thindesk.entity.Tecnico;
import com.pfc.thindesk.repository.TecnicoRepository;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepository tecnicoRepository;

    // Método para salvar um novo técnico
    public void salvarTecnico(Tecnico tecnico) {
        tecnicoRepository.save(tecnico); // Salva o técnico na collection
    }

    // Método para criar um novo técnico
    public Tecnico criarTecnico(Tecnico tecnico) {
        if (tecnico.getNome() == null || tecnico.getNome().isEmpty()) {
            throw new RuntimeException("O nome do técnico é obrigatório.");
        }
        if (tecnico.getEspecializacao() == null || tecnico.getEspecializacao().isEmpty()) {
            throw new RuntimeException("A especialização do técnico é obrigatória.");
        }
        tecnico.setStatus("DISPONIVEL"); // Define o status padrão como DISPONIVEL
        return tecnicoRepository.save(tecnico);
    }

    // Método para atualizar um técnico
    public Tecnico atualizarTecnico(String id, Tecnico tecnicoAtualizado) {
        Optional<Tecnico> tecnicoExistente = tecnicoRepository.findById(id);
        if (tecnicoExistente.isPresent()) {
            Tecnico tecnico = tecnicoExistente.get();
            tecnico.setNome(tecnicoAtualizado.getNome());
            tecnico.setEspecializacao(tecnicoAtualizado.getEspecializacao());
            tecnico.setStatus(tecnicoAtualizado.getStatus());
            return tecnicoRepository.save(tecnico);
        } else {
            throw new RuntimeException("Técnico com ID " + id + " não encontrado.");
        }
    }

    // Método para buscar um técnico por ID
    public Optional<Tecnico> buscarTecnicoPorId(String id) {
        return tecnicoRepository.findById(id);
    }

    // Método para listar todos os técnicos
    public List<Tecnico> listarTecnicos() {
        return tecnicoRepository.findAll();
    }

    // Método para deletar um técnico
    public void deletarTecnico(String id) {
        if (tecnicoRepository.existsById(id)) {
            tecnicoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Técnico com ID " + id + " não encontrado.");
        }
    }

    public List<Tecnico> pesquisarTecnicos(String nome, String especializacao, String status) {
        if ((nome == null || nome.isEmpty()) && (especializacao == null || especializacao.isEmpty()) && (status == null || status.isEmpty())) {
            return tecnicoRepository.findAll();
        }

        return tecnicoRepository.findAll().stream()
                .filter(tecnico -> (nome == null || nome.isEmpty() || tecnico.getNome().contains(nome)))
                .filter(tecnico -> (especializacao == null || especializacao.isEmpty() || tecnico.getEspecializacao().contains(especializacao)))
                .filter(tecnico -> (status == null || status.isEmpty() || tecnico.getStatus().equals(status)))
                .collect(Collectors.toList());
    }

    public List<String> listarEspecializacoesUnicas() {
        return tecnicoRepository.findAll().stream()
                .map(Tecnico::getEspecializacao)
                .distinct()
                .collect(Collectors.toList());
    }

        // Método para buscar o nome do técnico pelo ID
        public String buscarNomeTecnicoPorId(String id) {
            return tecnicoRepository.findById(id)
                    .map(Tecnico::getNome)
                    .orElseThrow(() -> new RuntimeException("Técnico não encontrado com ID: " + id));
        }

}