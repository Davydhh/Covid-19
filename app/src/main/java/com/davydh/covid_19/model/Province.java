package com.davydh.covid_19.model;

public class Province {
    private final String data;
    private final String stato;
    private final int codiceRegione;
    private final String nomeRegione;
    private final int codiceProvincia;
    private final String nomeProvincia;
    private final String siglaProvincia;
    private final int totaleCasi;

    public Province(String data, String stato, int codiceRegione, String nomeRegione, int codiceProvincia, String nomeProvincia, String siglaProvincia, int totaleCasi) {
        this.data = data;
        this.stato = stato;
        this.codiceRegione = codiceRegione;
        this.nomeRegione = nomeRegione;
        this.codiceProvincia = codiceProvincia;
        this.nomeProvincia = nomeProvincia;
        this.siglaProvincia = siglaProvincia;
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

    public int getTotaleCasi() {
        return totaleCasi;
    }

    public String getNomeProvincia() {
        return nomeProvincia;
    }

    @Override
    public String toString() {
        return "Province{" +
                "data='" + data + '\'' +
                ", stato='" + stato + '\'' +
                ", codiceRegione=" + codiceRegione +
                ", nomeRegione='" + nomeRegione + '\'' +
                ", codiceProvincia=" + codiceProvincia +
                ", nomeProvincia='" + nomeProvincia + '\'' +
                ", siglaProvincia='" + siglaProvincia + '\'' +
                ", totaleCasi=" + totaleCasi +
                '}';
    }
}
