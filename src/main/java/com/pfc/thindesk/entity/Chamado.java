package com.pfc.thindesk.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "chamados")
public class Chamado {

    @Id
    private String id;
    private String titulo;
    private String descricao;
    private String status; // EM_ANDAMENTO, ENCERRADO
    private LocalDateTime dataCriacao;
    private String usuarioSolicitanteId;
    private String tecnicoResponsavelId;
    private String tecnicoResponsavelNome; 
    private String prioridade; // BAIXA, MEDIA, ALTA
    private String nivelChamado; // NIVEL 1, NIVEL 2, NIVEL 3
    Tecnico tecnico;

    // Getters e Setters
    public String getNivelChamado() {
        return nivelChamado;
    }

    public void setNivelChamado(String nivelChamado) {
        this.nivelChamado = nivelChamado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Tecnico getTecnico() {
        

        return tecnico;

    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public String getUsuarioSolicitanteId() {
        return usuarioSolicitanteId;
    }

    public void setUsuarioSolicitanteId(String usuarioSolicitanteId) {
        this.usuarioSolicitanteId = usuarioSolicitanteId;
    }

    public String getTecnicoResponsavelId() {
        return tecnicoResponsavelId;
    }

    public void setTecnicoResponsavelId(String tecnicoResponsavelId) {
        this.tecnicoResponsavelId = tecnicoResponsavelId;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    @Override
    public String toString() {
        return "Chamado{"
                + "id='" + id + '\''
                + ", titulo='" + titulo + '\''
                + ", descricao='" + descricao + '\''
                + ", status='" + status + '\''
                + ", dataCriacao=" + dataCriacao
                + ", usuarioSolicitanteId='" + usuarioSolicitanteId + '\''
                + ", tecnicoResponsavelId='" + tecnicoResponsavelId + '\''
                + ", prioridade='" + prioridade + '\''
                + ", nivelChamado='" + nivelChamado + '\''
                + '}';
    }

    public void setUsuario(Usuario orElse) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setTecnico(Tecnico orElse) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

        // Getters e Setters
        public String getTecnicoResponsavelNome() {
            return tecnicoResponsavelNome;
        }
    
        public void setTecnicoResponsavelNome(String tecnicoResponsavelNome) {
            this.tecnicoResponsavelNome = tecnicoResponsavelNome;
        }
}
