package com.skyworth.car.rulerdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @Author: 高烁
 * @CreateDate: 2019-06-04 14:07
 * @Email: gaoshuo521@foxmail.com
 */
public class RulerView extends View {
    private Paint mLinePaint;
    private Paint mTextPaint;
    private Paint mRulerPaint;
    private float progress = 20;
    private int max = 1080;
    private int min = 870;

    public RulerView(Context context) {
        super(context);
        init();
    }

    public RulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLinePaint = new Paint();
        mLinePaint.setColor(getResources().getColor(R.color.grey));
        //抗锯齿
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(1);

        mTextPaint = new Paint();
        mTextPaint.setColor(getResources().getColor(R.color.grey));
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(2);
        mTextPaint.setTextSize(24);

        //指示线
        mRulerPaint = new Paint();
        mRulerPaint.setAntiAlias(true);
        mRulerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mRulerPaint.setColor(getResources().getColor(R.color.ruler_line));
        mRulerPaint.setStrokeWidth(3);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(setMeasureWidth(widthMeasureSpec), setMeasureHeight(heightMeasureSpec));
    }

    private int setMeasureHeight(int spec) {
        int mode = MeasureSpec.getMode(spec);
        int size = MeasureSpec.getSize(spec);
        int result = Integer.MAX_VALUE;
        switch (mode) {
            case MeasureSpec.AT_MOST:
                size = Math.min(result, size);
                break;
            case MeasureSpec.EXACTLY:
                break;
            default:
                size = result;
                break;
        }
        return size;
    }

    private int setMeasureWidth(int spec) {
        int mode = MeasureSpec.getMode(spec);
        int size = MeasureSpec.getSize(spec);
        int result = Integer.MAX_VALUE;
        switch (mode) {
            case MeasureSpec.AT_MOST:
                size = Math.min(result, size);
                break;
            case MeasureSpec.EXACTLY:
                break;
            default:
                size = result;
                break;
        }
        return size;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        //绘制刻度线
        for (int i = min; i <= max; i++) {
            if (i % 10 == 0) {
                canvas.drawLine(20, 0, 20, 140, mLinePaint);

                String text = i / 10 + "";
                Rect rect = new Rect();
                float txtWidth = mTextPaint.measureText(text);
                mTextPaint.getTextBounds(text, 0, text.length(), rect);
                if (i / 10 % 2 == 1 && i / 10 != 107) {
                    canvas.drawText(text, 20 - txtWidth / 2, 72 + rect.height() + 74, mTextPaint);
                }
                if (i / 10 == 108) {
                    canvas.drawText(text, 20 - txtWidth / 2, 72 + rect.height() + 74, mTextPaint);
                }
            } else if (i % 5 == 0) {
                canvas.drawLine(20, 30, 20, 110, mLinePaint);
            } else {
                canvas.drawLine(20, 54, 20, 86, mLinePaint);
            }
            canvas.translate((float) 8, 0);
        }
        canvas.restore();

        //绘制指示线
        canvas.drawLine(progress, 0, progress, 140, mRulerPaint);
        mTextPaint.setTextSize(24);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                //
                float x = event.getX();
                //移动指示条
                setProgress((int) x);
                Log.d("TAG", "progress:" + progress);
                Log.d("TAG", "channel:" + getFmChannel());
                break;
            default:
        }
        return true;
    }


    public void setProgress(int i) {
        progress = i;
        invalidate();
    }

    public void setFmChannl(double fmChanel) {
        int temp = (int)((fmChanel - 87) * 80) + 20;
        Log.d("TAG",temp+"temp");
        setProgress(temp);
    }

    public double getFmChannel(){
        return ((progress - 20.0)/80.0 +87.0);
    }

}
