package com.pxn.gamepad.demo.listener;

import android.view.KeyEvent;
import android.view.MotionEvent;

public interface GamepadListener {

    void onAxisEvent(MotionEvent event);

    void onButtonEvent(KeyEvent event);

}
