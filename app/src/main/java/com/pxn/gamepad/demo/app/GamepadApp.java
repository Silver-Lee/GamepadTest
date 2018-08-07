package com.pxn.gamepad.demo.app;

import android.app.Application;

import com.pxn.gamepad.demo.util.ScreenUtil;

public class GamepadApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ScreenUtil.getInstance().init(this);
    }
}
