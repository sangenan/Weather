package com.example.sangenan.weather;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.sangenan.weather.Util.BezierEvaluator;
import com.example.sangenan.weather.Util.BitmapUtil;
import com.example.sangenan.weather.Util.CalculateUtil;
import com.example.sangenan.weather.Util.ScaleChangeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuwakuzukusunoki on 2017/2/5.
 */

public class WeatherAnimView extends View {
    private int Width;
    private int Height;
    private float scaleSideHill;
    private Bitmap sidehillLeft;
    private Bitmap sidehillRight;
    private RectF mRectFLeft;
    private RectF mRectFRight;
    private static final int maxOffsetX = 50;
    private Bitmap backgroud;
    private float currentOffsetX;
    private float mProgress;

    private int birdTimes;
    private Paint birdPaint;

    private Bitmap bird;
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
    private boolean isDrawBirds = false;
    private List<Bitmap> birdsList = new ArrayList<>(16);

    private Bitmap treeBranch, treeBallLeft, treeBallRight, treeTrunk, treeBallMiddle, treeLeaf;
    private Matrix treeBranch_Matrix, treeBallLeft_Matrix, treeBallRight_Matrix, treeTrunk_Matrix, treeBallMiddle_Matrix, treeLeaf_Matrix;
    private static final float SCALESIZE = 0.2f;
    private static final float SCALESIZEBALL = 0.3f;
    private static final float SCALESIZELEAF = 0.4f;

    private int rotateCenterX;
    private int rotateCenterY;
    private int treeX;
    private int treeY;
    private float rotateValueTree = 0;
    private float rotateValueLeaf = 0;
    private boolean isDrawTree = false;
    private float treeControlPointX;
    private float treeControlPointY;
    private float treeStartPointX;
    private float treeStartPointY;
    private float treeEndPointX;
    private float treeEndPointY;
    private float moveLeafX;
    private float moveLeafY;
    private ValueAnimator valueAnimatorTree;
    private ValueAnimator valueAnimatorAlpha;
    private double randomRotate;
    private int timesTree = 0;
    private Paint treeLeafPaint;

    public WeatherAnimView(Context context) {
        super(context);
        init(context);
    }

    public WeatherAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        currentOffsetX = 0;
        mProgress = 0;

        bird = BitmapFactory.decodeResource(getResources(), R.drawable.blue01);
        birdPaint = new Paint();
        birdPaint.setStyle(Paint.Style.STROKE);
        birdTimes = 0;
        offsetBirds = ScaleChangeUtils.dp2px(context, 15);
        ThreadBirds threadBirds = new ThreadBirds();
        threadBirds.start();

        treeBranch = BitmapFactory.decodeResource(getResources(), R.drawable.bg_sunny_tree_branch_night);
        treeBallLeft = BitmapFactory.decodeResource(getResources(), R.drawable.bg_sunny_tree_ball_left_night);
        treeBallRight = BitmapFactory.decodeResource(getResources(), R.drawable.bg_sunny_tree_ball_right_night);
        treeBallMiddle = BitmapFactory.decodeResource(getResources(), R.drawable.bg_sunny_tree_ball_middle_night);
        treeTrunk = BitmapFactory.decodeResource(getResources(), R.drawable.bg_sunny_tree_trunk_night);
        treeLeaf = BitmapFactory.decodeResource(getResources(), R.drawable.bg_sunny_tree_leaf_night);
        treeLeafPaint = new Paint();

