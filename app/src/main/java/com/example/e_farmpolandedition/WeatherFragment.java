package com.example.e_farmpolandedition;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class WeatherFragment extends Fragment {

    TextView id_stacji, temperatura, suma_opadu, cisnienie;
    Integer position = 0;

    ViewModelWeather viewModel;

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

        viewModel = ViewModelProviders.of(getActivity()).get(ViewModelWeather.class);

        viewModel.getData().observe(getActivity(), new Observer<ArrayList<weatherDataClass>>() {
                    @Override
                    public void onChanged(ArrayList<weatherDataClass> weatherDataClasses) {
                        if(!weatherDataClasses.isEmpty()) {
                            weatherDataClass t = weatherDataClasses.get(0);
                            id_stacji.setText(String.valueOf(t.getId_stacji()));
                            temperatura.setText(String.valueOf(t.getTemperatura()));
                            suma_opadu.setText(String.valueOf(t.getSuma_opadu()));
                            cisnienie.setText(String.valueOf(t.getCisnienie()));
                        }
                    }
                });

        return view;
    }


}