package com.davydh.covid_19.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Nation implements Parcelable {
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

    protected Nation(Parcel in) {
        data = in.readString();
        stato = in.readString();
        ricoveratiConSintomi = in.readInt();
        terapiaIntensiva = in.readInt();
        totaleOspedalizzati = in.readInt();
        isolamentoDomiciliare = in.readInt();
        attualmentePositivi = in.readInt();
        nuoviPositivi = in.readInt();
        dimessi = in.readInt();
        deceduti = in.readInt();
        totaleCasi = in.readInt();
        tamponi = in.readInt();
        totaleNuoviPositivi = in.readInt();
        ingressiTerapiaIntensiva = in.readInt();
        casiTestati = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(data);
        dest.writeString(stato);
        dest.writeInt(ricoveratiConSintomi);
        dest.writeInt(terapiaIntensiva);
        dest.writeInt(totaleOspedalizzati);
        dest.writeInt(isolamentoDomiciliare);
        dest.writeInt(attualmentePositivi);
        dest.writeInt(nuoviPositivi);
        dest.writeInt(dimessi);
        dest.writeInt(deceduti);
        dest.writeInt(totaleCasi);
        dest.writeInt(tamponi);
        dest.writeInt(totaleNuoviPositivi);
        dest.writeInt(ingressiTerapiaIntensiva);
        dest.writeInt(casiTestati);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Nation> CREATOR = new Parcelable.Creator<Nation>() {
        @Override
        public Nation createFromParcel(Parcel in) {
            return new Nation(in);
        }

        @Override
        public Nation[] newArray(int size) {
            return new Nation[size];
        }
    };
}