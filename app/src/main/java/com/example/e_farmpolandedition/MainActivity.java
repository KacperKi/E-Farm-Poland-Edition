package com.example.e_farmpolandedition;


import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private String previousAct;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(previousAct.equals("StartActivity")) {
            Intent myIntent = new Intent(MainActivity.this, StartActivity.class);
            MainActivity.this.startActivity(myIntent);
            finish();
        }

    }

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.main_user_view);
        this.previousAct= getIntent().getExtras().getString("previousActivity");



    }


}
