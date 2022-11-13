package com.example.e_farmpolandedition;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class WeatherFragment extends Fragment {

    TextView id_stacji, temperatura, suma_opadu, cisnienie, kierunekWiatru,nazwaStacji;
    Integer position = 0;

    ViewModelWeather viewModel;
    Observer<weatherDataClass> weatherDataClassObserver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_weather, container, false);

        id_stacji = (TextView) view.findViewById(R.id.id_stacji_v);
        temperatura = (TextView) view.findViewById(R.id.temperaturaV);
        suma_opadu = (TextView) view.findViewById(R.id.suma_opaduV);
        cisnienie = (TextView) view.findViewById(R.id.cisnienienieV);
        kierunekWiatru = (TextView) view.findViewById(R.id.kierunekWiatruV);
        nazwaStacji = (TextView) view.findViewById(R.id.nazwaStacji);

        viewModel = new ViewModelProvider(requireActivity()).get(ViewModelWeather.class);
        weatherDataClassObserver = new Observer<weatherDataClass>() {
            @Override
            public void onChanged(weatherDataClass s ) {
                    id_stacji.setText(String.valueOf(s.getId_stacji()));
                    temperatura.setText(String.valueOf(s.getTemperatura())+"\u2103");
                    suma_opadu.setText(String.valueOf(s.getSuma_opadu()));
                    cisnienie.setText(String.valueOf(s.getCisnienie())+"hPa");

                    int k = s.getKierunek_wiatru();
                    Log.e("K VALUE", String.valueOf(k));
                    String kI = "";
                    if(k<12 && k>=0 || k>348 && k<360 ) kI = "pół";
                    if(k>11 && k<34) kI = "pół-pół\n-wsch";
                    if(k>33 && k<57) kI = "pół-wsch";
                    if(k>56 && k<79) kI = "wsch-pół\n-wsch";
                    if(k>78 && k<102) kI = "wsch";
                    if(k>101 && k<124) kI = "wsch-poł\n-wsch";
                    if(k>123 && k<147) kI = "poł-wsch";
                    if(k>146 && k<169) kI = "poł-poł\n-wsch";
                    if(k>168 && k<192) kI = "poł";
                    if(k>191 && k<214) kI = "poł-poł\n-zach";
                    if(k>213 && k<237) kI = "poł-zach";
                    if(k>236 && k<259) kI = "zach-poł\n-zach";
                    if(k>258 && k<282) kI = "zach";
                    if(k>281 && k<304) kI = "zach-pół\n-zach";
                    if(k>303 && k<327) kI = "pół-zach";
                    if(k>326 && k<349) kI = "pół-pół\n-zach";
                    kierunekWiatru.setText(kI);
                    nazwaStacji.setText(s.getStacja());
            }
        };
        viewModel.dataToPrint.observe(getViewLifecycleOwner(), weatherDataClassObserver);

        return view;
    }


}