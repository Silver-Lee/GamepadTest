package com.pxn.gamepad.demo.util;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

public class ScreenUtil {

    public static final int UNIT_DP = 0;
    public static final int UNIT_SP = 1;
    public static final int UNIT_PX = 2;

    private Context mContext;
    private int mScreenWidth;
    private int mScreenHeight;
    private static ScreenUtil instance;

    private ScreenUtil() {
    }

    public static ScreenUtil getInstance() {
        if (instance == null) {
            synchronized (ScreenUtil.class) {
                if (instance == null) {
                    instance = new ScreenUtil();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        mContext = context;
        obtainWH();
    }

    private void obtainWH() {
        WindowManager manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
    }

    public int getScreenWidth() {
        return mScreenWidth;
    }

    public int getScreenHeight() {
        return mScreenHeight;
    }

    public int getDimenUnit(int value, int unit) {
        int unitType = TypedValue.COMPLEX_UNIT_DIP;
        switch (unit) {
            case UNIT_DP:
                unitType = TypedValue.COMPLEX_UNIT_DIP;
                break;
            case UNIT_SP:
                unitType = TypedValue.COMPLEX_UNIT_SP;
                break;
            case UNIT_PX:
                unitType = TypedValue.COMPLEX_UNIT_PX;
                break;
        }
        return (int) TypedValue.applyDimension(unitType, value, mContext.getResources().getDisplayMetrics());
    }

}
