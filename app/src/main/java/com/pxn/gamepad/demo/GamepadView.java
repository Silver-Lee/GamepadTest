package com.pxn.gamepad.demo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.pxn.gamepad.demo.listener.GamepadListener;

public class GamepadView extends SurfaceView implements SurfaceHolder.Callback, GamepadListener {

    private GamepadHandler mDrawHandler;
    private Context mContext;

    public GamepadView(Context context) {
        this(context, null);
    }

    public GamepadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GamepadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        this.getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mDrawHandler = new GamepadHandler(surfaceHolder, mContext);
        mDrawHandler.startDrawing();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mDrawHandler != null) {
            mDrawHandler.stopDrawing();
        }
    }

    @Override
    public void onAxisEvent(MotionEvent event) {
        mDrawHandler.putAxisEvent(event);
    }

    @Override
    public void onButtonEvent(KeyEvent event) {
        mDrawHandler.putButtonEvent(event);
    }
}
