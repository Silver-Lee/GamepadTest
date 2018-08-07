package com.pxn.gamepad.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import com.pxn.gamepad.demo.util.ScreenUtil;

public class GamepadHandler extends Thread {

    private Context mContext;
    private SurfaceHolder mSurfaceHolder;
    private volatile boolean mRunning;
    private boolean mStartDrawing;
    private ScreenUtil mScreenUtil;

    private Paint mStrokePaint, mFillPaint;
    private int mBoardBgColor;
    private int mJoystickBgColor;
    private int mGenericNormalColor;
    private int mGenericPressedColor;

    private int mCircleRadius;
    private int mJoyStickRadius, mJoyStickBgRadius;
    private int mThumbTextSize;

    private String mThumbLeftText = "LS";
    private int mThumbLeftTextWidth, mThumbLeftTextHeight;
    private int mLeftStickCenterX, mLeftStickCenterY;
    private int mLeftStickBgCenterX, mLeftStickBgCenterY;

    private String mThumbRightText = "RS";
    private int mThumbRightTextWidth, mThumbRightTextHeight;
    private int mRightStickCenterX, mRightStickCenterY;
    private int mRightStickBgCenterX, mRightStickBgCenterY;

    private int mTriggerTextSize;
    private int mTriggerCircleRadius;

    private String mLeftTriggerText = "LT";
    private int mLeftTriggerProgress;
    private int mLeftTriggerTextX, mLeftTriggerTextY;
    private int mLeftTriggerCircleX, mLeftTriggerCircleY;

    private String mRightTriggerText = "RT";
    private int mRightTriggerProgress;
    private int mRightTriggerTextX, mRightTriggerTextY;
    private int mRightTriggerCircleX, mRightTriggerCircleY;

    private int mBumperWidth, mBumperHeight;

    private String mLeftBumperText = "LB";
    private int mLeftBumperX, mLeftBumperY;
    private int mLeftBumperTextX, mLeftBumperTextY;

    private String mRightBumperText = "RB";
    private int mRightBumperX, mRightBumperY;
    private int mRightBumperTextX, mRightBumperTextY;

    private int mDpadCenterX, mDpadCenterY;

    private int mButtonPadX, mButtonPadY;
    private int mBtnACenterX, mBtnACenterY;
    private int mBtnBCenterX, mBtnBCenterY;
    private int mBtnXCenterX, mBtnXCenterY;
    private int mBtnYCenterX, mBtnYCenterY;
    private int mButtonRadius;
    private String mBtnAText = "A";
    private String mBtnBText = "B";
    private String mBtnXText = "X";
    private String mBtnYText = "Y";
    private int mBtnATextX, mBtnATextY;
    private int mBtnBTextX, mBtnBTextY;
    private int mBtnXTextX, mBtnXTextY;
    private int mBtnYTextX, mBtnYTextY;

    private int mCenterBtnWidth, mCenterBtnHeight;
    private int mCenterBtnTextSize;

    private String mBtnStartText = "START";
    private int mBtnStartX;
    private int mBtnStartTextX, mBtnStartTextY;

    private String mBtnSelectText = "SELECT";
    private int mBtnSelectX, mBtnSelectY;
    private int mBtnSelectTextX, mBtnSelectTextY;

