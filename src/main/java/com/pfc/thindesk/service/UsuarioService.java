package com.pfc.thindesk.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pfc.thindesk.entity.Usuario;
import com.pfc.thindesk.repository.UsuarioRepository;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    // Método para salvar um novo técnico
    public void salvarUsuario(Usuario usuario) {
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuarioRepository.save(usuario); // Salva o técnico na collection
    }


    // Método para criar um novo usuário
    public Usuario criarUsuario(Usuario usuario) {
        if (usuario.getNome() == null || usuario.getNome().isEmpty()) {
            throw new RuntimeException("O nome do usuário é obrigatório.");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            throw new RuntimeException("O e-mail do usuário é obrigatório.");
        }
        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            throw new RuntimeException("A senha do usuário é obrigatória.");
        }
        if (usuario.getTipoUsuario() == null || usuario.getTipoUsuario().isEmpty()) {
            throw new RuntimeException("O tipo do usuário é obrigatório.");
        }

        // Criptografar a senha antes de salvar
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        return usuarioRepository.save(usuario);
    }

    // Método para atualizar um usuário
    public Usuario atualizarUsuario(String id, Usuario usuarioAtualizado, String senhaExistente) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNome(usuarioAtualizado.getNome());
            usuario.setEmail(usuarioAtualizado.getEmail());
            if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().isEmpty()) {
                // Criptografar a nova senha antes de salvar
                usuario.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
            } else {
                usuario.setSenha(senhaExistente);
            }
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuario não encontrado com ID: " + id));
    }

    // Método para buscar um usuário por ID
    public Optional<Usuario> buscarUsuarioPorId(String id) {
        return usuarioRepository.findById(id);
    }

    // Método para listar todos os usuários
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    // Método para deletar um usuário
    public void deletarUsuario(String id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
        } else {
            throw new RuntimeException("Usuário com ID " + id + " não encontrado.");
        }
    }
    public Optional<Usuario> buscarUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> pesquisarUsuarios(String nome, String tipoUsuario) {
        if ((nome == null || nome.isEmpty()) && (tipoUsuario == null || tipoUsuario.isEmpty())) {
            return usuarioRepository.findAll();
        }

        return usuarioRepository.findAll().stream()
                .filter(usuario -> (nome == null || nome.isEmpty() || usuario.getNome().contains(nome)))
                .filter(usuario -> (tipoUsuario == null || tipoUsuario.isEmpty() || usuario.getTipoUsuario().equals(tipoUsuario)))
                .collect(Collectors.toList());
    }
    public Optional<Usuario> buscarUsuarioPorEmailESenha(String email, String senha) {
        return usuarioRepository.findByEmailAndSenha(email, senha);
    }
    
}
