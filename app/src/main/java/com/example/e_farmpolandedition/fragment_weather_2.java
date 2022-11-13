package com.example.e_farmpolandedition;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class fragment_weather_2 extends Fragment {

    TextView kierunekWiatru, godzina, data, wilgotnosc, predkoscWiatru;

    ViewModelWeather viewModel;
    Observer<weatherDataClass> weatherDataClassObserver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather_2, container, false);

        kierunekWiatru = (TextView) view.findViewById(R.id.kierunekWiatruV);
        wilgotnosc = (TextView) view.findViewById(R.id.wilgotnoscV);
        data = (TextView) view.findViewById(R.id.dataV);
        godzina = (TextView) view.findViewById(R.id.godzinaV);
        predkoscWiatru = (TextView) view.findViewById(R.id.predkoscWiatruV);

        viewModel = new ViewModelProvider(requireActivity()).get(ViewModelWeather.class);
        weatherDataClassObserver = new Observer<weatherDataClass>() {
            @Override
            public void onChanged(weatherDataClass s ) {

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

                wilgotnosc.setText(String.valueOf(s.getWilgotnosc_wzgledna()));

                DateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                data.setText(String.valueOf(df.format(s.getData_pomiaru())));

                godzina.setText(String.valueOf(s.getGodzina_pomiaru())+":00");
                predkoscWiatru.setText(String.valueOf(s.getPredkosc_wiatru())+"km/h");

            }
        };
        viewModel.dataToPrint.observe(getViewLifecycleOwner(), weatherDataClassObserver);
        return view;
    }
}