    private SparseIntArray mButtonColorMap = new SparseIntArray();
    private static final int KEY_LEFT_TRIGGER = KeyEvent.KEYCODE_BUTTON_L2;
    private static final int KEY_LEFT_BUMPER = KeyEvent.KEYCODE_BUTTON_L1;
    private static final int KEY_RIGHT_TRIGGER = KeyEvent.KEYCODE_BUTTON_R2;
    private static final int KEY_RIGHT_BUMPER = KeyEvent.KEYCODE_BUTTON_R1;
    private static final int KEY_DPAD_LEFT = KeyEvent.KEYCODE_DPAD_LEFT;
    private static final int KEY_DPAD_UP = KeyEvent.KEYCODE_DPAD_UP;
    private static final int KEY_DPAD_RIGHT = KeyEvent.KEYCODE_DPAD_RIGHT;
    private static final int KEY_DPAD_DOWN = KeyEvent.KEYCODE_DPAD_DOWN;
    private static final int KEY_BUTTON_A = KeyEvent.KEYCODE_BUTTON_A;
    private static final int KEY_BUTTON_B = KeyEvent.KEYCODE_BUTTON_B;
    private static final int KEY_BUTTON_X = KeyEvent.KEYCODE_BUTTON_X;
    private static final int KEY_BUTTON_Y = KeyEvent.KEYCODE_BUTTON_Y;
    private static final int KEY_BUTTON_SELECT = KeyEvent.KEYCODE_BUTTON_SELECT;
    private static final int KEY_BUTTON_START = KeyEvent.KEYCODE_BUTTON_START;
    private static final int KEY_THUMB_LEFT = KeyEvent.KEYCODE_BUTTON_THUMBL;
    private static final int KEY_THUMB_RIGHT = KeyEvent.KEYCODE_BUTTON_THUMBR;

    public GamepadHandler(SurfaceHolder surfaceHolder, Context context) {
        mScreenUtil = ScreenUtil.getInstance();
        mContext = context;
        mSurfaceHolder = surfaceHolder;
        mRunning = true;
        mStrokePaint = new Paint();
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mFillPaint = new Paint();
        mFillPaint.setAntiAlias(true);
        mFillPaint.setStyle(Paint.Style.FILL);
        mBoardBgColor = Color.WHITE;
        mGenericNormalColor = Color.WHITE;
        mGenericPressedColor = ContextCompat.getColor(mContext, R.color.colorJoystickBg);
        mJoystickBgColor = ContextCompat.getColor(mContext, R.color.colorJoystickBg);

        mCircleRadius = mScreenUtil.getDimenUnit(60, ScreenUtil.UNIT_DP);
        initJoyStickElement();
        initTriggerElement();
        initBumperElement();
        initDpadElement();
        initBtnPadElement();
        initCenterBtnElement();
    }

    private void initJoyStickElement() {
        mJoyStickBgRadius = mScreenUtil.getDimenUnit(40, ScreenUtil.UNIT_DP);
        mJoyStickRadius = mScreenUtil.getDimenUnit(30, ScreenUtil.UNIT_DP);
        mThumbTextSize = mScreenUtil.getDimenUnit(16, ScreenUtil.UNIT_SP);

        mLeftStickBgCenterX = mScreenUtil.getScreenWidth() / 4;
        mLeftStickBgCenterY = mScreenUtil.getScreenHeight() / 4 * 3;
        mLeftStickCenterX = mLeftStickBgCenterX;
        mLeftStickCenterY = mLeftStickBgCenterY;

        mRightStickBgCenterX = mScreenUtil.getScreenWidth() / 4 * 3;
        mRightStickBgCenterY = mLeftStickBgCenterY;
        mRightStickCenterX = mRightStickBgCenterX;
        mRightStickCenterY = mRightStickBgCenterY;

        Rect bounds = new Rect();
        mStrokePaint.setTextSize(mThumbTextSize);
        mStrokePaint.getTextBounds(mThumbLeftText, 0, mThumbLeftText.length(), bounds);
        mThumbLeftTextWidth = bounds.width();
        mThumbLeftTextHeight = bounds.height();

        mStrokePaint.getTextBounds(mThumbRightText, 0, mThumbRightText.length(), bounds);
        mThumbRightTextWidth = bounds.width();
        mThumbRightTextHeight = bounds.height();

    }

