package com.fenix.wakonga.model;

import java.util.Date;

public class Igreja {
    private String nome;
    private String desc;
    private String hora;
    private String data;

    public Igreja() {

    }
    public Igreja(String nome, String desc, String hora, String data) {
        this.nome = nome;
        this.desc = desc;
        this.hora = hora;
        this.data = data;
    }
    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getDesc() {
        return desc;
    }
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    @Override
    public String toString() {
        return "Igreja";
    }
}
