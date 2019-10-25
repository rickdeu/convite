package com.fenix.wakonga.model;

public class ServicoCategoria {

    private String nome;
    private String imagem;

    public ServicoCategoria() {
    }

    public ServicoCategoria(String nome, String imagem) {
        this.nome = nome;
        this.imagem = imagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }
}