    private void initTriggerElement() {

        mTriggerCircleRadius = mScreenUtil.getDimenUnit(14, ScreenUtil.UNIT_DP);
        mTriggerTextSize = mScreenUtil.getDimenUnit(13, ScreenUtil.UNIT_SP);
        mStrokePaint.setTextSize(mTriggerTextSize);

        mLeftTriggerCircleX = mScreenUtil.getDimenUnit(15, ScreenUtil.UNIT_DP) + mTriggerCircleRadius;
        mLeftTriggerCircleY = mScreenUtil.getScreenHeight() / 2;

        float ltTextWidth = mStrokePaint.measureText(mLeftTriggerText);
        mLeftTriggerTextX = (int) (mLeftTriggerCircleX - mTriggerCircleRadius + ltTextWidth / 2);
        mLeftTriggerTextY = mLeftTriggerCircleY + (mTriggerCircleRadius / 3);

        mRightTriggerCircleX = mScreenUtil.getScreenWidth() - mScreenUtil.getDimenUnit(15, ScreenUtil.UNIT_DP) - mTriggerCircleRadius;
        mRightTriggerCircleY = mLeftTriggerCircleY;

        float rtTextWidth = mStrokePaint.measureText(mRightTriggerText);
        mRightTriggerTextX = (int) (mRightTriggerCircleX - rtTextWidth / 2);
        mRightTriggerTextY = mLeftTriggerTextY;
    }

    private void initBumperElement() {

        mBumperWidth = mScreenUtil.getDimenUnit(75, ScreenUtil.UNIT_DP);
        mBumperHeight = mScreenUtil.getDimenUnit(37, ScreenUtil.UNIT_DP);

        mLeftBumperX = mScreenUtil.getDimenUnit(15, ScreenUtil.UNIT_DP);
        mLeftBumperY = mLeftBumperX;

        float lbTextWidth = mStrokePaint.measureText(mLeftBumperText);
        mLeftBumperTextX = (int) (mLeftBumperX + mBumperWidth / 2 - lbTextWidth / 2);
        mLeftBumperTextY = mLeftBumperY + mBumperHeight / 2 + mBumperHeight / 2 / 3;

        mRightBumperX = mScreenUtil.getScreenWidth() - mScreenUtil.getDimenUnit(15, ScreenUtil.UNIT_DP) - mBumperWidth;
        mRightBumperY = mLeftBumperY;

        float rbTextWidth = mStrokePaint.measureText(mRightBumperText);
        mRightBumperTextX = (int) (mRightBumperX + mBumperWidth / 2 - rbTextWidth / 2);
        mRightBumperTextY = mLeftBumperTextY;

    }

    private void initDpadElement() {
        mDpadCenterX = mScreenUtil.getScreenWidth() / 4;
        mDpadCenterY = mScreenUtil.getScreenHeight() / 3;
    }

    private void initBtnPadElement() {
        mButtonRadius = mScreenUtil.getDimenUnit(15, ScreenUtil.UNIT_DP);

        mButtonPadX = mScreenUtil.getScreenWidth() / 4 * 3;
        mButtonPadY = mScreenUtil.getScreenHeight() / 3;

        mBtnXCenterX = mButtonPadX - mCircleRadius / 2;
        mBtnXCenterY = mButtonPadY;

        mBtnYCenterX = mButtonPadX;
        mBtnYCenterY = mButtonPadY - mCircleRadius / 2;

        mBtnACenterX = mButtonPadX;
        mBtnACenterY = mButtonPadY + mCircleRadius / 2;

        mBtnBCenterX = mButtonPadX + mCircleRadius / 2;
        mBtnBCenterY = mButtonPadY;

        float btnATextWidth = mStrokePaint.measureText(mBtnAText);
        mBtnATextX = (int) (mBtnACenterX - btnATextWidth / 2);
        mBtnATextY = mBtnACenterY + (mButtonRadius / 3);


        float btnBTextWidth = mStrokePaint.measureText(mBtnBText);
        mBtnBTextX = (int) (mBtnBCenterX - btnBTextWidth / 2);
        mBtnBTextY = mBtnBCenterY + (mButtonRadius / 3);

        float btnXTextWidth = mStrokePaint.measureText(mBtnXText);
        mBtnXTextX = (int) (mBtnXCenterX - btnXTextWidth / 2);
        mBtnXTextY = mBtnXCenterY + (mButtonRadius / 3);

        float btnYTextWidth = mStrokePaint.measureText(mBtnYText);
        mBtnYTextX = (int) (mBtnYCenterX - btnYTextWidth / 2);
        mBtnYTextY = mBtnYCenterY + (mButtonRadius / 3);
    }

