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
    public static final int MIN_POSITION = 20;//起始位置
    public static final int MAX_POSITION = 1700;//结束位置
    private OnMoveActionListener mMove = null;

    private Paint mLinePaint;//刻度线画笔
    private Paint mTextPaint;//指示数字画笔
    private Paint mRulerPaint;//指示线画笔

    private float position = 20;
    private int max = 1080;//FM频道最大值108*10
    private int min = 870;//FM频道最小值87*10

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
        canvas.drawLine(position, 0, position, 140, mRulerPaint);
        mTextPaint.setTextSize(24);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                if (x < MIN_POSITION) {
                    setPosition(MIN_POSITION);
                } else if (x > MAX_POSITION) {
                    setPosition(MAX_POSITION);
                } else {
                    setPosition((int) x);
                }
                //移动指示条
                if (mMove != null) {
                    mMove.onMove(Double.parseDouble(String.format("%.1f", getFmChannel())));
                }
                Log.d("TAG", "position:" + position);
                Log.d("TAG", "channel:" + getFmChannel());
            case MotionEvent.ACTION_CANCEL:
                //只停在0.1(刻度线上)的位置
                setFmChanel(Double.parseDouble(String.format("%.1f", getFmChannel())));
                Log.d("停下来后", "channel:" + Double.parseDouble(String.format("%.1f", getFmChannel())));
                break;
            default:
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //解决刻度尺和viewPager的滑动冲突
        //当滑动刻度尺时，告知父控件不要拦截事件，交给子view处理
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    public void setPosition(int i) {
        position = i;
        invalidate();
    }

    public void setFmChanel(double fmChanel) {
        int temp = (int) ((fmChanel - 87) * 80) + 20;
        setPosition(temp);
    }

    public double getFmChannel() {
        return ((position - 20.0) / 80.0 + 87.0);
    }

    /**
     * 定义监听接口
     */
    public interface OnMoveActionListener {
        void onMove(double x);
    }

    /**
     * 为每个接口设置监听器
     */
    public void setOnMoveActionListener(OnMoveActionListener move) {
        mMove = move;
    }

}
