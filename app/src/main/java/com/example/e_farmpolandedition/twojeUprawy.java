package com.example.e_farmpolandedition;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class twojeUprawy extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
                                    GoogleMap.OnMarkerDragListener{

    private Marker marker;
    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.twoje_uprawy);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.mapa_google, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
//focus on poland
        googleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(51.9189046, 19.1343786), 5));

        marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(51.9189046, 19.1343786))
                .draggable(true)
                .title("Ustaw na Twoim polu!"));

        //googleMap.setOnMarkerClickListener(this);
        googleMap.setOnMarkerDragListener(this);


        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                LatLng position = marker.getPosition();
                Toast.makeText(
                        twojeUprawy.this,
                        "Lat " + position.latitude + " " + "Long " + position.longitude,
                        Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    @Override
    public void onMarkerDrag(@NonNull Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(@NonNull Marker marker) {
        Toast.makeText(getApplicationContext(),
                "Dane: " + marker.getPosition().latitude
                + " - dane2: " + marker.getPosition().longitude,
                Toast.LENGTH_LONG
                ).show();
    }

    @Override
    public void onMarkerDragStart(@NonNull Marker marker) {

    }
}
