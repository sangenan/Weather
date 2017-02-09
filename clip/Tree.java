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
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.sangenan.weather.Util.BezierEvaluator;
import com.example.sangenan.weather.Util.CalculateUtil;

/**
 * Created by kuwakuzukusunoki on 2017/2/2.
 */

public class Tree extends View {
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


    public Tree(Context context) {
        super(context);
        init();
    }

    public Tree(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

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
        startAnimTree();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLeaf(canvas);
        drawTree(canvas);

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

    private void drawTree(Canvas canvas) {
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

    public void startAnimTree() {
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

    public void startAnimLeaf() {
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
                float fraction =valueAnimator.getAnimatedFraction();
                rotateValueLeaf = 90 * fraction;
                if (fraction>=0.75 && valueAnimatorAlpha.isRunning()== false){
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
    private void startAlpha(){
        valueAnimatorAlpha = ValueAnimator.ofFloat(0,1);
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
                treeLeafPaint = new Paint();
            }
        });
        valueAnimatorAlpha.setInterpolator(new LinearInterpolator());
        valueAnimatorAlpha.start();
    }
}
