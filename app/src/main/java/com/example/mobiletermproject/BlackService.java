package com.example.mobiletermproject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class BlackService extends Service {

    WindowManager windowManager;
    View overLayView;
    private SharedPreferences transparency;
    private SharedPreferences tapPref;
    private SharedPreferences volumePref;
    private Intent intent;
    private int tapStatus;
    private int volumeStatus;
    private int tranStatus;

    public BlackService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        intent = new Intent(getApplicationContext(), BlackService.class);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // using for WindowManager different Flag for version
        int LAYOUT_FLAG;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
                PixelFormat.TRANSLUCENT
        );
        overLayView = inflater.inflate(R.layout.blackscreen, null);

        overLayView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        final TextView textView = overLayView.findViewById(R.id.textView);

        final Button bt = overLayView.findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                bt.setImageResource(R.mipmap.ic_launcher_round);
//                textView.setText("on click!!");
                stopService(intent);
            }
        });

        windowManager.addView(overLayView, params);

        transparency = getSharedPreferences("transparency", Context.MODE_PRIVATE);
        tranStatus = transparency.getInt("transparency", 255);
        overLayView.getBackground().setAlpha(tranStatus);

        tapPref = getSharedPreferences("tapSwitch", Context.MODE_PRIVATE);
        tapStatus = tapPref.getInt("tapSwitch", 0);

        volumePref = getSharedPreferences("volumeSwitch", Context.MODE_PRIVATE);
        volumeStatus = volumePref.getInt("volumeSwitch", 1);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (windowManager != null) {
            if (overLayView != null) {
                windowManager.removeView(overLayView);
                overLayView = null;
            }
            windowManager = null;
        }
    }
}