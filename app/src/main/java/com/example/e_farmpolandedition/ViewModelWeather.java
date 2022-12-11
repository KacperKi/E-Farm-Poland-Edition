package com.example.e_farmpolandedition;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ViewModelWeather extends ViewModel {

    public final MutableLiveData<weatherDataClass> dataToPrint = new MutableLiveData<>();

    public void setData(weatherDataClass s)
    {
        dataToPrint.setValue(s);
    }

    public MutableLiveData<weatherDataClass> getData()
    {
        return dataToPrint;
    }
}