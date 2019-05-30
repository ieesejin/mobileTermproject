package com.example.blackscreensaver;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    Handler handler = new Handler();
    public SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("Pref", MODE_PRIVATE);
        checkFirstRun();

    }

    public void checkFirstRun(){
        boolean isFirstRun = prefs.getBoolean("isFirstRun",true);
        if(isFirstRun) {
            Intent intent = new Intent(this, MainFragment.class);
            startActivity(intent);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent1);
                    finish();
                }
            }, 4000);
            finish();

            prefs.edit().putBoolean("isFirstRun",false).apply();
            //처음만 true 그다음부터는 false 바꾸는 동작
        }
        else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent1);
                    finish();
                }
            }, 4000);
            finish();
        }
    }
}
