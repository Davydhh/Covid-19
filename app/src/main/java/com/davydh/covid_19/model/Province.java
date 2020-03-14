package com.davydh.covid_19.model;

public class Province {
    private String data;
    private String stato;
    private int codiceRegione;
    private String nomeRegione;
    private int codiceProvincia;
    private String nomeProvincia;
    private String siglaProvincia;
    private double latitude;
    private double longitude;
    private int totaleCasi;

    public Province(String data, String stato, int codiceRegione, String nomeRegione, int codiceProvincia, String nomeProvincia, String siglaProvincia, double latitude, double longitude, int totaleCasi) {
        this.data = data;
        this.stato = stato;
        this.codiceRegione = codiceRegione;
        this.nomeRegione = nomeRegione;
        this.codiceProvincia = codiceProvincia;
        this.nomeProvincia = nomeProvincia;
        this.siglaProvincia = siglaProvincia;
        this.latitude = latitude;
        this.longitude = longitude;
        this.totaleCasi = totaleCasi;
    }

    public String getData() {
        return data;
    }

    public String getStato() {
        return stato;
    }

    public int getCodiceRegione() {
        return codiceRegione;
    }

    public String getNomeRegione() {
        return nomeRegione;
    }

    public int getCodiceProvincia() {
        return codiceProvincia;
    }

    public String getSiglaProvincia() {
        return siglaProvincia;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getTotaleCasi() {
        return totaleCasi;
    }

    public String getNomeProvincia() {
        return nomeProvincia;
    }
}
