package com.example.mobiletermproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 1002;
    private Intent intent;
    private Switch overlaySwitch;
    private Switch tapSwitch;
    private Switch insideSwitch;
    private Switch dragSwitch;
    private SeekBar seekTrans;
    private SharedPreferences option;
    private SharedPreferences.Editor editOption;
    private boolean onOffStatus;
    private boolean tapStatus;
    private int transStatus;
    private boolean insideStatus;
    private boolean dragStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        option = getSharedPreferences("option", Context.MODE_PRIVATE);
        editOption = option.edit();
        onOffStatus = option.getBoolean("onOff", false);
        transStatus = option.getInt("transparency", 200);
        tapStatus = option.getBoolean("tap", true);
        insideStatus = option.getBoolean("inside", false);
        dragStatus = option.getBoolean("drag", false);

        if(onOffStatus) {
            checkPermission();
        }

        overlaySwitch = findViewById(R.id.switch1);
        overlaySwitch.setChecked(onOffStatus);
        overlaySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                intent = new Intent(MainActivity.this, MyService.class);
                if(isChecked){
                    checkPermission();
                } else {
                    stopService(intent);
                }
                editOption.putBoolean("onOff", isChecked);
                editOption.apply();
            }
        });

        tapSwitch = findViewById(R.id.switch2);
        tapSwitch.setChecked(tapStatus);
        tapSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editOption.putBoolean("tap", isChecked);
                editOption.apply();
                tapStatus = option.getBoolean("tap", true);
                dragStatus = option.getBoolean("drag", false);
                if(!tapStatus && !dragStatus) {
                    dragSwitch.setChecked(true);
                }
            }
        });

        dragSwitch = findViewById(R.id.switch3);
        dragSwitch.setChecked(dragStatus);
        dragSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editOption.putBoolean("drag", isChecked);
                editOption.apply();
                if(isChecked) {
                    insideSwitch.setChecked(true);
                }
                tapStatus = option.getBoolean("tap", true);
                dragStatus = option.getBoolean("drag", false);
                if(!tapStatus && !dragStatus) {
                    tapSwitch.setChecked(true);
                }
            }
        });

        insideSwitch = findViewById(R.id.switch4);
        insideSwitch.setChecked(insideStatus);
        insideSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editOption.putBoolean("inside", isChecked);
                editOption.apply();
            }
        });

        seekTrans = findViewById(R.id.seekTrans);
        seekTrans.setProgress(transStatus);
        seekTrans.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                editOption.putInt("transparency", seekBar.getProgress());
                editOption.apply();
            }
        });
    }

    public void checkPermission() {
        intent = new Intent(MainActivity.this, MyService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   // 마시멜로우 이상일 경우
            if (!Settings.canDrawOverlays(this)) {              // 체크
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
            } else {
                startService(intent);
            }
        } else {
            startService(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE){
            if (Settings.canDrawOverlays(this)) {              // 체크
                intent = new Intent(MainActivity.this, MyService.class);
                startService(intent);
            } else{
                checkPermission();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
