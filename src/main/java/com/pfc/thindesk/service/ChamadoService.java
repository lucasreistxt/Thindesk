package com.pfc.thindesk.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.pfc.thindesk.entity.Chamado;
import com.pfc.thindesk.repository.ChamadoRepository;

@Service
public class ChamadoService {

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Autowired
    private TecnicoService tecnicoService;

    // Método para salvar um novo técnico
    public void salvarChamado(Chamado chamado) {
        chamado.setStatus("Em Andamento");
        chamadoRepository.save(chamado); // Salva o técnico na collection
    }

    // Método para criar um novo chamado
    public Chamado criarChamado(Chamado chamado) {
        chamado.setDataCriacao(LocalDateTime.now());
        chamado.setStatus("Em Andamento"); // Definir status padrão ao criar
        return chamadoRepository.save(chamado);
    }

    // Método para atualizar um chamado
    public Chamado atualizarChamado(String id, Chamado chamadoAtualizado) {
        Chamado chamadoExistente = chamadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado com ID: " + id));

        chamadoExistente.setUsuarioSolicitanteId(chamadoAtualizado.getUsuarioSolicitanteId());
        chamadoExistente.setTitulo(chamadoAtualizado.getTitulo());
        chamadoExistente.setDescricao(chamadoAtualizado.getDescricao());
        chamadoExistente.setStatus(chamadoAtualizado.getStatus());
        chamadoExistente.setPrioridade(chamadoAtualizado.getPrioridade());
        chamadoExistente.setNivelChamado(chamadoAtualizado.getNivelChamado());
        chamadoExistente.setTecnicoResponsavelId(chamadoAtualizado.getTecnicoResponsavelId());

        return chamadoRepository.save(chamadoExistente);
    }

    // Método para encerrar um chamado
    public void encerrarChamado(String id) {
        // Busca o chamado pelo ID
        Chamado chamado = chamadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chamado não encontrado com ID: " + id));

        // Atualiza o status para "Encerrado"
        chamado.setStatus("Encerrado");
        chamadoRepository.save(chamado); // Salva a alteração no banco
    }

    // Método para buscar um chamado por ID
    public Optional<Chamado> buscarChamadoPorId(String id) {
        return chamadoRepository.findById(id);
    }

    // Método para listar todos os chamados
    public List<Chamado> listarChamados() {
        return chamadoRepository.findAll();
    }

    // Método para listar chamados por status (EM_ANDAMENTO, ENCERRADO)
    public List<Chamado> listarChamadosPorStatus(String status) {
        return chamadoRepository.findByStatus(status);
    }

    // Método para listar chamados por usuário solicitante
    public List<Chamado> listarChamadosPorUsuario(String usuarioSolicitanteId) {
        return chamadoRepository.findByUsuarioSolicitanteId(usuarioSolicitanteId);
    }

    // Método para listar chamados atribuídos a um técnico
    public List<Chamado> listarChamadosPorTecnico(String tecnicoResponsavelId) {
        return chamadoRepository.findByTecnicoResponsavelId(tecnicoResponsavelId);
    }

    // Método para deletar um chamado
    public void deletarChamado(String id) {
        if (chamadoRepository.existsById(id)) {
            chamadoRepository.deleteById(id);
        } else {
            throw new RuntimeException("Chamado com ID " + id + " não encontrado.");
        }
    }
    public List<Chamado> listarTodosChamados() {
        return chamadoRepository.findAll();
    }
    public List<Chamado> listarChamadosOrdenados() {
        return chamadoRepository.findAll(Sort.by(Sort.Order.desc("status"), Sort.Order.desc("tecnicoResponsavelId")));
    }

    public List<Chamado> listarChamadosPorDescricao(String descricao) {
        return chamadoRepository.findByDescricaoContaining(descricao);
    }

    public List<Chamado> listarChamadosPorDescricaoEStatus(String descricao, String status) {
        return chamadoRepository.findByDescricaoContainingAndStatus(descricao, status);
    }
    public List<Chamado> pesquisarChamados(String descricao, String status, String prioridade, String tecnicoResponsavelId) {
        if ((descricao == null || descricao.isEmpty()) && (status == null || status.isEmpty()) && 
            (prioridade == null || prioridade.isEmpty()) && (tecnicoResponsavelId == null || tecnicoResponsavelId.isEmpty())) {
            return chamadoRepository.findAll();
        }

        return chamadoRepository.findAll().stream()
                .filter(chamado -> (descricao == null || descricao.isEmpty() || chamado.getDescricao().toLowerCase().contains(descricao.toLowerCase())))
                .filter(chamado -> (status == null || status.isEmpty() || chamado.getStatus().toLowerCase().contains(status.toLowerCase())))
                .filter(chamado -> (prioridade == null || prioridade.isEmpty() || chamado.getPrioridade().toLowerCase().contains(prioridade.toLowerCase())))
                .filter(chamado -> (tecnicoResponsavelId == null || tecnicoResponsavelId.isEmpty() || chamado.getTecnicoResponsavelId().equals(tecnicoResponsavelId)))
                .collect(Collectors.toList());
    }

    public List<Chamado> pesquisarChamadosPorTecnicoNome(String descricao, String status, String prioridade, String tecnicoResponsavelId) {
        if ((descricao == null || descricao.isEmpty()) && (status == null || status.isEmpty()) && 
            (prioridade == null || prioridade.isEmpty()) && (tecnicoResponsavelId == null || tecnicoResponsavelId.isEmpty())) {
            return chamadoRepository.findAll();
        }
       
        List<Chamado> todosChamados = chamadoRepository.findAll();

  


        return chamadoRepository.findAll().stream()
                .filter(chamado -> tecnicoService.buscarNomeTecnicoPorId(chamado.getTecnicoResponsavelId()).equals(tecnicoResponsavelId))
                .collect(Collectors.toList());
    }
}


