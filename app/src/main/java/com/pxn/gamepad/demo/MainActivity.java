package com.pxn.gamepad.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.pxn.gamepad.demo.listener.GamepadListener;

public class MainActivity extends AppCompatActivity {

    private GamepadListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listener = findViewById(R.id.gameView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        listener.onButtonEvent(event);
        return true;
    }

    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {

        listener.onAxisEvent(event);

        float axisHatX = event.getAxisValue(MotionEvent.AXIS_HAT_X);
        float axisHatY = event.getAxisValue(MotionEvent.AXIS_HAT_Y);
        if (axisHatX == 0 && axisHatY == 0) {
            return true;
        }
        return super.onGenericMotionEvent(event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        listener.onButtonEvent(event);
        return true;
    }
}
