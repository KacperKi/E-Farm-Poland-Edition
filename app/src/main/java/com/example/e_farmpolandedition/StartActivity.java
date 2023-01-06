package com.example.e_farmpolandedition;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

public class StartActivity extends AppCompatActivity {

    Context context;
    CardView startActivityLogin;

    Handler handler = new Handler();
    Handler loadData = new Handler();

    Runnable run;
    Spinner spinner;

    ProgressDialog progressDialog;

    EditText userPasswordField, userLoginField;
    Button loginButton;

    boolean validationLoginData = false;
    boolean acceptReg = false;
    boolean loginExistInDB = false;
    boolean passwordCorr = false;
    boolean funStat = false;

    String userLogin, userPassword;
    String wojewodztwo, imie, nazwisko, email;
    Date data;

    FirebaseFirestore firestore;
    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = getApplicationContext();

        firestore = FirebaseFirestore.getInstance();

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
        this.spinner = findViewById(R.id.spinner);
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
                }
            }
        });

        findViewById(R.id.registerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    showRegisterCard(view);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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
        if(validationLoginData){
            checkLoginPasswordInDB();
//            Snackbar.make(view, "Użytkownik nie istnieje, utworzyć?", Snackbar.LENGTH_LONG)
//                    .setAction("Utwórz!", new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            //show fields to register user
//                            try {
//                                showRegisterCard(view);
//                            } catch (ParseException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    })
//                    .addCallback(new Snackbar.Callback(){
//                        @Override
//                        public void onDismissed(Snackbar transientBottomBar, int event) {
//                            super.onDismissed(transientBottomBar, event);
//                            if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
//                                Toast.makeText(getApplicationContext(),
//                                        "Nie utworzono konta! Utwórz je.",
//                                        Toast.LENGTH_LONG).show();
//                            }
//                        }
//                    })
//                    .show();
        }else Toast.makeText(getApplicationContext(), "Mamy problem z logowaniem!", Toast.LENGTH_LONG).show();


    }

    private void showRegisterCard(View view) throws ParseException {
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();
        materialDateBuilder.setTitleText("Ustaw datę!");

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        findViewById(R.id.loginArea).setVisibility(View.GONE);
        findViewById(R.id.registerArea).setVisibility(View.VISIBLE);

        ImageButton buttonDay = findViewById(R.id.selectDateBirthday);
        TextInputLayout dateLayout = findViewById(R.id.dateLayout);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.wojewodztwa, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        buttonDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(),"MATERIAL_DATE_PICKER");
            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                dateLayout.getEditText().setText(materialDatePicker.getHeaderText());
                Toast.makeText(getApplicationContext(), "Selected date is: " + materialDatePicker.getHeaderText(),
                        Toast.LENGTH_LONG).show();
            }
        });

        CheckBox actBox = findViewById(R.id.actRegCheckBox);
        actBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if(b) acceptReg = true;
            else acceptReg = false;
        });

        //check checkbox
        Button registerButton = (Button) findViewById(R.id.registerButtonArea);
        registerButton.setOnClickListener(view1 -> {
            userAccount userData = collectData();
            if (userData.validationData()) {
                if(acceptReg) {
                    checkUserinDb();
                    if (!loginExistInDB) {
                        CollectionReference dbUsers = firestore.collection("user_account");
                        dbUsers.document(userData.getLogin()).set(userData);

                        new AlertDialog.Builder(StartActivity.this)
                                .setTitle("Konto utworzone!")
                                .setMessage("Chcesz się zalogować?")
                                .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        findViewById(R.id.loginArea).setVisibility(View.VISIBLE);
                                        findViewById(R.id.registerArea).setVisibility(View.GONE);                                    }
                                })
                                .setNegativeButton("Nie", null)
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();


                    } else
                        Toast.makeText(StartActivity.this, "Login niedostępny!", Toast.LENGTH_LONG).show();
                }else
                    Toast.makeText(StartActivity.this, "Niezakceptowany regulamin!", Toast.LENGTH_LONG).show();
            }
                Toast.makeText(StartActivity.this, "Niepoprawne dane!", Toast.LENGTH_LONG).show();

        });
    }

    public userAccount collectData(){
        TextInputLayout userLoginLayout = findViewById(R.id.usernameLayout);
        TextInputLayout nameLayout = findViewById(R.id.nameLayout);
        TextInputLayout surnameLayout = findViewById(R.id.surnameLayout);
        TextInputLayout emailLayout = findViewById(R.id.emailLayout);
        TextInputLayout dateLayout = findViewById(R.id.dateLayout);
        TextInputLayout passwordLayout = findViewById(R.id.userInsertPasswordLayout);

        this.imie = Objects.requireNonNull(nameLayout.getEditText()).getText().toString();
        this.nazwisko = Objects.requireNonNull(surnameLayout.getEditText()).getText().toString();
        this.userLogin = Objects.requireNonNull(userLoginLayout.getEditText()).getText().toString();
        this.email = Objects.requireNonNull(emailLayout.getEditText()).getText().toString();
        this.userPassword = Objects.requireNonNull(passwordLayout.getEditText()).getText().toString();
        this.wojewodztwo = spinner.getSelectedItem().toString();

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        try {
            this.data = dateFormat.parse(dateLayout.getEditText().getText().toString());
        }
        catch (ParseException e){
            Log.e("Parse exception - Start Activity", e.toString());
        }

        userAccount t = new userAccount(
                this.userLogin,
                this.imie,
                this.nazwisko,
                this.email,
                dateLayout.getEditText().getText().toString(),
                this.wojewodztwo,
                this.userPassword
        );

        return t;
    }

    public void checkUserinDb(){

        firestore.collection("user_account")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        TextInputLayout userLoginLayout = findViewById(R.id.usernameLayout);

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document.getId().equals(userLoginLayout.getEditText().getText().toString())) {
                                loginExistInDB = true;
                            }
                            else {
                                loginExistInDB = false;
                            }
                        }
                    }
                });

    }

    public void checkUserinDbLogin(){
//        loadData.post(new Runnable() {
//            @Override
//            public void run() {
//                progressDialog = new ProgressDialog(StartActivity.this);
//                progressDialog.setMessage("Pobieram Dane");
//                progressDialog.setCancelable(false);
//                progressDialog.show();
//            }
//        });

        firestore.collection("user_account")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        TextInputLayout usernameLoginLayout = findViewById(R.id.userLoginLayout);
                        Log.e("Koniec weryfikacji loginu!", "Weryfikacja CHECK USER IN DB LOGIN");
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document.getId().equals(usernameLoginLayout.getEditText().getText().toString())) {
                                loginExistInDB = true;
                                verifyPassword();
                            }
                            else {
                                loginExistInDB = false;
                            }
                        }
                        setFunStat(true);
                    }
                });

