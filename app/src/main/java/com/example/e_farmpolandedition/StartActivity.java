package com.example.e_farmpolandedition;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.snackbar.Snackbar;

public class StartActivity extends AppCompatActivity {

    CardView startActivityLogin;
    Handler handler = new Handler();
    Runnable run;

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

        startActivityLogin = findViewById(R.id.startLoginAct);
        findViewById(R.id.helpInfo).setOnClickListener(
                view -> Toast.makeText(StartActivity.this, "Jeżeli nie masz konta - nie przejmuj się!\n" +
                "Dzięki automatycznym funkcjom, aplikacja utworzy je sama!", Toast.LENGTH_LONG).show());
        runAnimation();
        handler.postDelayed(run, 5000);

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
}