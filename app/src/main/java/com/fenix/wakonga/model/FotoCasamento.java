package com.fenix.wakonga.model;

public class FotoCasamento {
    private String title;
    private String foto;
    private String timestamp;

    public FotoCasamento() {
    }

    public FotoCasamento(String title, String foto, String timestamp) {
        this.title = title;
        this.foto = foto;
        this.timestamp = timestamp;
    }

    public FotoCasamento(String title, String foto) {
        this.title = title;
        this.foto = foto;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }


    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "FotoCasamento";
    }
}