    private void initCenterBtnElement() {

        mCenterBtnWidth = mScreenUtil.getDimenUnit(45, ScreenUtil.UNIT_DP);
        mCenterBtnHeight = mScreenUtil.getDimenUnit(15, ScreenUtil.UNIT_DP);
        mCenterBtnTextSize = mScreenUtil.getDimenUnit(8, ScreenUtil.UNIT_SP);

        Rect rect = new Rect();
        mFillPaint.setTextSize(mCenterBtnTextSize);
        mFillPaint.getTextBounds(mBtnSelectText, 0, mBtnSelectText.length(), rect);

        mBtnSelectX = mScreenUtil.getScreenWidth() / 2 - mCenterBtnWidth - mScreenUtil.getDimenUnit(20, ScreenUtil.UNIT_DP);
        mBtnSelectY = mDpadCenterY - mCenterBtnHeight / 2;

        mBtnSelectTextX = mBtnSelectX + mCenterBtnWidth / 2 - rect.width() / 2;
        mBtnSelectTextY = mBtnSelectY - rect.height();

        mFillPaint.getTextBounds(mBtnStartText, 0, mBtnStartText.length(), rect);

        mBtnStartX = mScreenUtil.getScreenWidth() / 2 + mScreenUtil.getDimenUnit(20, ScreenUtil.UNIT_DP);

        mBtnStartTextX = mBtnStartX + mCenterBtnWidth / 2 - rect.width() / 2;
        mBtnStartTextY = mBtnSelectTextY;

    }

    public void putAxisEvent(MotionEvent event) {

        updateLeftStick(event.getAxisValue(MotionEvent.AXIS_X), event.getAxisValue(MotionEvent.AXIS_Y));

        updateRightStick(event.getAxisValue(MotionEvent.AXIS_Z), event.getAxisValue(MotionEvent.AXIS_RZ));

        updateLeftTrigger(event.getAxisValue(MotionEvent.AXIS_BRAKE));

        updateRightTrigger(event.getAxisValue(MotionEvent.AXIS_GAS));


    }

    public void putButtonEvent(KeyEvent event) {
        boolean isButtonDown = event.getAction() == KeyEvent.ACTION_DOWN;
        mButtonColorMap.put(event.getKeyCode(), isButtonDown ? mGenericPressedColor : mGenericNormalColor);
    }

    private void updateLeftStick(float axisX, float axisY) {
        if (axisX == 0 && axisY == 0) {
            mLeftStickCenterX = mLeftStickBgCenterX;
            mLeftStickCenterY = mLeftStickBgCenterY;
        } else {
            int moveX = (int) (mJoyStickBgRadius * axisX);
            mLeftStickCenterX = mLeftStickBgCenterX + moveX;

            int moveY = (int) (mJoyStickBgRadius * axisY);
            mLeftStickCenterY = mLeftStickBgCenterY + moveY;
        }
    }

    private void updateRightStick(float axisX, float axisY) {
        if (axisX == 0 && axisY == 0) {
            mRightStickCenterX = mRightStickBgCenterX;
            mRightStickCenterY = mRightStickBgCenterY;
        } else {
            int moveX = (int) (mJoyStickBgRadius * axisX);
            mRightStickCenterX = mRightStickBgCenterX + moveX;

            int moveY = (int) (mJoyStickBgRadius * axisY);
            mRightStickCenterY = mRightStickBgCenterY + moveY;
        }
    }

    private void updateLeftTrigger(float axisLT) {
        mLeftTriggerProgress = (int) (mScreenUtil.getScreenHeight() * axisLT);
    }

    private void updateRightTrigger(float axisRT) {
        mRightTriggerProgress = (int) (mScreenUtil.getScreenHeight() * axisRT);
    }

