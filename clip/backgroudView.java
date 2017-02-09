package com.example.sangenan.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by kuwakuzukusunoki on 2017/1/31.
 */

public class backgroudView extends ImageView {
    private int Width;
    private int Height;
    private float scaleBackgroud;
    private float scaleSideHill;
    private Bitmap sidehillLeft;
    private Bitmap sidehillRight;
    private RectF mRectFLeft;
    private RectF mRectFRight;
    private static final int maxOffsetX = 50;
    private Bitmap backgroud;
    private float currentOffsetX;
    private float mProgress;

    public backgroudView(Context context) {
        super(context);
        init();
    }

    public backgroudView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    private void init() {
        currentOffsetX = 0;
        mProgress = 0;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Width = w;
        Height = h;
        scaleBackgroud = w / h;
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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        currentOffsetX = maxOffsetX * mProgress;
        canvas.translate(currentOffsetX,0);
        canvas.drawBitmap(sidehillLeft, null, mRectFLeft, null);
        canvas.drawBitmap(sidehillRight, null, mRectFRight, null);

    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
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

    public void setSensorChangedFraction(float v) {
        Log.d("tag", "fraction" + v);
        mProgress =v;
        invalidate();
    }

}
