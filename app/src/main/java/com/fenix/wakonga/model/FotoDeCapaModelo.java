package com.fenix.wakonga.model;

public class FotoDeCapaModelo {
    private String foto;

    public FotoDeCapaModelo() {
    }

    public FotoDeCapaModelo(String foto) {
        this.foto = foto;
    }

    public String getFoto() {
        return foto;
    }
    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "FotoDeCapaModelo";
    }
}
