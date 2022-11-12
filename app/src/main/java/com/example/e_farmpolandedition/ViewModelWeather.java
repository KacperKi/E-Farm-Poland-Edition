package com.example.e_farmpolandedition;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class ViewModelWeather extends ViewModel {

    MutableLiveData<ArrayList<weatherDataClass>> dataToPrint = new MutableLiveData<>();

    public void setData(ArrayList<weatherDataClass> s)
    {
        dataToPrint.setValue(s);
    }

    public MutableLiveData<ArrayList<weatherDataClass>> getData()
    {
        return dataToPrint;
    }
}