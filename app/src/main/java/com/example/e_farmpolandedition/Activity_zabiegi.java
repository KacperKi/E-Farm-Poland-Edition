package com.example.e_farmpolandedition;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_zabiegi extends AppCompatActivity {

    FirebaseFirestore firestore;
    String login;

    ArrayList<zabieg> listaZabiegow;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_zabiegi);

        Intent intent = getIntent();
        login = intent.getStringExtra("login");

        firestore = FirebaseFirestore.getInstance();
        listaZabiegow = new ArrayList<>();



    }




    void addZabieg(){
        String PATH = "uprawy/" + login + "/zabieg";
        CollectionReference ref = firestore.collection(PATH);

        Map<String, Object> zabiegE = new HashMap<>();
        zabiegE.put("nazwaZabiegu", );
        zabiegE.put("dataRozpoczecia", );
        zabiegE.put("okresKarencji", );
        zabiegE.put("dawka", );
        zabiegE.put("rodzajSrodka", );
        zabiegE.put("koszt", );

        ref.document(===name)
        .set(zabiegE);

    }

    void getZabiegi(){
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
                        }
                    }
                });
    }



}
