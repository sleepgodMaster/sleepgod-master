package com.org.sleepgod.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.org.sleepgod.R;
import com.org.sleepgod.utils.UIUtils;

import java.util.Calendar;

/**
 * 时钟效果
 * Created by cool on 2016/11/1.
 * 博客参考：http://blog.csdn.net/qq_26971803/article/details/52061943
 */

public class WatchBoardView extends View {

    private float mRadius; //外圆半径
    private float mPadding; //边距
    private float mTextSize; //文字大小
    private float mHourPointWidth; //时针宽度
    private float mMinutePointWidth; //分针宽度
    private float mSecondPointWidth; //秒针宽度
    private int mPointRadius; // 指针圆角
    private float mPointEndLength; //指针末尾的长度

    private int mColorLong; //长线的颜色
    private int mColorShort; //短线的颜色
    private int mHourPointColor; //时针的颜色
    private int mMinutePointColor; //分针的颜色
    private int mSecondPointColor; //秒针的颜色

    private Paint mPaint; //画笔

    public WatchBoardView(Context context) {
        this(context,null);
    }

    public WatchBoardView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WatchBoardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.WatchBoardView);
        try {
            mPadding = ta.getDimension(R.styleable.WatchBoardView_padding, UIUtils.dp2px(context,10.0f));
            mTextSize = ta.getDimension(R.styleable.WatchBoardView_textSize,UIUtils.sp2px(context,16.0f));
            mHourPointWidth = ta.getDimension(R.styleable.WatchBoardView_hourPointWidth,UIUtils.dp2px(context,5.0f));
            mMinutePointWidth = ta.getDimension(R.styleable.WatchBoardView_minutePointWidth,UIUtils.dp2px(context,3.0f));
            mSecondPointWidth = ta.getDimension(R.styleable.WatchBoardView_secondPointWidth,UIUtils.dp2px(context,2.0f));
            mPointRadius = (int) ta.getDimension(R.styleable.WatchBoardView_pointRadius,UIUtils.dp2px(context,10.0f));
            mPointEndLength = ta.getDimension(R.styleable.WatchBoardView_pointEndLength,UIUtils.dp2px(context,10.0f));
            mColorLong = ta.getColor(R.styleable.WatchBoardView_colorLong, Color.argb(225, 0, 0, 0));
            mColorShort = ta.getColor(R.styleable.WatchBoardView_colorShort,Color.argb(125, 0, 0, 0));
            mHourPointColor = ta.getColor(R.styleable.WatchBoardView_hourPointColor,Color.BLACK);
            mMinutePointColor = ta.getColor(R.styleable.WatchBoardView_minutePointColor,Color.BLACK);
            mSecondPointColor = ta.getColor(R.styleable.WatchBoardView_secondPointWidth,Color.RED);
        }catch (Exception e){//如果出异常，全部赋予默认值
            mPadding = UIUtils.dp2px(context,10.0f);
            mTextSize = UIUtils.sp2px(context,16.0f);
            mHourPointWidth = UIUtils.dp2px(context,5.0f);
            mMinutePointWidth = UIUtils.dp2px(context,3.0f);
            mSecondPointWidth = UIUtils.dp2px(context,2.0f);
            mPointRadius = UIUtils.dp2px(context,10.0f);
            mPointEndLength = UIUtils.dp2px(context,10.0f);
            mColorLong = Color.argb(225, 0, 0, 0);
            mColorShort = Color.argb(125, 0, 0, 0);
            mHourPointColor = Color.BLACK;
            mMinutePointColor = Color.BLACK;
            mSecondPointColor = Color.RED;
        }finally {
            ta.recycle();
        }

        //初始化
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//设置抗锯齿
        mPaint.setDither(true);//设置防抖动
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = 1000;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if(widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY){
            try{
                throw new NoDetermineSizeException("宽度高度至少有一个确定的值,不能同时为wrap_content");
            }catch (NoDetermineSizeException e){
                e.printStackTrace();
            }
        }else {
            if(widthMode == MeasureSpec.EXACTLY){
                width = Math.min(width,widthSize);
            }
            if(heightMode == MeasureSpec.EXACTLY){
                width = Math.min(width,heightSize);
            }
        }
        setMeasuredDimension(width,width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();//保存画布状态
        canvas.translate(getWidth()/2,getHeight()/2);
        //画圆
        drawCircle(canvas);
        //画刻度
        drawScale(canvas);
        //画指针
        drawPointer(canvas);
        canvas.restore();//去除保存的画布状态
        //1秒重绘一次
        postInvalidateDelayed(1000);
    }

    private void drawPointer(Canvas canvas) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);//时
        int minute = calendar.get(Calendar.MINUTE);//分
        int second = calendar.get(Calendar.SECOND);//秒
        int hourAngle = (int) ((hour%12)*360/12+ (minute*100/60*0.01f*360/12));
        int minuteAngle = (second+minute*60) * 360 / 3600;
        int secondAngle = second * 360 / 60;

        //绘制时针
        canvas.save();
        canvas.rotate(hourAngle);
        mPaint.setColor(mHourPointColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mHourPointWidth);
        RectF hourRectF = new RectF(-mHourPointWidth/2,-mRadius * 3 / 5,mHourPointWidth/2,mPointEndLength);
        canvas.drawRoundRect(hourRectF,mPointRadius,mPointRadius,mPaint);
        canvas.restore();
        //绘制分针
        canvas.save();
        canvas.rotate(minuteAngle);
        mPaint.setColor(mMinutePointColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mMinutePointWidth);
        RectF minuteRectF = new RectF(-mMinutePointWidth/2,-mRadius*3.3f/5,mMinutePointWidth/2,mPointEndLength);
        canvas.drawRoundRect(minuteRectF,mPointRadius,mPointRadius,mPaint);
        canvas.restore();
        //绘制秒针
        canvas.save();
        canvas.rotate(secondAngle);
        mPaint.setColor(mSecondPointColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mSecondPointWidth);
        RectF secondRectF = new RectF(-mSecondPointWidth/2,-mRadius + mPadding ,mSecondPointWidth/2,mPointEndLength);
        canvas.drawRoundRect(secondRectF,mPointRadius,mPointRadius,mPaint);
        canvas.restore();

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mSecondPointColor);
        canvas.drawCircle(0,0,mSecondPointWidth*4,mPaint);
    }

    /**
     * 画刻度
     * @param canvas
     */
    private void drawScale(Canvas canvas) {
        int lineWidth = 0;
        for (int i = 0; i < 60; i++) {
            if(i%5 ==0){//整点时刻
                mPaint.setStrokeWidth(UIUtils.dp2px(getContext(),1.5f));
                mPaint.setColor(mColorLong);
                lineWidth = 40;
                //画每个整点的时间值
                String timeScale = i/5==0 ? "12" : i/5 + "";
                drawScaleText(timeScale,lineWidth,i,canvas);
            }else {//非整点时刻
                mPaint.setStrokeWidth(UIUtils.dp2px(getContext(),1.0f));
                mPaint.setColor(mColorShort);
                lineWidth = 30;
            }
            canvas.drawLine(0,-mRadius + mPadding,0,-mRadius + mPadding +lineWidth,mPaint);
            canvas.rotate(6);//每画一个刻度旋转6度
        }
    }

    /**
     * 画整点的数字
     * @param timeScale
     * @param canvas
     */
    private void drawScaleText(String timeScale,int lineWidth , int i,Canvas canvas) {
        mPaint.setTextSize(mTextSize);
        Rect textRect = new Rect();
        mPaint.getTextBounds(timeScale,0,timeScale.length(),textRect);
        canvas.save();
        canvas.translate(0,-mRadius + mPadding + lineWidth + (textRect.bottom - textRect.top) + UIUtils.dp2px(getContext(),5.0f));
        canvas.rotate(-i*6);
        canvas.drawText(timeScale,-(textRect.right - textRect.left)/2, textRect.bottom,mPaint);
        canvas.restore();
    }

    /**
     * 画圆
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0,0,mRadius,mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mRadius = (Math.min(w, h) - mPadding) / 2;
        mPointEndLength = mRadius / 6; //尾部指针默认为半径的六分之一
    }

    /**
     * 异常类
     */
    class NoDetermineSizeException extends Exception{
        public NoDetermineSizeException(String msg){
            super(msg);
        }
    }
}
