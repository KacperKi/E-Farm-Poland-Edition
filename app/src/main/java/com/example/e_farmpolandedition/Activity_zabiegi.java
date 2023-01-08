package com.example.e_farmpolandedition;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_zabiegi extends AppCompatActivity {

    FirebaseFirestore firestore;
    String login;


    TextInputLayout nazwaZabieguForm, kwotaZabieguForm;
    Spinner dawka,okres_karencji,uprawyUsera,rodzajSrodka;
    Button selectDate;
    FloatingActionButton addZabieg;

    ArrayAdapter<CharSequence> adapter,adapter1,adapter3;
    ArrayAdapter<String> adapter2;

    private List<String> uprawyUserList;
    private ArrayList<zabieg> listaZabiegow;

    private RecyclerView recycler_zabiegi;
    private zabiegiRecyclerList adapterZabiegi;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_zabiegi);

        Intent intent = getIntent();
        login = intent.getStringExtra("login");

        firestore = FirebaseFirestore.getInstance();
        listaZabiegow = new ArrayList<>();
        uprawyUserList = new ArrayList<>();

        findObject();

        getUprawy();
        getZabiegi();
        setListener();
        setSpinner();

        setAdapterZabiegi();
    }

    private void setAdapterZabiegi(){
        adapterZabiegi = new zabiegiRecyclerList(listaZabiegow);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Activity_zabiegi.this);
        recycler_zabiegi.setLayoutManager(layoutManager);
        recycler_zabiegi.setItemAnimator(new DefaultItemAnimator());
        recycler_zabiegi.setAdapter(adapterZabiegi);

        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recycler_zabiegi);
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
            Snackbar.make(findViewById(R.id.listaCardView), "Delete item?", Snackbar.LENGTH_LONG)
                    .setAction("Yes!", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String PATH = "uprawy/" + login + "/zabieg";
                            String nameOfDoc = listaZabiegow.get(viewHolder.getAdapterPosition()).getNazwaZabiegu();
                            firestore.collection(PATH).document(nameOfDoc)
                                    .delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            listaZabiegow.remove(viewHolder.getAdapterPosition());
                                            adapterZabiegi.notifyDataSetChanged();
                                            Toast.makeText(getApplicationContext(),
                                                    "Usunięto poprawnie!",
                                                    Toast.LENGTH_LONG).show();
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

    void addZabieg(){
        String t1,t2,t3,t4,t5,t6;
        t1 = nazwaZabieguForm.getEditText().getText().toString();
        t2 = selectDate.getText().toString();
        t3 = okres_karencji.getSelectedItem().toString();
        t4 = dawka.getSelectedItem().toString();
        t5 = rodzajSrodka.getSelectedItem().toString();
        t6 = kwotaZabieguForm.getEditText().getText().toString();

        if(!t1.isEmpty() && !t6.isEmpty()) {

            String PATH = "uprawy/" + login + "/zabieg";
            CollectionReference ref = firestore.collection(PATH);

            Map<String, Object> zabiegE = new HashMap<>();
            zabiegE.put("nazwaZabiegu", t1);
            zabiegE.put("dataRozpoczecia", t2);
            zabiegE.put("okresKarencji", t3);
            zabiegE.put("dawka", t4);
            zabiegE.put("rodzajSrodka", t5);
            zabiegE.put("uprawaUsera", uprawyUsera.getSelectedItem().toString());
            zabiegE.put("koszt", t6);

            ref.document(nazwaZabieguForm.getEditText().getText().toString())
                    .set(zabiegE).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Poprawnie dodano!", Toast.LENGTH_LONG).show();
                            getZabiegi();
                        }
                    });
        }else Toast.makeText(getApplicationContext(), "Dane nie spełniają minimalnych wymagań!", Toast.LENGTH_SHORT).show();
    }

    void getZabiegi(){
        listaZabiegow.clear();
        String PATH = "uprawy/" + login + "/zabieg";
        CollectionReference ref = firestore.collection(PATH);
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                zabieg t = document.toObject(zabieg.class);
                                listaZabiegow.add(t);
                            }
                            if(adapterZabiegi!=null) {
                                adapterZabiegi.updateArrayList(listaZabiegow);
                                adapterZabiegi.notifyDataSetChanged();
                            }
                        }
                    }
                });

    }

    void getUprawy(){
        uprawyUserList.clear();
        String PATH_Uprawy = "uprawy/" + login + "/uprawa";

        firestore.collection(PATH_Uprawy)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
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
                                uprawyUserList.add(t.getName());
                            }
                        }
                    }
                });

    }

    void findObject(){
         nazwaZabieguForm = findViewById(R.id.nazwaZabieguForm);
         kwotaZabieguForm = findViewById(R.id.kwotaZabieguForm);
         dawka = findViewById(R.id.dawka);
         okres_karencji = findViewById(R.id.okres_karencji);
         uprawyUsera = findViewById(R.id.uprawyUsera);
         rodzajSrodka = findViewById(R.id.rodzajSrodka);
         selectDate = findViewById(R.id.selectDateForm);
         addZabieg = findViewById(R.id.dodajZabiegButton);
         recycler_zabiegi = findViewById(R.id.recycler_zabiegi);
    }

    void setListener(){
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Ustaw datę!");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                selectDate.setText(materialDatePicker.getHeaderText());
            }
        });

        selectDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                        materialDatePicker.show(getSupportFragmentManager(),"MATERIAL_DATE_PICKER");
            }
        });
        addZabieg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addZabieg();
            }
        });

    }

    void setSpinner(){
        //    Spinner dawka,okres_karencji,uprawyUsera,rodzajSrodka;
        adapter = ArrayAdapter.createFromResource(this,
                R.array.dawka, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dawka.setAdapter(adapter);

        adapter1 = ArrayAdapter.createFromResource(this,
                R.array.okres_karencji, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        okres_karencji.setAdapter(adapter1);

        adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, uprawyUserList);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        uprawyUsera.setAdapter(adapter2);

        adapter3 = ArrayAdapter.createFromResource(this,
                R.array.opryski_nawozy, android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rodzajSrodka.setAdapter(adapter3);

    }

}
