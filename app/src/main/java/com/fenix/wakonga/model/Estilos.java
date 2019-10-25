package com.fenix.wakonga.model;

public class Estilos {
    String nome, convite, preco;

    public Estilos() {
    }

    public Estilos(String nome, String convite, String preco) {
        this.nome = nome;
        this.convite = convite;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getConvite() {
        return convite;
    }

    public void setConvite(String convite) {
        this.convite = convite;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }
}
