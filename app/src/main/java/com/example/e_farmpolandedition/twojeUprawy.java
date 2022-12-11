package com.example.e_farmpolandedition;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.*;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

public class twojeUprawy extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
                                    GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener {

    private Marker marker;
    GoogleMap googleMaps;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.twoje_uprawy);

        firestore = FirebaseFirestore.getInstance();

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
        googleMaps = googleMap;
        googleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(51.9189046, 19.1343786), 5));

        marker = googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(51.9189046, 19.1343786))
                .draggable(true)
                .title("Nowe Pole"));

        googleMap.setOnMarkerDragListener(this);
        googleMap.setOnMapLongClickListener(this);
        googleMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        new MaterialAlertDialogBuilder(twojeUprawy.this)
                .setTitle("Dodaj uprawę")
                .setMessage("Czy chcesz dodać uprawę do listy upraw? Dane lokalizacji pobierane są automatycznie z mapy.")
                .setPositiveButton("Tak, dodaj", (dialogInterface, i) -> {
                    showDialogInsertData();
                })
                .setNegativeButton("Nie", null)
                .show();
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

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        marker.setPosition(latLng);
        googleMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
    }

    private void showDialogInsertData(){
        final Dialog dialog = new Dialog(twojeUprawy.this);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Ustaw datę!");

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();


        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.insert_data_gps);

        final EditText name = dialog.findViewById(R.id.name);
        final Spinner plantName = dialog.findViewById(R.id.plantName);
        final EditText surface = dialog.findViewById(R.id.surface);
        final Spinner surfaceMetric = dialog.findViewById(R.id.surfaceMetric);
        final TextView startDate = dialog.findViewById(R.id.selectedDate);
        final Button selectDate = dialog.findViewById(R.id.selectDate);
        final Button zatwierdzDane = dialog.findViewById(R.id.zatwierdzDane);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.uprawy, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        plantName.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(twojeUprawy.this,
                R.array.metrics, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        surfaceMetric.setAdapter(adapter1);

        plantName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0) Toast.makeText(twojeUprawy.this, "Dane zaktualizowane!",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        surfaceMetric.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        selectDate.setOnClickListener(view -> {
            materialDatePicker.show(getSupportFragmentManager(),"MATERIAL_DATE_PICKER");
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                startDate.setText(materialDatePicker.getHeaderText());
                Toast.makeText(getApplicationContext(), "Selected date is: " + materialDatePicker.getHeaderText(),
                        Toast.LENGTH_LONG).show();
            }
        });
        zatwierdzDane.setOnClickListener(view -> {
            Toast.makeText(twojeUprawy.this, "Dane zatwierdzone",
                    Toast.LENGTH_LONG).show();
            dialog.hide();
        });

        dialog.show();
    }
}


/*

        Map<String, Object> users = new HashMap<>();
        users.put("k","w");
        users.put("kw", "w");

        firestore.collection("users").add(users).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        })
 */