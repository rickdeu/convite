package com.fenix.wakonga.model;

import java.util.Date;

public class Noivo {
    private String nome;
    private String sobrenome;
    private String dataNascimento;
    private String telefone;
    private String casa;
    private String bairro;
    private String foto;
    private String email;
    private String bi;


    public Noivo() {
    }

    public Noivo(String nome, String sobrenome, String dataNascimento, String telefone, String casa, String bairro, String foto, String email, String bi) {
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.casa = casa;
        this.bairro = bairro;
        this.foto = foto;
        this.email = email;
        this.bi = bi;

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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCasa() {
        return casa;
    }

    public void setCasa(String casa) {
        this.casa = casa;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getBi() {
        return bi;
    }

    public void setBi(String bi) {
        this.bi = bi;
    }

    @Override
    public String toString() {
        return "Noivo";
    }
}
