package com.example.e_farmpolandedition;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StartActivity extends AppCompatActivity {

    Context context;
    CardView startActivityLogin;
    Handler handler = new Handler();
    Runnable run;

    EditText userPasswordField, userLoginField;
    Button loginButton;

    boolean validationLoginData = false;
    boolean acceptReg = false;

    String userLogin, userPassword;
    String wojewodztwo, imie, nazwisko, email;
    Date data;

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        runAnimation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = getApplicationContext();

        findObject(); //to find filed on act
        setLister();  //set listener to login or register
        runAnimation(); //function only to run animation start card view with button
        handler.postDelayed(run, 5000); //run handler in 5s delay

    }

    private void findObject(){
        this.startActivityLogin = findViewById(R.id.startLoginAct);
        this.userLoginField = findViewById(R.id.userLoginField);
        this.userPasswordField = findViewById(R.id.userPasswordField);
        this.loginButton = findViewById(R.id.loginButton);
    }

    private void setLister(){
        findViewById(R.id.helpInfo).setOnClickListener(
                view -> Toast.makeText(StartActivity.this, "Jeżeli nie masz konta - nie przejmuj się!\n" +
                "Dzięki automatycznym funkcjom, aplikacja utworzy je sama!", Toast.LENGTH_LONG).show());

        this.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = userLoginField.getText().toString();
                String password = userPasswordField.getText().toString();
                if(login.isEmpty() || password.isEmpty()){
                    Snackbar.make(
                            view,
                            "Podano błędne dane",
                            Snackbar.LENGTH_LONG
                    ).show();
                }
                else {
                    loginAction(login,password,view);
                    // Get your layout set up, this is just an example
                    ConstraintLayout mainLayout = (ConstraintLayout) findViewById(R.id.mainConstraintLay);

                    // Then just use the following:
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
                }
            }
        });

        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegisterCard(view);
            }
        });

        userPasswordField.addTextChangedListener(new TextWatcher() {
            Pattern pattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$");
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String tmpPass = charSequence.toString();
                Matcher matcher = pattern.matcher(tmpPass);
                if (!matcher.matches()) {
                    userPasswordField.setError("Błędne znaki!",null);
                }
                else {
                    (userPasswordField).setError(null);
                    validationLoginData = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        userLoginField.addTextChangedListener(new TextWatcher() {
            Pattern pattern = Pattern.compile("^(?=.{8,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$");
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String tmpLog = charSequence.toString();
                Matcher matcher = pattern.matcher(tmpLog);
                if (!matcher.matches()) {
                    userLoginField.setError("Błędne znaki!",null);
                }
                else {
                    (userLoginField).setError(null);
                    validationLoginData = true;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void runAnimation(){
        startActivityLogin.setOnClickListener(view -> {
            turnVisable();
        });
            run = new Runnable() {
                @Override
                public void run() {
                        Animation animation = AnimationUtils.loadAnimation(StartActivity.this, R.anim.fadein);
                        startActivityLogin.startAnimation(animation);
                        handler.postDelayed(this, 5000);
                }
            };
    }

    private void turnVisable(){
        startActivityLogin.setOnClickListener(null);
        Toast.makeText(StartActivity.this,"Podaj dane do logowania", Toast.LENGTH_LONG).show();
        findViewById(R.id.loginArea).setVisibility(View.VISIBLE);
        findViewById(R.id.startLoginAct).setVisibility(View.GONE);
        findViewById(R.id.consLay).setVisibility(View.GONE);
        findViewById(R.id.infoText).setVisibility(View.GONE);
        findViewById(R.id.startButton).setVisibility(View.GONE);
        handler.removeCallbacksAndMessages(null);
        handler.removeMessages(0);
    }

    private void loginAction(String login, String password, View view){
        this.userPassword=password;
        this.userLogin=login;
        boolean loginExist = true; //check in DB if login exist - return true, false - notify and run regis
        if(validationLoginData && loginExist){
            Snackbar.make(view, "Użytkownik nie istnieje, utworzyć?", Snackbar.LENGTH_LONG)
                    .setAction("Utwórz!", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //show fields to register user
                            showRegisterCard(view);
                        }
                    })
                    .addCallback(new Snackbar.Callback(){
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);
                            if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                                Toast.makeText(getApplicationContext(),
                                        "Nie utworzono konta! Utwórz je.",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .show();
        }else Toast.makeText(getApplicationContext(), "Mamy problem z logowaniem!", Toast.LENGTH_LONG).show();
    }

    private void showRegisterCard(View view){
        findViewById(R.id.loginArea).setVisibility(View.GONE);
        findViewById(R.id.registerArea).setVisibility(View.VISIBLE);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.wojewodztwa, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        CheckBox actBox = findViewById(R.id.actRegCheckBox);
        actBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b) acceptReg = true;
            else acceptReg = false;
        });

        String wojewodztwo = spinner.getSelectedItem().toString();

        TextInputLayout userLoginLayout = findViewById(R.id.usernameLayout);
        TextInputLayout nameLayout = findViewById(R.id.nameLayout);
        TextInputLayout surnameLayout = findViewById(R.id.surnameLayout);

        this.imie = nameLayout.getEditText().getText().toString();
        this.nazwisko = surnameLayout.getEditText().toString();
        this.userLogin = userLoginLayout.getEditText().toString();



    }


}