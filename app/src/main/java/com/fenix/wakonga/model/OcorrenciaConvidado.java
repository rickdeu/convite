package com.fenix.wakonga.model;

public class OcorrenciaConvidado {

   private   String ocorrencia;
    private   String idConvidado;


    public OcorrenciaConvidado() {
    }

    public OcorrenciaConvidado(String ocorrencia, String idConvidado) {
        this.ocorrencia = ocorrencia;
        this.idConvidado = idConvidado;
    }

    public String getOcorrencia() {
        return ocorrencia;
    }

    public void setOcorrencia(String ocorrencia) {
        this.ocorrencia = ocorrencia;
    }

    public String getIdConvidado() {
        return idConvidado;
    }

    public void setIdConvidado(String idConvidado) {
        this.idConvidado = idConvidado;
    }

    @Override
    public String toString() {
        return "OcorrenciaConvidado";
    }
}
