package com.example.e_farmpolandedition;

public class zabieg {
    private String nazwaZabiegu;
    private String dataRozpoczecia;
    private String okresKarencji;
    private String dawka;
    private Float koszt;

    public zabieg() {
    }

    public zabieg(String nazwaZabiegu, String dataRozpoczecia, String okresKarencji, String dawka, Float koszt) {
        this.nazwaZabiegu = nazwaZabiegu;
        this.dataRozpoczecia = dataRozpoczecia;
        this.okresKarencji = okresKarencji;
        this.dawka = dawka;
        this.koszt = koszt;
    }

    public String getNazwaZabiegu() {
        return nazwaZabiegu;
    }

    public void setNazwaZabiegu(String nazwaZabiegu) {
        this.nazwaZabiegu = nazwaZabiegu;
    }

    public String getDataRozpoczecia() {
        return dataRozpoczecia;
    }

    public void setDataRozpoczecia(String dataRozpoczecia) {
        this.dataRozpoczecia = dataRozpoczecia;
    }

    public String getOkresKarencji() {
        return okresKarencji;
    }

    public void setOkresKarencji(String okresKarencji) {
        this.okresKarencji = okresKarencji;
    }

    public String getDawka() {
        return dawka;
    }

    public void setDawka(String dawka) {
        this.dawka = dawka;
    }

    public Float getKoszt() {
        return koszt;
    }

    public void setKoszt(Float koszt) {
        this.koszt = koszt;
    }
}
