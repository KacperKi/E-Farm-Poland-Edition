package com.example.e_farmpolandedition;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
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

    Context context;
    private String previousAct;
    ProgressDialog progressDialog;
    ArrayList<weatherDataClass> danePomiarowe;
    Handler weatherHandler = new Handler();
    ViewModelWeather viewModelWeather;
    CardView selectWoj,twojeUprawy;
    String currentFrag;

    Handler handler = new Handler();
    Runnable run;

    private int wojewodztwo=0;
    private FragmentManager fragmentManager;
    private Fragment fragment1, fragment2;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.main_user_view);
        context = getApplicationContext();

        find_objects();
        create_listeners();


        //To verify previous step in app.
        this.previousAct= getIntent().getExtras().getString("previousActivity");

        viewModelWeather = ViewModelProviders.of(MainActivity.this).get(ViewModelWeather.class);

        danePomiarowe = new ArrayList<>();
        viewModelWeather.setData(new weatherDataClass());
        new weatherData().start();

        set_fragment_in_app();
        run_reading_data();
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

    private void find_objects(){
        this.selectWoj = findViewById(R.id.wojewodztwoButton);
        this.twojeUprawy = findViewById(R.id.twojeUprawy);
    }

    private void create_listeners(){
        this.selectWoj.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Wybierz stację pomiarową!");
                ArrayList<String> elements = new ArrayList<>();
                for(weatherDataClass w: danePomiarowe){
                    elements.add(w.getStacja());
                }
                builder.setItems(elements.toArray(new String[elements.size()]), (dialog, which) -> {
                    wojewodztwo = which;
                    viewModelWeather.dataToPrint.setValue(danePomiarowe.get(wojewodztwo));
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            };
        });
        this.selectWoj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentFrag.equals("First Frag")) {
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                    R.anim.eneter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.weather_layout, fragment2)
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                    currentFrag = "Sec Frag";
                } else{
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                    R.anim.eneter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.weather_layout, fragment1)
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .commit();
                    currentFrag= "First Frag";
                }
            }
        });
        this.twojeUprawy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, twojeUprawy.class);
                MainActivity.this.startActivity(myIntent);
            }
        });
    }

    private void set_fragment_in_app(){
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragment1 = new WeatherFragment();
        fragment2 = new fragment_weather_2();

        transaction.add(R.id.weather_layout, fragment1, "First Frag");
        currentFrag = "First Frag";
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void run_reading_data(){
        run = new Runnable() {
            @Override
            public void run() {
                if(danePomiarowe.isEmpty()) viewModelWeather.setData(new weatherDataClass());
                else viewModelWeather.setData(danePomiarowe.get(wojewodztwo));
                Log.e("Data Runnable - ", "RUN FUNCTION TO UPDATE DATA FROM API");
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(run, 5000);
    }

    private final void setDanePomiarowe(ArrayList<weatherDataClass> t){
        this.danePomiarowe = t;
        viewModelWeather.dataToPrint.setValue(t.get(wojewodztwo));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(previousAct.equals("StartActivity")) {
            Intent myIntent = new Intent(MainActivity.this, StartActivity.class);
            MainActivity.this.startActivity(myIntent);
            finish();
        }

    }

}
