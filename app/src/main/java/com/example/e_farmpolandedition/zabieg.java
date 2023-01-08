package com.example.e_farmpolandedition;

public class zabieg {
    private String nazwaZabiegu;
    private String dataRozpoczecia;
    private String uprawaUsera;
    private String okresKarencji;
    private String dawka;
    private String rodzajSrodka;
    private String koszt;

    public zabieg() {
    }

    public zabieg(String nazwaZabiegu, String dataRozpoczecia, String okresKarencji, String uprawaUsera, String dawka, String koszt, String rodzajSrodka) {
        this.nazwaZabiegu = nazwaZabiegu;
        this.dataRozpoczecia = dataRozpoczecia;
        this.okresKarencji = okresKarencji;
        this.dawka = dawka;
        this.uprawaUsera = uprawaUsera;
        this.koszt = koszt;
        this.rodzajSrodka = rodzajSrodka;
    }

    public String getUprawaUsera() {
        return uprawaUsera;
    }

    public void setUprawaUsera(String uprawaUsera) {
        this.uprawaUsera = uprawaUsera;
    }

    public String getRodzajSrodka() {
        return rodzajSrodka;
    }

    public void setRodzajSrodka(String rodzajSrodka) {
        this.rodzajSrodka = rodzajSrodka;
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

    public String getKoszt() {
        return koszt;
    }

    public void setKoszt(String koszt) {
        this.koszt = koszt;
    }
}
