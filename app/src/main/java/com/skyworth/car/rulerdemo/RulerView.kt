package com.skyworth.car.rulerdemo

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * @Author: 高烁
 * @CreateDate: 2019-06-04 14:07
 * @Email: gaoshuo521@foxmail.com
 */
class RulerView : View {
    private var mMove: OnMoveActionListener? = null

    private lateinit var mLinePaint: Paint //刻度线画笔
    private lateinit var mTextPaint: Paint //指示数字画笔
    private lateinit var mRulerPaint: Paint //指示线画笔

    private var position = 20f
    private val max = 1080//FM频道最大值108*10
    private val min = 870//FM频道最小值87*10

    val fmChannel: Double
        get() = (position - 20.0) / 80.0 + 87.0

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        mLinePaint = Paint()
        mLinePaint.color = resources.getColor(R.color.grey, null)
        //抗锯齿
        mLinePaint.isAntiAlias = true
        mLinePaint.style = Paint.Style.STROKE
        mLinePaint.strokeWidth = 1f

        mTextPaint = Paint()
        mTextPaint.color = resources.getColor(R.color.grey, null)
        mTextPaint.isAntiAlias = true
        mTextPaint.style = Paint.Style.FILL
        mTextPaint.strokeWidth = 2f
        mTextPaint.textSize = 24f

        mRulerPaint = Paint()
        mRulerPaint.isAntiAlias = true
        mRulerPaint.style = Paint.Style.FILL_AND_STROKE
        mRulerPaint.color = resources.getColor(R.color.ruler_line, null)
        mRulerPaint.strokeWidth = 3f
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(setMeasureWidth(widthMeasureSpec), setMeasureHeight(heightMeasureSpec))
    }

    private fun setMeasureHeight(spec: Int): Int {
        val mode = MeasureSpec.getMode(spec)
        var size = MeasureSpec.getSize(spec)
        val result = Integer.MAX_VALUE
        when (mode) {
            MeasureSpec.AT_MOST -> size = Math.min(result, size)
            MeasureSpec.EXACTLY -> {
            }
            else -> size = result
        }
        return size
    }

    private fun setMeasureWidth(spec: Int): Int {
        val mode = MeasureSpec.getMode(spec)
        var size = MeasureSpec.getSize(spec)
        val result = Integer.MAX_VALUE
        when (mode) {
            MeasureSpec.AT_MOST -> size = Math.min(result, size)
            MeasureSpec.EXACTLY -> {
            }
            else -> size = result
        }
        return size
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        //绘制刻度线
        for (i in min..max) {
            if (i % 10 == 0) {
                canvas.drawLine(20f, 0f, 20f, 140f, mLinePaint)

                val text = (i / 10).toString() + ""
                val rect = Rect()
                val txtWidth = mTextPaint.measureText(text)
                mTextPaint.getTextBounds(text, 0, text.length, rect)
                if (i / 10 % 2 == 1 && i / 10 != 107) {
                    canvas.drawText(text, 20 - txtWidth / 2, (72 + rect.height() + 74).toFloat(), mTextPaint)
                }
                if (i / 10 == 108) {
                    canvas.drawText(text, 20 - txtWidth / 2, (72 + rect.height() + 74).toFloat(), mTextPaint)
                }
            } else if (i % 5 == 0) {
                canvas.drawLine(20f, 30f, 20f, 110f, mLinePaint)
            } else {
                canvas.drawLine(20f, 54f, 20f, 86f, mLinePaint)
            }
            canvas.translate(8.toFloat(), 0f)
        }
        canvas.restore()

        //绘制指示线
        canvas.drawLine(position, 0f, position, 140f, mRulerPaint)
        mTextPaint.textSize = 24f
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
            }
            MotionEvent.ACTION_MOVE -> {
                val x = event.x

                setPosition(when {
                    x < MIN_POSITION -> MIN_POSITION
                    x > MAX_POSITION -> MAX_POSITION
                    else -> x.toInt()
                })

                mMove?.onMove(java.lang.Double.parseDouble(String.format("%.1f", fmChannel)))

                //移动指示条
                Log.d("TAG", "position:$position")
                Log.d("TAG", "channel:$fmChannel")
                //只停在0.1(刻度线上)的位置
                setFmChanel(java.lang.Double.parseDouble(String.format("%.1f", fmChannel)))
                Log.d("停下来后", "channel:" + java.lang.Double.parseDouble(String.format("%.1f", fmChannel)))
            }
            MotionEvent.ACTION_CANCEL -> {
                setFmChanel(java.lang.Double.parseDouble(String.format("%.1f", fmChannel)))
                Log.d("停下来后", "channel:" + java.lang.Double.parseDouble(String.format("%.1f", fmChannel)))
            }
        }
        return true
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        //解决刻度尺和viewPager的滑动冲突
        //当滑动刻度尺时，告知父控件不要拦截事件，交给子view处理
        parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(ev)
    }

    fun setPosition(i: Int) {
        position = i.toFloat()
        invalidate()
    }

    fun setFmChanel(fmChanel: Double) {
        val temp = ((fmChanel - 87) * 80).toInt() + 20
        setPosition(temp)
    }

    /**
     * 定义监听接口
     */
    interface OnMoveActionListener {
        fun onMove(x: Double)
    }

    /**
     * 为每个接口设置监听器
     */
    fun setOnMoveActionListener(move: OnMoveActionListener) {
        mMove = move
    }

    companion object {
        //const 真正的常量
        const val MIN_POSITION = 20//起始位置
        const val MAX_POSITION = 1700//结束位置
    }

}
