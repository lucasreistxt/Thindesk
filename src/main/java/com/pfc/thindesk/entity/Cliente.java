package com.pfc.thindesk.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "clientes")
public class Cliente {

    @Id
    @javax.persistence.Id
    private String id;
    private String nome;
    private String empresa;
    private String contato;
    private boolean isActive = true;
    

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public String getContato() {
        return contato;
    }
    
    public boolean getIsActive() {

        return isActive;

    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public void setIsActive(boolean isActive) {

        this.isActive = isActive;

    }
}






