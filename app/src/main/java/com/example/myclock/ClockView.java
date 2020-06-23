package com.example.myclock;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class ClockView extends View {

    private Canvas canvas;
    private Context context;
    private int hour;
    private int minute;
    private int width;
    private int height;
    private Calendar c;
    private Paint rectPaint;
    private boolean flag = false;

    private Rect rect;

    private Paint textPaint;

    private MediaPlayer mediaPlayer;

    public ClockView(Context context) {
        super(context);
        initView(context);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        this.context = context;

        // 方框画笔
        rectPaint = new Paint();
        rectPaint.setStrokeWidth((float) 25);
        rectPaint.setStrokeCap(Paint.Cap.ROUND);
        rectPaint.setStyle(Paint.Style.FILL);
        rectPaint.setAntiAlias(true);
        rectPaint.setColor(context.getResources().getColor(R.color.red));
        rectPaint.setMaskFilter(new BlurMaskFilter(70f, BlurMaskFilter.Blur.SOLID));

        rect = new Rect();

        textPaint = new Paint();
        textPaint.setStyle(Paint.Style.FILL); // 文字风格，填充
        textPaint.setAntiAlias(true); // 抗锯齿
        textPaint.setTextSize(260);
        textPaint.setColor(context.getResources().getColor(R.color.white));

        mediaPlayer = new MediaPlayer();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec); // 获取大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec); // 获取模式
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        width = widthSize;
        height = heightSize;

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.canvas = canvas;

        c = Calendar.getInstance();

        hour = c.get(Calendar.HOUR_OF_DAY);
        minute = c.get(Calendar.MINUTE);

        drawRect();
        drawText();

        if(minute == 0 || minute == 10){
            if(flag == false){
                flag = true;
                mediaPlayer = MediaPlayer.create(context, R.raw.music);
                mediaPlayer.start();
            }
        }else{
            flag = false;
        }
        invalidate();
    }

    private void drawText() {

        canvas.save();
        String text = String.format("%02d:%02d",hour,minute);
        textPaint.getTextBounds(text, 0, text.length(), rect);

        int tWidth = rect.width();
        int tHeight = rect.height();
        canvas.drawText(text, width/2 - tWidth/2, height/2 + tHeight/2, textPaint);
        canvas.restore();
    }

    private void drawRect() {
        canvas.save();
        rectPaint.setShader(new LinearGradient(
                width/7*2,
                height/4,
                width/7*5,
                height/4*3,
                Color.BLUE, Color.GREEN,
                Shader.TileMode.CLAMP
        ));
        canvas.drawLine(
                width/7*2,
                height/4 + 100,
                width/7*2,
                height/4*3,
                rectPaint
        );
        canvas.drawLine(
                width/7*2,
                height/4*3,
                width/7*5 - 100,
                height/4*3,
                rectPaint
        );
        canvas.drawLine(
                width/7*5,
                height/4,
                width/7*5,
                height/4*3 - 100,
                rectPaint
        );
        canvas.drawLine(
                width/7*2 + 100,
                height/4,
                width/7*5,
                height/4,
                rectPaint
        );
        canvas.restore();
    }
}
