package com.example.e_farmpolandedition;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class weatherDataClass {
        int id_stacji = 0, godzina_pomiaru=0 , kierunek_wiatru=0;
        Float suma_opadu =0.0F;
        String stacja = "Brak";
        Date data_pomiaru = new Date(1901,01,01);
        float temperatura = 0.0F, predkosc_wiatru= 0.0F, wilgotnosc_wzgledna = 0.0F, cisnienie=0.0F;

        public weatherDataClass() {
        }

        public weatherDataClass(int id_stacji, int godzina_pomiaru,
                                int kierunek_wiatru, float suma_opadu,
                                String stacja, Date data_pomiaru, float temperatura,
                                float predkosc_wiatru, float wilgotnosc_wzgledna, float cisnienie) {
            this.id_stacji = id_stacji;
            this.godzina_pomiaru = godzina_pomiaru;
            this.kierunek_wiatru = kierunek_wiatru;
            this.suma_opadu = suma_opadu;
            this.stacja = stacja;
            this.data_pomiaru = data_pomiaru;
            this.temperatura = temperatura;
            this.predkosc_wiatru = predkosc_wiatru;
            this.wilgotnosc_wzgledna = wilgotnosc_wzgledna;
            this.cisnienie = cisnienie;
        }

        public float getSuma_opadu() {
            return suma_opadu;
        }

        public void setSuma_opadu(String suma_opadu) {
            float suma_o = Float.valueOf(suma_opadu);
            this.suma_opadu = suma_o;
        }

        public Date getData_pomiaru() {
            return data_pomiaru;
        }

        public void setData_pomiaru(String data_pomiaru) throws ParseException {
            SimpleDateFormat formatter1=new SimpleDateFormat("yyyy-MM-dd");
            Date data_tmp = formatter1.parse(data_pomiaru);
            this.data_pomiaru = data_tmp;
        }

        public int getId_stacji() {
            return id_stacji;
        }

        public void setId_stacji(String id_stacji) {
            this.id_stacji = Integer.valueOf(id_stacji);
        }

        public int getGodzina_pomiaru() {
            return godzina_pomiaru;
        }

        public void setGodzina_pomiaru(String godzina_pomiaru) {
            this.godzina_pomiaru = Integer.valueOf(godzina_pomiaru);
        }

        public int getKierunek_wiatru() {
            return kierunek_wiatru;
        }

        public void setKierunek_wiatru(String kierunek_wiatru) {
            this.kierunek_wiatru = Integer.valueOf(kierunek_wiatru);
        }


        public String getStacja() {
            return stacja;
        }

        public void setStacja(String stacja) {
            this.stacja = stacja;
        }


        public float getTemperatura() {
            return temperatura;
        }

        public void setTemperatura(String temperatura) {
            this.temperatura = Float.valueOf(temperatura);
        }

        public float getPredkosc_wiatru() {
            return predkosc_wiatru;
        }

        public void setPredkosc_wiatru(String predkosc_wiatru) {
            this.predkosc_wiatru = Float.valueOf(predkosc_wiatru);
        }

        public float getWilgotnosc_wzgledna() {
            return wilgotnosc_wzgledna;
        }

        public void setWilgotnosc_wzgledna(String wilgotnosc_wzgledna) {
            this.wilgotnosc_wzgledna = Float.valueOf(wilgotnosc_wzgledna);
        }

        public float getCisnienie() {
            return cisnienie;
        }

        public void setCisnienie(String cisnienie) {
            if(!cisnienie.equals("null") && cisnienie!=null) {this.cisnienie = Float.valueOf(cisnienie);}
            else {this.cisnienie = 0;}
        }

}