    @Override
    public void run() {
        while (mRunning && mSurfaceHolder.getSurface().isValid()) {
            try {
                Canvas localCanvas = this.mSurfaceHolder.lockCanvas();
                if (localCanvas != null) {
                    draw(localCanvas);
                }

                if (localCanvas == null) {
                    continue;
                }

                try {
                    this.mSurfaceHolder.unlockCanvasAndPost(localCanvas);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void draw(Canvas canvas) {
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        mFillPaint.setColor(mBoardBgColor);
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), mFillPaint);

        drawJoyStick(canvas);
        drawTrigger(canvas);
        drawBumper(canvas);
        drawDpad(canvas);
        drawButtonPad(canvas);
        drawCenterButton(canvas);
    }

    private void drawJoyStick(Canvas canvas) {
        mFillPaint.setColor(mJoystickBgColor);
        canvas.drawCircle(mLeftStickBgCenterX, mLeftStickBgCenterY, mJoyStickBgRadius, mFillPaint);
        canvas.drawCircle(mRightStickBgCenterX, mRightStickBgCenterY, mJoyStickBgRadius, mFillPaint);
        mStrokePaint.setColor(Color.BLACK);
        mStrokePaint.setStrokeWidth(mScreenUtil.getDimenUnit(2, ScreenUtil.UNIT_DP));
        canvas.drawCircle(mLeftStickBgCenterX, mLeftStickBgCenterY, mJoyStickBgRadius, mStrokePaint);
        canvas.drawCircle(mRightStickBgCenterX, mRightStickBgCenterY, mJoyStickBgRadius, mStrokePaint);
        mFillPaint.setColor(Color.BLACK);
        canvas.drawCircle(mLeftStickCenterX, mLeftStickCenterY, mJoyStickRadius, mFillPaint);
        canvas.drawCircle(mRightStickCenterX, mRightStickCenterY, mJoyStickRadius, mFillPaint);

        mFillPaint.setColor(mButtonColorMap.get(KEY_THUMB_LEFT, mGenericNormalColor));
        canvas.drawCircle(mLeftStickCenterX, mLeftStickCenterY, mTriggerCircleRadius, mFillPaint);
        mFillPaint.setColor(mButtonColorMap.get(KEY_THUMB_RIGHT, mGenericNormalColor));
        canvas.drawCircle(mRightStickCenterX, mRightStickCenterY, mTriggerCircleRadius, mFillPaint);

        canvas.drawText(mThumbLeftText, mLeftStickCenterX - mThumbLeftTextWidth / 2, mLeftStickCenterY + mThumbLeftTextHeight / 3, mStrokePaint);
        canvas.drawText(mThumbRightText, mRightStickCenterX - mThumbRightTextWidth / 2, mRightStickCenterY + mThumbRightTextHeight / 3, mStrokePaint);
    }

    private void drawTrigger(Canvas canvas) {
        /* draw progress */
        mFillPaint.setColor(mJoystickBgColor);
        mFillPaint.setStrokeWidth(mScreenUtil.getDimenUnit(10, ScreenUtil.UNIT_DP));

        float startX = mFillPaint.getStrokeWidth() / 2;
        float startY = mScreenUtil.getScreenHeight();
        float stopX = startX;
        float stopY = mScreenUtil.getScreenHeight() - mLeftTriggerProgress;

        canvas.drawLine(startX, startY, stopX, stopY, mFillPaint);

        startX = mScreenUtil.getScreenWidth() - mFillPaint.getStrokeWidth() / 2;
        stopX = startX;
        stopY = mScreenUtil.getScreenHeight() - mRightTriggerProgress;

        canvas.drawLine(startX, startY, stopX, stopY, mFillPaint);

        /* draw text circle */
        mFillPaint.setColor(mButtonColorMap.get(KEY_LEFT_TRIGGER, mGenericNormalColor));
        canvas.drawCircle(mLeftTriggerCircleX, mLeftTriggerCircleY, mTriggerCircleRadius, mFillPaint);
        canvas.drawCircle(mLeftTriggerCircleX, mLeftTriggerCircleY, mTriggerCircleRadius, mStrokePaint);

        mFillPaint.setColor(mButtonColorMap.get(KEY_RIGHT_TRIGGER, mGenericNormalColor));
        canvas.drawCircle(mRightTriggerCircleX, mRightTriggerCircleY, mTriggerCircleRadius, mFillPaint);
        canvas.drawCircle(mRightTriggerCircleX, mRightTriggerCircleY, mTriggerCircleRadius, mStrokePaint);

        mStrokePaint.setTextSize(mTriggerTextSize);
        canvas.drawText(mLeftTriggerText, mLeftTriggerTextX, mLeftTriggerTextY, mStrokePaint);
        canvas.drawText(mRightTriggerText, mRightTriggerTextX, mRightTriggerTextY, mStrokePaint);
    }

    private void drawBumper(Canvas canvas) {
        mFillPaint.setColor(mButtonColorMap.get(KEY_LEFT_BUMPER, mGenericNormalColor));
        RectF leftRect = new RectF(mLeftBumperX, mLeftBumperY, mLeftBumperX + mBumperWidth, mLeftBumperY + mBumperHeight);
        canvas.drawRoundRect(leftRect, 20, 20, mFillPaint);
        canvas.drawRoundRect(leftRect, 20, 20, mStrokePaint);
        canvas.drawText(mLeftBumperText, mLeftBumperTextX, mLeftBumperTextY, mStrokePaint);

        mFillPaint.setColor(mButtonColorMap.get(KEY_RIGHT_BUMPER, mGenericNormalColor));
        RectF rightRect = new RectF(mRightBumperX, mRightBumperY, mRightBumperX + mBumperWidth, mRightBumperY + mBumperHeight);
        canvas.drawRoundRect(rightRect, 20, 20, mFillPaint);
        canvas.drawRoundRect(rightRect, 20, 20, mStrokePaint);
        canvas.drawText(mRightBumperText, mRightBumperTextX, mRightBumperTextY, mStrokePaint);
    }

    private void drawDpad(Canvas canvas) {
        canvas.drawCircle(mDpadCenterX, mDpadCenterY, mCircleRadius, mStrokePaint);

        mStrokePaint.setStrokeJoin(Paint.Join.ROUND);
        mStrokePaint.setStrokeCap(Paint.Cap.ROUND);

        int dp10 = mScreenUtil.getDimenUnit(10, ScreenUtil.UNIT_DP);

        /* Up */
        Path path = new Path();
        path.moveTo(mDpadCenterX, mDpadCenterY - dp10);
        path.rLineTo(dp10, -dp10);
        path.rLineTo(0, -dp10 * 2);
        path.rLineTo(-dp10 * 2, 0);
        path.rLineTo(0, dp10 * 2);
        path.close();

        mFillPaint.setColor(mButtonColorMap.get(KEY_DPAD_UP, mGenericNormalColor));
        canvas.drawPath(path, mFillPaint);
        canvas.drawPath(path, mStrokePaint);

        /* Left */
        path.reset();
        path.moveTo(mDpadCenterX - dp10, mDpadCenterY);
        path.rLineTo(-dp10, -dp10);
        path.rLineTo(-dp10 * 2, 0);
        path.rLineTo(0, dp10 * 2);
        path.rLineTo(dp10 * 2, 0);
        path.close();
        mFillPaint.setColor(mButtonColorMap.get(KEY_DPAD_LEFT, mGenericNormalColor));
        canvas.drawPath(path, mFillPaint);
        canvas.drawPath(path, mStrokePaint);

        /* Down */
        path.reset();
        path.moveTo(mDpadCenterX, mDpadCenterY + dp10);
        path.rLineTo(-dp10, dp10);
        path.rLineTo(0, dp10 * 2);
        path.rLineTo(dp10 * 2, 0);
        path.rLineTo(0, -dp10 * 2);
        path.close();
        mFillPaint.setColor(mButtonColorMap.get(KEY_DPAD_DOWN, mGenericNormalColor));
        canvas.drawPath(path, mFillPaint);
        canvas.drawPath(path, mStrokePaint);

        /* Right */
        path.reset();
        path.moveTo(mDpadCenterX + dp10, mDpadCenterY);
        path.rLineTo(dp10, dp10);
        path.rLineTo(dp10 * 2, 0);
        path.rLineTo(0, -dp10 * 2);
        path.rLineTo(-dp10 * 2, 0);
        path.close();
        mFillPaint.setColor(mButtonColorMap.get(KEY_DPAD_RIGHT, mGenericNormalColor));
        canvas.drawPath(path, mFillPaint);
        canvas.drawPath(path, mStrokePaint);

    }

    private void drawButtonPad(Canvas canvas) {

        canvas.drawCircle(mButtonPadX, mButtonPadY, mCircleRadius, mStrokePaint);

        mFillPaint.setColor(mButtonColorMap.get(KEY_BUTTON_A, mGenericNormalColor));
        canvas.drawCircle(mBtnACenterX, mBtnACenterY, mButtonRadius, mFillPaint);
        canvas.drawCircle(mBtnACenterX, mBtnACenterY, mButtonRadius, mStrokePaint);
        canvas.drawText(mBtnAText, mBtnATextX, mBtnATextY, mStrokePaint);

        mFillPaint.setColor(mButtonColorMap.get(KEY_BUTTON_B, mGenericNormalColor));
        canvas.drawCircle(mBtnBCenterX, mBtnBCenterY, mButtonRadius, mFillPaint);
        canvas.drawCircle(mBtnBCenterX, mBtnBCenterY, mButtonRadius, mStrokePaint);
        canvas.drawText(mBtnBText, mBtnBTextX, mBtnBTextY, mStrokePaint);

        mFillPaint.setColor(mButtonColorMap.get(KEY_BUTTON_X, mGenericNormalColor));
        canvas.drawCircle(mBtnXCenterX, mBtnXCenterY, mButtonRadius, mFillPaint);
        canvas.drawCircle(mBtnXCenterX, mBtnXCenterY, mButtonRadius, mStrokePaint);
        canvas.drawText(mBtnXText, mBtnXTextX, mBtnXTextY, mStrokePaint);

        mFillPaint.setColor(mButtonColorMap.get(KEY_BUTTON_Y, mGenericNormalColor));
        canvas.drawCircle(mBtnYCenterX, mBtnYCenterY, mButtonRadius, mFillPaint);
        canvas.drawCircle(mBtnYCenterX, mBtnYCenterY, mButtonRadius, mStrokePaint);
        canvas.drawText(mBtnYText, mBtnYTextX, mBtnYTextY, mStrokePaint);
    }

    private void drawCenterButton(Canvas canvas) {

        mFillPaint.setColor(mButtonColorMap.get(KEY_BUTTON_SELECT, mGenericNormalColor));
        RectF rect = new RectF(mBtnSelectX, mBtnSelectY, mBtnSelectX + mCenterBtnWidth, mBtnSelectY + mCenterBtnHeight);
        canvas.drawRoundRect(rect, 20, 20, mFillPaint);
        canvas.drawRoundRect(rect, 20, 20, mStrokePaint);

        mFillPaint.setColor(mButtonColorMap.get(KEY_BUTTON_START, mGenericNormalColor));
        rect.left = mBtnStartX;
        rect.right = mBtnStartX + mCenterBtnWidth;
        canvas.drawRoundRect(rect, 20, 20, mFillPaint);
        canvas.drawRoundRect(rect, 20, 20, mStrokePaint);

        mFillPaint.setColor(Color.BLACK);
        mFillPaint.setTextSize(mCenterBtnTextSize);
        canvas.drawText(mBtnSelectText, mBtnSelectTextX, mBtnSelectTextY, mFillPaint);
        canvas.drawText(mBtnStartText, mBtnStartTextX, mBtnStartTextY, mFillPaint);

    }

    public void startDrawing() {
        if (!mStartDrawing) {
            mStartDrawing = true;
            start();
        }
    }

    public void stopDrawing() {
        mRunning = false;
    }
}
