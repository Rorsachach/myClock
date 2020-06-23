package com.example.myclock;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    /**
     * 最大的屏幕亮度
     */
    float maxLight;
    /**
     * 当前的亮度
     */
    float currentLight;

    /**
     * 用来控制屏幕亮度
     */
    Handler handler;

    long DenyTime = 5 * 1000L;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        init();
    }

    private void init() {
        handler = new Handler(Looper.getMainLooper());
        maxLight = GetLightness(this);
    }

    void SetLight(Activity context, int light) {
        currentLight = light;
        WindowManager.LayoutParams localLayoutParams = context.getWindow().getAttributes();
        localLayoutParams.screenBrightness = (light / 255.0F);
        context.getWindow().setAttributes(localLayoutParams);
    }

    float GetLightness(Activity context) {
        WindowManager.LayoutParams localLayoutParams = context.getWindow().getAttributes();
        float light = localLayoutParams.screenBrightness;
        return light;
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopSleepTask();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startSleepTask();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (currentLight == 1) {
            startSleepTask();
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 开启休眠任务
     */
    void startSleepTask() {
        SetLight(this, (int) maxLight);
        handler.removeCallbacks(sleepWindowTask);
        handler.postDelayed(sleepWindowTask, DenyTime);
    }

    /**
     * 结束休眠任务
     */
    void stopSleepTask() {
        handler.removeCallbacks(sleepWindowTask);
    }

    /**
     * 休眠任务
     */
    Runnable sleepWindowTask = new Runnable() {

        @Override
        public void run() {
            SetLight(MainActivity.this, 1);
        }
    };

}
