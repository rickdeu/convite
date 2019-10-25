package com.fenix.wakonga.model;

public class FundoEstiloConvite {
    String fundEstiloConvite;

    public FundoEstiloConvite() {
    }
    public FundoEstiloConvite(String fundEstiloConvite) {
        this.fundEstiloConvite = fundEstiloConvite;
    }

    public String getFundEstiloConvite() {
        return fundEstiloConvite;
    }

    public void setFundEstiloConvite(String fundEstiloConvite) {
        this.fundEstiloConvite = fundEstiloConvite;
    }

    @Override
    public String toString() {
        return "FundoEstiloConvite";
    }
}
