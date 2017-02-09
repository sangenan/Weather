package com.example.sangenan.weather;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.sangenan.weather.Util.BezierEvaluator;
import com.example.sangenan.weather.Util.BitmapUtil;
import com.example.sangenan.weather.Util.ScaleChangeUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by kuwakuzukusunoki on 2017/1/29.
 */

public class flyingBirds extends View  {
    private int birdTimes;
    private Paint birdPaint;

    private Bitmap bird ;
    private int Height;
    private int Width;
    private int offsetBirds;
    private int birdStartPointX;
    private int birdStartPointY;
    private int birdEndPointX;
    private int birdEndPointY;

    private int birdMovePointX;
    private int birdMovePointY;

    private int birdControlPointX;
    private int birdControlPointY;
    private static final int birdsSize = 45;
    private  boolean isDrawBirds = false;
    private List<Bitmap> birdsList = new ArrayList<>(16);

    public flyingBirds(Context context) {
        super(context);
        init(context);

    }

    public flyingBirds(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }
    private void init(Context context){
        bird = BitmapFactory.decodeResource(getResources(),R.drawable.blue01);
        birdPaint = new Paint();
        birdPaint.setStyle(Paint.Style.STROKE);
        birdTimes = 0;
        offsetBirds = ScaleChangeUtils.dp2px(context,15);
        ThreadBirds threadBirds = new ThreadBirds();
        threadBirds.start();
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Width = w;
        Height = h;
        birdStartPointX = Width;
        birdStartPointY = Height / 4;
        birdEndPointX = -3*offsetBirds-birdsSize;
        birdEndPointY = 0;
        birdControlPointX = Width / 2;
        birdControlPointY = Height / 4 + 80;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isDrawBirds) {
            canvas.drawBitmap(bird, null, new RectF(birdMovePointX, birdMovePointY, birdMovePointX + birdsSize, birdMovePointY + birdsSize), null);
            canvas.drawBitmap(bird, null, new RectF(birdMovePointX + offsetBirds, birdMovePointY - offsetBirds / 2, birdMovePointX + birdsSize + offsetBirds, birdMovePointY + birdsSize - offsetBirds / 2), null);
            canvas.drawBitmap(bird, null, new RectF(birdMovePointX + 2 * offsetBirds, birdMovePointY - offsetBirds / 2, birdMovePointX + birdsSize + 2 * offsetBirds, birdMovePointY + birdsSize - offsetBirds / 2), null);
            canvas.drawBitmap(bird, null, new RectF((float) (birdMovePointX + 1.5 * offsetBirds), (float) (birdMovePointY + offsetBirds / 2), (float) (birdMovePointX + birdsSize + 1.5 * offsetBirds), (float) (birdMovePointY + birdsSize + offsetBirds / 2)), null);
            canvas.drawBitmap(bird, null, new RectF(birdMovePointX + 3 * offsetBirds, (float) (birdMovePointY - 1.5 * offsetBirds), birdMovePointX + birdsSize + 3 * offsetBirds, (float) (birdMovePointY + birdsSize - 1.5 * offsetBirds)), null);
        }
    }
    public void startAnimBirds() {
        isDrawBirds = true;
        BezierEvaluator bezierEvaluator = new BezierEvaluator(new PointF(birdControlPointX, birdControlPointY));
        final ValueAnimator anim = ValueAnimator.ofObject(bezierEvaluator,
                new PointF(birdStartPointX, birdStartPointY),
                new PointF(birdEndPointX, birdEndPointY));
        anim.setDuration(8000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PointF point = (PointF) valueAnimator.getAnimatedValue();
                birdMovePointX = (int) point.x;
                birdMovePointY = (int) point.y;
                ++birdTimes;
                if (birdTimes % 8 == 0){
                   updatebirds(birdTimes);
                }
                invalidate();
            }
        });
        anim.setInterpolator(new LinearInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
                birdTimes = 0 ;
            }
        });
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.start();
    }
    private void updatebirds(int birdTimes){
        bird = birdsList.get(birdTimes % 16);
    }
    class ThreadBirds extends Thread{

        @Override
        public void run() {
            for (int i=0;i<15;++i){
                birdsList.add(i,BitmapFactory.decodeResource(getResources(), BitmapUtil.getBirdsResource(i+1)));
            }
        }
    }
}
