package com.example.e_farmpolandedition;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class twojeUprawy extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
                                    GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener {

    private Marker marker;
    GoogleMap googleMaps;
    FirebaseFirestore firestore;

    FloatingActionButton showAllMarker, clearAllMarker;

    private String login;

    private ArrayList<uprawa> lista_upraw_usera = new ArrayList<>();
    private ArrayList<String> lista_zabiegow_usera = new ArrayList<>();

    private RecyclerView recyclerView;
    private uprawyRecyclerList adapterUpraw;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.twoje_uprawy);

        setListenerFloatingButton();

        Intent intent = getIntent();
        login = intent.getStringExtra("login");

        recyclerView = findViewById(R.id.recyclerview_upraw);

        firestore = FirebaseFirestore.getInstance();

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.mapa_google, mapFragment)
                .commit();
        mapFragment.getMapAsync(this);

        getUprawyData();
        setAdapterUpraw();

    }

    private void setAdapterUpraw(){
        adapterUpraw = new uprawyRecyclerList(lista_upraw_usera);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(twojeUprawy.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterUpraw);

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
    }

    ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Snackbar.make(findViewById(R.id.lista_upraw_layout), "Delete item?", Snackbar.LENGTH_LONG)
                    .setAction("Yes!", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String PATH = "uprawy/" + login + "/uprawa";
                            String nameOfDoc = lista_upraw_usera.get(viewHolder.getAdapterPosition()).name;
                            firestore.collection(PATH).document(nameOfDoc)
                                            .delete()
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            lista_upraw_usera.remove(viewHolder.getAdapterPosition());
                                                            adapterUpraw.notifyDataSetChanged();
                                                            Toast.makeText(getApplicationContext(),
                                                                    "Usunięto poprawnie!",
                                                                    Toast.LENGTH_LONG).show();
                                                            updateGoogleMapsMarkers();
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getApplicationContext(),
                                                                    "Usunięcie nie możliwe!",
                                                                    Toast.LENGTH_LONG).show();
                                                        }
                                                    });

                        }
                    })
                    .show();
        }
    };

    private void getUprawyData(){
        lista_upraw_usera.clear();

        String PATH_Uprawy = "uprawy/" + login + "/uprawa";
        String PATH_Zabiegi = "uprawy/" + login + "/zabieg";

        firestore.collection(PATH_Uprawy)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            Log.e("UPRAWA Dane z API Firebase UPRAWA!",
                                    "Czas : " +
                                        String.valueOf(Calendar.getInstance().getTime()) +
                                        " rozmiar tablicy upraw lokalny: " +
                                        String.valueOf(lista_upraw_usera.size())
                                    );

                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                uprawa t = document.toObject(uprawa.class);
                                uprawa t = new uprawa(
                                        document.get("latitude").toString(),
                                        document.get("longtitude").toString(),
                                        document.get("name").toString(),
                                        document.get("plantName").toString(),
                                        document.get("startDate").toString(),
                                        document.get("surface").toString(),
                                        document.get("surfaceMetric").toString(),
                                        document.get("description").toString()
                                        );
                                lista_upraw_usera.add(t);
                            }

                            adapterUpraw.updateArrayList(lista_upraw_usera);
                            adapterUpraw.notifyDataSetChanged();
                        }
                    }
                });

        firestore.collection(PATH_Zabiegi)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                    lista_zabiegow_usera.add("");
                            }
                        }
                    }
                });

    }

    public void setListenerFloatingButton(){
        showAllMarker = findViewById(R.id.showAllMarkersOnMap);
        clearAllMarker = findViewById(R.id.clearAllPointOnMaps);

        showAllMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMaps.clear();
                ArrayList<PointOnMaps> listOfPoints = new ArrayList<>();
                for(uprawa t: lista_upraw_usera){
                    listOfPoints.add(new PointOnMaps(
                            new LatLng(
                                    Float.parseFloat(t.latitude),
                                    Float.parseFloat(t.longtitude)
                            ),
                            t.name));
                }
                for(PointOnMaps t: listOfPoints){
                    googleMaps.addMarker(new MarkerOptions().position(t.position).title(t.nameOfMarker));
                }
            }
        });

        clearAllMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                googleMaps.clear();
                Toast.makeText(getApplicationContext(), "Wyczyszczono mapę!", Toast.LENGTH_LONG).show();
            }
        });

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
                    showDialogInsertData(marker);
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
        googleMaps.clear();
        marker.setPosition(latLng);
        googleMaps.addMarker(new MarkerOptions().position(latLng).title("Nowe miejsce"));
        googleMaps.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
    }

    private void showDialogInsertData(Marker marker){
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
        final TextView startDate = dialog.findViewById(R.id.selectedDates);
        final Button selectDate = dialog.findViewById(R.id.selectDate);
        final TextView selectedDates = dialog.findViewById(R.id.selectedDates);
        final EditText description = dialog.findViewById(R.id.description);
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
            }
        });

        zatwierdzDane.setOnClickListener(view -> {
            String PATH = "uprawy/" + login + "/uprawa";


            Map<String, Object> uprawa = new HashMap<>();

            uprawa.put("latitude", String.valueOf(marker.getPosition().latitude));
            uprawa.put("longtitude", String.valueOf(marker.getPosition().longitude));
            uprawa.put("name", name.getText().toString());
            uprawa.put("plantName", plantName.getSelectedItem().toString());
            uprawa.put("surface", surface.getText().toString());
            uprawa.put("surfaceMetric", surfaceMetric.getSelectedItem().toString());
            uprawa.put("startDate", selectedDates.getText().toString());
            uprawa.put("description", description.getText().toString());

            firestore.collection(PATH).document(name.getText().toString())
                .set(uprawa)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Snackbar.make(view, "Poprawnie dodano dane!", Snackbar.LENGTH_LONG)
                                .setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialog.hide();
                                    }
                                })
                                .addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                                    @Override
                                    public void onDismissed(Snackbar transientBottomBar, int event) {
                                        super.onDismissed(transientBottomBar, event);
                                        dialog.hide();
                                    }
                                })
                                .show();
                    }
                });

            getUprawyData();
        });

        dialog.show();
    }

    private void updateGoogleMapsMarkers(){
        googleMaps.clear();
        ArrayList<PointOnMaps> listOfPoints = new ArrayList<>();
        for(uprawa t: lista_upraw_usera){
            listOfPoints.add(new PointOnMaps(
                    new LatLng(
                            Float.parseFloat(t.latitude),
                            Float.parseFloat(t.longtitude)
                    ),
                    t.name));
        }
        for(PointOnMaps t: listOfPoints){
            googleMaps.addMarker(new MarkerOptions().position(t.position).title(t.nameOfMarker));
        }
        googleMaps.moveCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(51.9189046, 19.1343786), 5));    }

    public class PointOnMaps {
        private LatLng position;
        private String nameOfMarker;

        public PointOnMaps(LatLng position, String nameOfMarker) {
            this.position = position;
            this.nameOfMarker = nameOfMarker;
        }

        public LatLng getPosition() {
            return position;
        }

        public void setPosition(LatLng position) {
            this.position = position;
        }

        public String getNameOfMarker() {
            return nameOfMarker;
        }

        public void setNameOfMarker(String nameOfMarker) {
            this.nameOfMarker = nameOfMarker;
        }
    }
}
