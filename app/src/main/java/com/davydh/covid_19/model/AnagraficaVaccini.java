package com.davydh.covid_19.model;

public class AnagraficaVaccini {
    private final String fasciaAnagrafica;
    private final int totale;

    public AnagraficaVaccini(String fasciaAnagrafica, int totale) {
        this.fasciaAnagrafica = fasciaAnagrafica;
        this.totale = totale;
    }

    public String getFasciaAnagrafica() {
        return fasciaAnagrafica;
    }

    public int getTotale() {
        return totale;
    }
}
