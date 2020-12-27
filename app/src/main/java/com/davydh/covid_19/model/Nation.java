package com.davydh.covid_19.model;

public class Nation {
    private String data;
    private String stato;
    private int ricoveratiConSintomi;
    private int terapiaIntensiva;
    private int totaleOspedalizzati;
    private int isolamentoDomiciliare;
    private int attualmentePositivi;
    private int nuoviPositivi;
    private int dimessi;
    private int deceduti;
    private int totaleCasi;
    private int tamponi;
    private int totaleNuoviPositivi;
    private int ingressiTerapiaIntensiva;
    private int casiTestati;

    public Nation(String data, String stato, int ricoveratiConSintomi, int terapiaIntensiva,
                  int totaleOspedalizzati, int isolamentoDomiciliare, int attualmentePositivi,
                  int nuoviPositivi, int dimessi, int deceduti, int totaleCasi, int tamponi,
                  int totaleNuoviPositivi, int ingressiTerapiaIntensiva, int casiTestati) {
        this.data = data;
        this.stato = stato;
        this.ricoveratiConSintomi = ricoveratiConSintomi;
        this.terapiaIntensiva = terapiaIntensiva;
        this.totaleOspedalizzati = totaleOspedalizzati;
        this.isolamentoDomiciliare = isolamentoDomiciliare;
        this.attualmentePositivi = attualmentePositivi;
        this.nuoviPositivi = nuoviPositivi;
        this.dimessi = dimessi;
        this.deceduti = deceduti;
        this.totaleCasi = totaleCasi;
        this.tamponi = tamponi;
        this.totaleNuoviPositivi = totaleNuoviPositivi;
        this.ingressiTerapiaIntensiva = ingressiTerapiaIntensiva;
        this.casiTestati = casiTestati;
    }

    public String getData() {
        return data;
    }

    public String getStato() {
        return stato;
    }

    public int getRicoveratiConSintomi() {
        return ricoveratiConSintomi;
    }

    public int getTerapiaIntensiva() {
        return terapiaIntensiva;
    }

    public int getTotaleOspedalizzati() {
        return totaleOspedalizzati;
    }

    public int getIsolamentoDomiciliare() {
        return isolamentoDomiciliare;
    }

    public int getAttualmentePositivi() {
        return attualmentePositivi;
    }

    public int getNuoviPositivi() {
        return nuoviPositivi;
    }

    public int getDimessi() {
        return dimessi;
    }

    public int getDeceduti() {
        return deceduti;
    }

    public int getTotaleCasi() {
        return totaleCasi;
    }

    public int getTamponi() {
        return tamponi;
    }

    public int getTotaleNuoviPositivi() { return totaleNuoviPositivi; }

    public int getIngressiTerapiaIntensiva() {
        return ingressiTerapiaIntensiva;
    }

    public int getCasiTestati() {
        return casiTestati;
    }

    @Override
    public String toString() {
        return "Nation{" +
                "data='" + data + '\'' +
                ", stato='" + stato + '\'' +
                ", ricoveratiConSintomi=" + ricoveratiConSintomi +
                ", terapiaIntensiva=" + terapiaIntensiva +
                ", totaleOspedalizzati=" + totaleOspedalizzati +
                ", isolamentoDomiciliare=" + isolamentoDomiciliare +
                ", attualmentePositivi=" + attualmentePositivi +
                ", nuoviPositivi=" + nuoviPositivi +
                ", dimessi=" + dimessi +
                ", deceduti=" + deceduti +
                ", totaleCasi=" + totaleCasi +
                ", tamponi=" + tamponi +
                '}';
    }
}
