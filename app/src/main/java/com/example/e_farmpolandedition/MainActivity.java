package com.example.e_farmpolandedition;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private String previousAct;
    ProgressDialog progressDialog;
    ArrayList<weatherDataClass> danePomiarowe;
    Handler weatherHandler = new Handler();

    private FragmentManager fragmentManager;
    private Fragment fragment1;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(previousAct.equals("StartActivity")) {
            Intent myIntent = new Intent(MainActivity.this, StartActivity.class);
            MainActivity.this.startActivity(myIntent);
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.main_user_view);
        this.previousAct= getIntent().getExtras().getString("previousActivity");

        fragment1 = new WeatherFragment();
        fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        danePomiarowe = new ArrayList<>();
        new weatherData().start();


        transaction.add(R.id.weather_layout, fragment1);
        transaction.addToBackStack(null);
        transaction.commit();


    }


    class weatherData extends Thread {
        String data;
        @Override
        public void run() {
            weatherHandler.post(new Runnable() {
                @Override
                public void run() {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Pobieram Dane");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            });
            try {
                URL url = new URL("https://danepubliczne.imgw.pl/api/data/synop");
                HttpURLConnection httpURL = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURL.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = bufferedReader.readLine()) != null && !line.equals("null")){
                    data = data + line;
                }

                if(!data.isEmpty()) {
                    ArrayList<weatherDataClass> dat = new ArrayList<>();

                    //Because api returns json with prefix - null - problem with convert string to json
                    String nullPrefix = "null";
                    if(data.startsWith(nullPrefix)){
                        data = data.substring(nullPrefix.length(), data.length());
                    }

                    JSONArray jsonArray = new JSONArray(data);

                    danePomiarowe.clear();
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject stacja = jsonArray.getJSONObject(i);
                        weatherDataClass t = new weatherDataClass();
                        try {
                            t.setId_stacji(stacja.getString("id_stacji"));
                            t.setGodzina_pomiaru(stacja.getString("godzina_pomiaru"));
                            t.setKierunek_wiatru(stacja.getString("kierunek_wiatru"));
                            t.setSuma_opadu(stacja.getString("suma_opadu"));
                            t.setStacja(stacja.getString("stacja"));
                            t.setData_pomiaru(stacja.getString("data_pomiaru"));
                            t.setTemperatura(stacja.getString("temperatura"));
                            t.setPredkosc_wiatru(stacja.getString("predkosc_wiatru"));
                            t.setWilgotnosc_wzgledna(stacja.getString("wilgotnosc_wzgledna"));
                            t.setCisnienie(stacja.getString("cisnienie"));
                        }catch(Exception e){
                            Log.e("INSERT ERROR: ", String.valueOf(e));
                        }
                        dat.add(t);
                    }
                    setDanePomiarowe(dat);
                }


            }catch (Exception e){

            }

            weatherHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(progressDialog.isShowing()) progressDialog.dismiss();
                }
            });


        }
    }

    class weatherDataClass {
        int id_stacji, godzina_pomiaru, kierunek_wiatru;
        Float suma_opadu;
        String stacja;
        Date data_pomiaru;
        float temperatura, predkosc_wiatru, wilgotnosc_wzgledna, cisnienie;

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

    private final void setDanePomiarowe(ArrayList<weatherDataClass> t){
        this.danePomiarowe = t;
        Log.e("Len of data from API: ", String.valueOf(danePomiarowe.size()));
    }
}
