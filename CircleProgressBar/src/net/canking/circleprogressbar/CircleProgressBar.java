/*
 * Copyright (C) 2014 @CankingApp.  All Rights Reserved.
 * bolg: http://blog.csdn.net/cankingapp
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

          http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
 */
package net.canking.circleprogressbar;

import net.canking.circlepregressbar.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Բ��progressBar���������̰߳�ȫ��View����ֱ�����߳��и��½���
 * 
 * @author canking
 * @since 2014-4-10
 */
public class CircleProgressBar extends View {
    private Context mContext;
    /**
     * ���ʶ��������
     */
    private Paint paint;

    /**
     * Բ������ɫ
     */
    private int roundColor;

    /**
     * Բ�����ȵ���ɫ
     */
    private int roundProgressColor;

    /**
     * �м���Ȱٷֱȵ��ַ�������ɫ
     */
    private int textColor;

    /**
     * �м���Ȱٷֱȵ��ַ���������
     */
    private float textSize;

    /**
     * Բ���Ŀ��
     */
    private float roundWidth;

    /**
     * ������
     */
    private int max;

    /**
     * ��ǰ����
     */
    private int progress;
    /**
     * �Ƿ���ʾ�м�Ľ���
     */
    private boolean textIsDisplayable;

    /**
     * ���ȵķ��ʵ�Ļ��߿���
     */
    private int style;

    public static final int STROKE = 0;
    public static final int FILL = 1;

    public CircleProgressBar(Context context) {
        this(context, null);
        this.mContext = context;

    }

    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
    }

    public CircleProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;

        paint = new Paint();

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                net.canking.circlepregressbar.R.styleable.CircleProgressBar);

        // ��ȡ�Զ������Ժ�Ĭ��ֵ
        roundColor = mTypedArray.getColor(R.styleable.CircleProgressBar_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.CircleProgressBar_roundProgressColor,
                Color.GREEN);
        textColor = mTypedArray.getColor(R.styleable.CircleProgressBar_textColor, Color.GREEN);
        textSize = mTypedArray.getDimension(R.styleable.CircleProgressBar_textSize, 15);
        roundWidth = mTypedArray.getDimension(R.styleable.CircleProgressBar_roundWidth, 5);
        max = mTypedArray.getInteger(R.styleable.CircleProgressBar_max, 100);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.CircleProgressBar_textIsDisplayable, true);
        style = mTypedArray.getInt(R.styleable.CircleProgressBar_style, 0);

        mTypedArray.recycle();
    }

    private  int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * �������Ĵ�Բ��
         */
        int centre = getWidth() / 2; // ��ȡԲ�ĵ�x����
        int radius = (int) (centre - roundWidth / 2); // Բ���İ뾶
        paint.setColor(roundColor); // ����Բ������ɫ
        paint.setStyle(Paint.Style.STROKE); // ���ÿ���
        paint.setStrokeWidth(roundWidth); // ����Բ���Ŀ��
        paint.setAntiAlias(true); // �������
        canvas.drawCircle(centre, centre, radius, paint); // ����Բ��

        Log.e("log", centre + "");

        /**
         * �����Ȱٷֱ�
         */
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        int tTextSise = dip2px(mContext, textSize);
        paint.setTextSize(tTextSise);
        paint.setTypeface(Typeface.DEFAULT_BOLD); // ��������
        int percent = (int) (((float) progress / (float) max) * 100); // �м�Ľ��Ȱٷֱȣ���ת����float�ڽ��г������㣬��Ȼ��Ϊ0
        float textWidth = paint.measureText(percent + "%"); // ���������ȣ�������Ҫ��������Ŀ��������Բ���м�

        if (textIsDisplayable && percent != 0 && style == STROKE) {
            canvas.drawText(percent + "%", centre - textWidth / 2, centre + tTextSise / 2, paint); // �������Ȱٷֱ�
        }

        /**
         * ��Բ�� ����Բ���Ľ���
         */

        // ���ý�����ʵ�Ļ��ǿ���
        paint.setStrokeWidth(roundWidth); // ����Բ���Ŀ��
        paint.setColor(roundProgressColor); // ���ý��ȵ���ɫ
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius); // ���ڶ����Բ������״�ʹ�С�Ľ���

        switch (style) {
            case STROKE: {
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, 0, 360 * progress / max, false, paint); // ���ݽ��Ȼ�Բ��
                break;
            }
            case FILL: {
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0)
                    canvas.drawArc(oval, 0, 360 * progress / max, true, paint); // ���ݽ��Ȼ�Բ��
                break;
            }
        }

    }

    public synchronized int getMax() {
        return max;
    }

    /**
     * ���ý��ȵ����ֵ
     * 
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * ��ȡ����.��Ҫͬ��
     * 
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * ���ý��ȣ���Ϊ�̰߳�ȫ�ؼ������ڿ��Ƕ��ߵ����⣬��Ҫͬ��
     * ˢ�½������postInvalidate()���ڷ�UI�߳�ˢ��
     * 
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }

    }

    public int getCricleColor() {
        return roundColor;
    }

    public void setCricleColor(int cricleColor) {
        this.roundColor = cricleColor;
    }

    public int getCricleProgressColor() {
        return roundProgressColor;
    }

    public void setCricleProgressColor(int cricleProgressColor) {
        this.roundProgressColor = cricleProgressColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

}