//        System.out.println("Fun set error after checkuser");
//
//        loadData.post(new Runnable() {
//            @Override
//            public void run() {
//                    if(progressDialog.isShowing()) progressDialog.dismiss();
//            }
//        });
    }

    public void verifyPassword(){
        TextInputLayout userLoginLayout = findViewById(R.id.userLoginLayout);
        TextInputLayout userPasswordLayout = findViewById(R.id.userPasswordLayout);

        firestore.collection("user_account")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if(document.getId().equals(userLoginLayout.getEditText().getText().toString())) {
                                userAccount t = document.toObject(userAccount.class);
                                if(t.getPassword().equals(userPasswordLayout.getEditText().getText().toString())) passwordCorr = true;
                                else passwordCorr = false;
                                break;
                            }
                        }
                    }
                });
    }

    public void setFunStat(boolean i){
        this.funStat = i;
    }

    public void checkLoginPasswordInDB() {
        TextInputLayout userLoginLayout = findViewById(R.id.userLoginLayout);
        for(int w=0; w<3; w++){checkUserinDbLogin();}
//        if(passwordCorr) {
//            new AlertDialog.Builder(StartActivity.this)
//                    .setTitle("Logowanie")
//                    .setMessage("Logujesz się jako " + userLoginLayout.getEditText().getText().toString())
//                    .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            checkLoginPasswordInDB();
//                        }
//                    })
//                    .setNegativeButton("Nie", null)
//                    .setIcon(R.drawable.uprawy_icon)
//                    .show();
//        }


            if (loginExistInDB) {
                if (passwordCorr) {
                    Intent myIntent = new Intent(StartActivity.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("previousActivity", "StartActivity");
                    myIntent.putExtras(bundle);
                    StartActivity.this.startActivity(myIntent);
                } else
                    Toast.makeText(getApplicationContext(), "Hasło nieprawidłowe!", Toast.LENGTH_LONG).show();
            } else
                Toast.makeText(getApplicationContext(), "Login nieprawidłowy!", Toast.LENGTH_LONG).show();
    }
}