        valueAnimatorAlpha = new ValueAnimator();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Width = w;
        Height = h;
        BitmapFactory.Options optionsSideHill = new BitmapFactory.Options();
        optionsSideHill.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.bg_sunny_left_night, optionsSideHill);
        if (optionsSideHill.outHeight != 0)
            scaleSideHill = optionsSideHill.outWidth / optionsSideHill.outHeight;
        mRectFLeft = new RectF(-maxOffsetX, Height / 5 * 3, -maxOffsetX + Height / 5 * 2 * scaleSideHill, Height);
        if (Width < Height / 3 * scaleSideHill) {
            mRectFRight = new RectF(-maxOffsetX, Height / 5 * 3, maxOffsetX + Height / 5 * 2 * scaleSideHill, Height);
        } else {
            mRectFRight = new RectF(-maxOffsetX, Height / 5 * 3, maxOffsetX + Width, Height);
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.bg_sunny_right_night, options);
        options.inSampleSize = 32;
        options.inJustDecodeBounds = false;
        backgroud = BitmapFactory.decodeResource(getResources(), R.drawable.bg_sunny_night, options);
        Drawable drawable = new BitmapDrawable(backgroud);
        setBackground(drawable);

        optionsSideHill = new BitmapFactory.Options();
        optionsSideHill.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.bg_sunny_left_night, optionsSideHill);
        optionsSideHill.inSampleSize = calculateInSampleSize(optionsSideHill, (int) (Height / 5 * 2 * scaleSideHill), Height / 5 * 2);
        optionsSideHill.inJustDecodeBounds = false;
        sidehillLeft = BitmapFactory.decodeResource(getResources(), R.drawable.bg_sunny_left_night, optionsSideHill);

        optionsSideHill = new BitmapFactory.Options();
        optionsSideHill.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.bg_sunny_right_night, optionsSideHill);
        optionsSideHill.inSampleSize = calculateInSampleSize(optionsSideHill, 2 * maxOffsetX + Width, Height / 5 * 2);
        optionsSideHill.inJustDecodeBounds = false;
        sidehillRight = BitmapFactory.decodeResource(getResources(), R.drawable.bg_sunny_right_night, optionsSideHill);

        birdStartPointX = Width;
        birdStartPointY = Height / 4;
        birdEndPointX = -3 * offsetBirds - birdsSize;
        birdEndPointY = 0;
        birdControlPointX = Width / 2;
        birdControlPointY = Height / 4 + 80;

        treeX = w / 4 * 3;
        treeY = (int) (h / 5 * 3 - treeTrunk.getHeight() / 4 * SCALESIZE);

        rotateCenterX = treeX;
        rotateCenterY = (int) (treeY + treeTrunk.getHeight() * SCALESIZE / 8 + treeBranch.getHeight() * SCALESIZE);

        treeStartPointX = treeX - treeBallMiddle.getWidth() / 2 * SCALESIZEBALL;
        treeStartPointY = treeY - treeBallMiddle.getWidth() * SCALESIZEBALL / 2;
        treeEndPointX = w / 2;
        treeEndPointY = treeY + treeTrunk.getHeight() * SCALESIZE / 2;
        treeControlPointX = w / 2;
        treeControlPointY = treeY - treeBallMiddle.getWidth() * SCALESIZEBALL / 2;

        startAnimBirds();
        startAnimTree();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        currentOffsetX = maxOffsetX * mProgress;
        canvas.translate(currentOffsetX, 0);
        drawBirds(canvas);
        drawsideHill(canvas);
        drawLeaf(canvas);
        drawTree(canvas, currentOffsetX);
    }

    private void drawBirds(Canvas canvas) {
        if (isDrawBirds) {
            canvas.drawBitmap(bird, null, new RectF(birdMovePointX, birdMovePointY, birdMovePointX + birdsSize, birdMovePointY + birdsSize), null);
            canvas.drawBitmap(bird, null, new RectF(birdMovePointX + offsetBirds, birdMovePointY - offsetBirds / 2, birdMovePointX + birdsSize + offsetBirds, birdMovePointY + birdsSize - offsetBirds / 2), null);
            canvas.drawBitmap(bird, null, new RectF(birdMovePointX + 2 * offsetBirds, birdMovePointY - offsetBirds / 2, birdMovePointX + birdsSize + 2 * offsetBirds, birdMovePointY + birdsSize - offsetBirds / 2), null);
            canvas.drawBitmap(bird, null, new RectF((float) (birdMovePointX + 1.5 * offsetBirds), (float) (birdMovePointY + offsetBirds / 2), (float) (birdMovePointX + birdsSize + 1.5 * offsetBirds), (float) (birdMovePointY + birdsSize + offsetBirds / 2)), null);
            canvas.drawBitmap(bird, null, new RectF(birdMovePointX + 3 * offsetBirds, (float) (birdMovePointY - 1.5 * offsetBirds), birdMovePointX + birdsSize + 3 * offsetBirds, (float) (birdMovePointY + birdsSize - 1.5 * offsetBirds)), null);
        }
    }

    private void drawsideHill(Canvas canvas) {
        canvas.drawBitmap(sidehillLeft, null, mRectFLeft, null);
        canvas.drawBitmap(sidehillRight, null, mRectFRight, null);
    }

    private void drawLeaf(Canvas canvas) {
        if (isDrawTree) {
            int degreeX = (int) (treeBallMiddle.getWidth() * SCALESIZEBALL / 5);
            int degreeY = 20;
            treeLeaf_Matrix = new Matrix();
            treeLeaf_Matrix.setScale(SCALESIZELEAF, SCALESIZELEAF);
            treeLeaf_Matrix.postTranslate(moveLeafX, moveLeafY);
            treeLeaf_Matrix.postRotate((float) (randomRotate + rotateValueLeaf)
                    , moveLeafX + treeLeaf.getWidth() * SCALESIZELEAF / 2, moveLeafY + treeLeaf.getHeight() * SCALESIZELEAF / 2);
            canvas.drawBitmap(treeLeaf, treeLeaf_Matrix, treeLeafPaint);

            treeLeaf_Matrix = new Matrix();
            treeLeaf_Matrix.setScale(SCALESIZELEAF, SCALESIZELEAF);
            treeLeaf_Matrix.postTranslate(moveLeafX + degreeX, moveLeafY + degreeY);
            treeLeaf_Matrix.postRotate((float) (randomRotate + rotateValueLeaf - 30)
                    , moveLeafX + treeLeaf.getWidth() * SCALESIZELEAF / 2 + degreeX, moveLeafY + treeLeaf.getHeight() * SCALESIZELEAF / 2 + degreeY);
            canvas.drawBitmap(treeLeaf, treeLeaf_Matrix, treeLeafPaint);

            treeLeaf_Matrix = new Matrix();
            treeLeaf_Matrix.setScale(SCALESIZELEAF, SCALESIZELEAF);
            treeLeaf_Matrix.postTranslate(moveLeafX + 2 * degreeX, moveLeafY - degreeY);
            treeLeaf_Matrix.postRotate((float) (randomRotate + rotateValueLeaf - 60)
                    , moveLeafX + treeLeaf.getWidth() * SCALESIZELEAF / 2 + 2 * degreeX, moveLeafY + treeLeaf.getHeight() * SCALESIZELEAF / 2 - degreeY);
            canvas.drawBitmap(treeLeaf, treeLeaf_Matrix, treeLeafPaint);

            treeLeaf_Matrix = new Matrix();
            treeLeaf_Matrix.setScale(SCALESIZELEAF, SCALESIZELEAF);
            treeLeaf_Matrix.postTranslate(moveLeafX + 3 * degreeX, moveLeafY + degreeY);
            treeLeaf_Matrix.postRotate((float) (randomRotate + rotateValueLeaf - 90)
                    , moveLeafX + treeLeaf.getWidth() * SCALESIZELEAF / 2 + 3 * degreeX, moveLeafY + treeLeaf.getHeight() * SCALESIZELEAF / 2 + degreeY);
            canvas.drawBitmap(treeLeaf, treeLeaf_Matrix, treeLeafPaint);
        }
    }

    private void drawTree(Canvas canvas, float currentOffsetX) {
        treeTrunk_Matrix = new Matrix();
        treeTrunk_Matrix.setScale(SCALESIZE, SCALESIZE);
        treeTrunk_Matrix.postTranslate(treeX, treeY);

        treeBranch_Matrix = new Matrix();
        treeBranch_Matrix.setScale(SCALESIZE, SCALESIZE);
        treeBranch_Matrix.postTranslate(treeX - treeBranch.getWidth() * SCALESIZE / 2, treeY + treeTrunk.getHeight() * SCALESIZE / 8);

        treeBallMiddle_Matrix = new Matrix();
        treeBallMiddle_Matrix.setScale(SCALESIZEBALL, SCALESIZEBALL);
        treeBallMiddle_Matrix.postTranslate(treeX - treeBallMiddle.getWidth() / 2 * SCALESIZEBALL
                , treeY - treeBallMiddle.getWidth() * SCALESIZEBALL + 10);

        float offsetXY = getoffsetXY(treeBallLeft.getWidth() * SCALESIZEBALL / 2);
        treeBallLeft_Matrix = new Matrix();
        treeBallLeft_Matrix.setScale(SCALESIZEBALL, SCALESIZEBALL);
        treeBallLeft_Matrix.postTranslate(treeX - treeBranch.getWidth() * SCALESIZE / 2 - treeBallLeft.getWidth() * SCALESIZEBALL + offsetXY
                , treeY + treeTrunk.getHeight() * SCALESIZE / 8 - treeBallLeft.getHeight() * SCALESIZEBALL + offsetXY);

        offsetXY = getoffsetXY(treeBallRight.getWidth() * SCALESIZEBALL / 2) +
                CalculateUtil.getPythagorean(treeBranch.getWidth() * SCALESIZE / 2, treeBranch.getHeight() * SCALESIZE) / 4;
        treeBallRight_Matrix = new Matrix();
        treeBallRight_Matrix.setScale(SCALESIZEBALL, SCALESIZEBALL);
        treeBallRight_Matrix.postTranslate(treeX + treeBranch.getWidth() * SCALESIZE / 2 - offsetXY
                , treeY + treeTrunk.getHeight() * SCALESIZE / 8 - treeBallRight.getHeight() * SCALESIZEBALL + offsetXY);

        treeBallLeft_Matrix.postRotate(rotateValueTree, rotateCenterX, rotateCenterY);
        treeBallRight_Matrix.postRotate(rotateValueTree, rotateCenterX, rotateCenterY);
        treeBallMiddle_Matrix.postRotate(rotateValueTree, rotateCenterX, rotateCenterY);
        treeBranch_Matrix.postRotate(rotateValueTree, rotateCenterX, rotateCenterY);
        canvas.drawBitmap(treeTrunk, treeTrunk_Matrix, null);
        canvas.drawBitmap(treeBranch, treeBranch_Matrix, null);
        canvas.drawBitmap(treeBallLeft, treeBallLeft_Matrix, null);
        canvas.drawBitmap(treeBallMiddle, treeBallMiddle_Matrix, null);
        canvas.drawBitmap(treeBallRight, treeBallRight_Matrix, null);
    }

    private float getoffsetXY(float radius) {
        double clinodiagonal = radius * Math.sqrt(2);
        double offsetxy = (clinodiagonal - radius) * Math.sin(45 * Math.PI / 180);
        return (float) offsetxy;
    }

    private void startAnimTree() {
        valueAnimatorTree = new ValueAnimator().ofFloat(0, 15);
        valueAnimatorTree.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                rotateValueTree = 360 - (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimatorTree.addPauseListener(new Animator.AnimatorPauseListener() {
            @Override
            public void onAnimationPause(Animator animation) {
                rotateValueTree = 0;
                isDrawTree = true;
                startAnimLeaf();
            }

            @Override
            public void onAnimationResume(Animator animation) {

            }
        });
        valueAnimatorTree.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                ++timesTree;
                if (timesTree % 2 == 1)
                    valueAnimatorTree.pause();
            }
        });
        valueAnimatorTree.setDuration(2000);
        valueAnimatorTree.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimatorTree.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimatorTree.setInterpolator(new LinearInterpolator());
        valueAnimatorTree.start();


    }

    private void startAnimLeaf() {
        randomRotate = Math.random() * 90;
        BezierEvaluator bezierEvaluator = new BezierEvaluator(new PointF(treeControlPointX, treeControlPointY));
        final ValueAnimator anim = ValueAnimator.ofObject(bezierEvaluator,
                new PointF(treeStartPointX, treeStartPointY),
                new PointF(treeEndPointX, treeEndPointY));
        anim.setDuration(4000);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PointF point = (PointF) valueAnimator.getAnimatedValue();
                moveLeafX = point.x;
                moveLeafY = point.y;
                float fraction = valueAnimator.getAnimatedFraction();
                rotateValueLeaf = 90 * fraction;
                if (fraction >= 0.75 && valueAnimatorAlpha.isRunning() == false) {
                    startAlpha();
                }
                invalidate();
            }
        });
        anim.setInterpolator(new LinearInterpolator());
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isDrawTree = false;
                valueAnimatorTree.resume();
                invalidate();
            }
        });
        anim.start();
    }

    private void startAlpha() {
        valueAnimatorAlpha = ValueAnimator.ofFloat(0, 1);
        valueAnimatorAlpha.setDuration(1000);
        valueAnimatorAlpha.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float fraction = (float) valueAnimator.getAnimatedValue();
                treeLeafPaint.setAlpha((int) (255 - 255 * fraction));
            }
        });
        valueAnimatorAlpha.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                treeLeafPaint.reset();
            }
        });
        valueAnimatorAlpha.setInterpolator(new LinearInterpolator());
        valueAnimatorAlpha.start();
    }

    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                      int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }

    private void startAnimBirds() {
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
                if (birdTimes % 8 == 0) {
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
                birdTimes = 0;
            }
        });
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.start();
    }

    private void updatebirds(int birdTimes) {
        bird = birdsList.get(birdTimes % 16);
    }

    class ThreadBirds extends Thread {

        @Override
        public void run() {
            for (int i = 0; i < 15; ++i) {
                birdsList.add(i, BitmapFactory.decodeResource(getResources(), BitmapUtil.getBirdsResource(i + 1)));
            }
        }
    }

    public void setSensorChangedFraction(float v) {
        mProgress = v;
        invalidate();
    }

    public void setGyroscopeObserver(GyroscopeObserver observer) {
        if (observer != null) {
            observer.addWeatherAnimView(this);
        }
    }

    public void stopAnim() {

    }
}
