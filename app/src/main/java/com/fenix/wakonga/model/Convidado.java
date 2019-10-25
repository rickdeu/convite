package com.fenix.wakonga.model;

public class Convidado {
    private String nome;
    private String sobrenome;
    private String email;
    private String telefone;
    private String acompanhante;
    private String foto;
    private int numeroConvidados;


    public Convidado() {

    }

    public Convidado(String nome, String sobrenome, String email, String telefone, String acompanhante, String foto, int numeroConvidados) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.email = email;
        this.telefone = telefone;
        this.acompanhante = acompanhante;
        this.foto = foto;
        this.numeroConvidados = numeroConvidados;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getAcompanhante() {
        return acompanhante;
    }

    public void setAcompanhante(String acompanhante) {
        this.acompanhante = acompanhante;
    }

    public int getNumeroConvidados() {
        return numeroConvidados;
    }

    public void setNumeroConvidados(int numeroConvidados) {
        this.numeroConvidados = numeroConvidados;
    }

    @Override
    public String toString() {
        return "Convidados";
    }
}
