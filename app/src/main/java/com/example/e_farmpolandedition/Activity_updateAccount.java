package com.example.e_farmpolandedition;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Activity_updateAccount extends AppCompatActivity {

    Context context;

    TextInputLayout name,surname,email,dateOfB, password;
    Spinner province;
    CalendarView kalendarz;
    FloatingActionButton savechanges;
    ArrayAdapter<CharSequence> adapter;

    String login;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.user_account_update);
        context = getApplicationContext();

        Intent intent = getIntent();
        login = intent.getStringExtra("login");

        firestore = FirebaseFirestore.getInstance();

        findObject();
        setListener();

        adapter = ArrayAdapter.createFromResource(this,
                R.array.wojewodztwa, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        province.setAdapter(adapter);

        loadDataToField();
    }

    private void findObject(){
        name = findViewById(R.id.nazwaZabiegu);
        surname = findViewById(R.id.editSurname);
        email = findViewById(R.id.editEmail);
        dateOfB = findViewById(R.id.editDate);
        province = findViewById(R.id.editSpinnerWojewodztwo);
        password = findViewById(R.id.editPassword);
        kalendarz = findViewById(R.id.kalendarz);
        savechanges = findViewById(R.id.saveChanges);
    }

    private void setListener(){
        savechanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savechangesInDB();
            }
        });

    }

    private void loadDataToField(){
        CollectionReference userDataPath = firestore.collection("user_account");
        userDataPath.document(login).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()) {
                                name.getEditText().setText((CharSequence) document.get("name"));
                                surname.getEditText().setText((CharSequence) document.get("surname"));
                                email.getEditText().setText((CharSequence) document.get("email"));
                                dateOfB.getEditText().setText((CharSequence) document.get("date"));
                                password.getEditText().setText((CharSequence) document.get("password"));
                                province.setSelection(adapter.getPosition((CharSequence) document.get("wojewodztwo")));

                                SimpleDateFormat formatter = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
                                Date date = null;
                                try {
                                    date = formatter.parse(document.get("date").toString());
                                    long dat = date.getTime();
                                    kalendarz.setDate(dat);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });

    }

    private void savechangesInDB(){
        userAccount changedAc = new userAccount(
                login,
                name.getEditText().getText().toString(),
                surname.getEditText().getText().toString(),
                email.getEditText().getText().toString(),
                dateOfB.getEditText().getText().toString(),
                province.getSelectedItem().toString(),
                password.getEditText().getText().toString()
        );

        Map<userAccount, Object> newObj = new HashMap<>();

        if(changedAc.validationData()) {
            CollectionReference dbUsers = firestore.collection("user_account");
            dbUsers.document(login).set(changedAc).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(Activity_updateAccount.this, "Poprawna modyfikacja!",
                            Toast.LENGTH_LONG).show();
                }
            });

        }
        else Toast.makeText(Activity_updateAccount.this, "Bład modyfikacji!",
                Toast.LENGTH_LONG).show();
    }


}
