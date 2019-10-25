package com.fenix.wakonga.model;

public class Dizeres {
    private String texto1;
    private String texto2;
    private String mensagem3;

    public Dizeres() {
    }

    public Dizeres(String texto1, String texto2, String mensagem3) {
        this.texto1 = texto1;
        this.texto2 = texto2;
        this.mensagem3 = mensagem3;
    }

    public String getTexto1() {
        return texto1;
    }

    public void setTexto1(String texto1) {
        this.texto1 = texto1;
    }

    public String getTexto2() {
        return texto2;
    }

    public void setTexto2(String texto2) {
        this.texto2 = texto2;
    }

    public String getMensagem3() {
        return mensagem3;
    }

    public void setMensagem3(String mensagem3) {
        this.mensagem3 = mensagem3;
    }

    @Override
    public String toString() {
        return "Dizeres";
    }
}
