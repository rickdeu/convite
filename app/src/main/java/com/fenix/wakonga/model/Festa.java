package com.fenix.wakonga.model;

import java.util.Date;

public class Festa {

    private String salao;
    private String data;
    private String hora;
    private String desc;





    public Festa() {
    }

    public Festa(String salao, String data, String hora, String desc) {
        this.salao = salao;
        this.data = data;
        this.hora = hora;
        this.desc = desc;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSalao() {
        return salao;
    }

    public void setSalao(String salao) {
        this.salao = salao;
    }

    @Override
    public String toString() {
        return "Festa";
    }
}
