package com.davydh.covid_19.model;

import java.util.Objects;

public class Region {
    //Covid Data
    private String data;
    private String stato;
    private int codice;
    private String nome;
    private double latitude;
    private double longitude;
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
    private String note;
    private int totaleNuoviPositivi;
    private int variazioneTotalePositivi;
    private int ingressiTerapiaIntensiva;

    //Vaccines Data
    private String area;
    private int dosiSomministrate;
    private int dosiConsegnate;
    private double percentualeSomministrazione;

    public Region(String area, int dosiSomministrate, int dosiConsegnate, double percentualeSomministrazione) {
        this.area = area;
        this.dosiSomministrate = dosiSomministrate;
        this.dosiConsegnate = dosiConsegnate;
        this.percentualeSomministrazione = percentualeSomministrazione;
    }

    public Region(String data, String stato, int codice, String nome, double latitude,
                  double longitude, int ricoveratiConSintomi, int terapiaIntensiva, int totaleOspedalizzati,
                  int isolamentoDomiciliare, int attualmentePositivi, int nuoviPositivi, int dimessi,
                  int deceduti, int totaleCasi, int tamponi, String note, int totaleNuoviPositivi,
                  int variazioneTotalePositivi, int ingressiTerapiaIntensiva) {
        this.data = data;
        this.stato = stato;
        this.codice = codice;
        this.nome = nome;
        this.latitude = latitude;
        this.longitude = longitude;
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
        this.note = note;
        this.totaleNuoviPositivi = totaleNuoviPositivi;
        this.variazioneTotalePositivi = variazioneTotalePositivi;
        this.ingressiTerapiaIntensiva = ingressiTerapiaIntensiva;
    }

    public String getData() {
        return data;
    }

    public String getStato() {
        return stato;
    }

    public int getCodice() {
        return codice;
    }

    public String getNome() {
        return nome;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
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

    public String getNote() {
        return note;
    }

    public int getTotaleNuoviPositivi() {
        return totaleNuoviPositivi;
    }

    public String getArea() {
        return area;
    }

    public int getDosiSomministrate() {
        return dosiSomministrate;
    }

    public int getDosiConsegnate() {
        return dosiConsegnate;
    }

    public double getPercentualeSomministrazione() {
        return percentualeSomministrazione;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return codice == region.codice &&
                data.equals(region.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, codice);
    }

    @Override
    public String toString() {
        return "Region{" +
                "data='" + data + '\'' +
                ", stato='" + stato + '\'' +
                ", codice=" + codice +
                ", nome='" + nome + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
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
                "}\n";
    }
}
