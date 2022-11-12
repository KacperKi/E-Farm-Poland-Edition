package com.example.e_farmpolandedition;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

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


public class MainActivity extends AppCompatActivity{

    private String previousAct;
    ProgressDialog progressDialog;
    ArrayList<weatherDataClass> danePomiarowe;
    Handler weatherHandler = new Handler();
    ViewModelWeather viewModelWeather;

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
        viewModelWeather = ViewModelProviders.of(MainActivity.this).get(ViewModelWeather.class);

        fragment1 = new WeatherFragment();
        fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        danePomiarowe = new ArrayList<>();
        viewModelWeather.setData(danePomiarowe);
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

    private final void setDanePomiarowe(ArrayList<weatherDataClass> t){
        this.danePomiarowe = t;
        viewModelWeather.setData(t);
        Log.e("Len of data from API: ", String.valueOf(danePomiarowe.size()));
    }
}
