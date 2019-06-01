package com.example.mobiletermproject;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class BlackService extends Service {

    WindowManager windowManager;
    View overLayView;
    private SharedPreferences option;
    private Intent intent;
    private Boolean tapStatus;
    private int volumeStatus;
    private int tranStatus;
    private GestureDetector gestureDetector;
//    SettingsContentObserver mSettingsContentObserver;

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

        windowManager.addView(overLayView, params);

        option = getSharedPreferences("option", Context.MODE_PRIVATE);
        Button btn = overLayView.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(intent);
            }
        });
        tranStatus = option.getInt("transparency", 200);
        overLayView.getBackground().setAlpha(tranStatus);

        tapStatus = option.getBoolean("tap", false);
        if(tapStatus == true){
            CustomGestureDetector customGestureDetector = new CustomGestureDetector();
            gestureDetector = new GestureDetector(this, customGestureDetector);
            gestureDetector.setOnDoubleTapListener(customGestureDetector);
            overLayView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
            });
        }


//
//        volumePref = getSharedPreferences("volumeSwitch", Context.MODE_PRIVATE);
//        volumeStatus = volumePref.getInt("volumeSwitch", 1);
//        if(volumeStatus == 0){
//            mSettingsContentObserver = new SettingsContentObserver(this, new Handler());
//            getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mSettingsContentObserver );
//        }
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
//        getApplicationContext().getContentResolver().unregisterContentObserver(mSettingsContentObserver);
    }

//    public class SettingsContentObserver extends ContentObserver {
//        int previousVolume;
//        Context context;
//
//        public SettingsContentObserver(Context c, Handler handler) {
//            super(handler);
//            context=c;
//
//            AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
//            previousVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
//        }
//
//        @Override
//        public boolean deliverSelfNotifications() {
//            return super.deliverSelfNotifications();
//        }
//
//        @Override
//        public void onChange(boolean selfChange) {
//            super.onChange(selfChange);
//            stopService(intent);
//        }
//    }
//
    public class CustomGestureDetector implements GestureDetector.OnGestureListener,
            GestureDetector.OnDoubleTapListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            stopService(intent);
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            stopService(intent);
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }
    }
}