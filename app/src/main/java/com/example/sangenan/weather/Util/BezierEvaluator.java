package com.example.sangenan.weather.Util;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

public class BezierEvaluator implements TypeEvaluator<PointF> {

    private PointF mControlPoint1;

    public BezierEvaluator(PointF mControlPoint1) {
        this.mControlPoint1 = mControlPoint1;
    }

    @Override
    public PointF evaluate(float t, PointF startValue, PointF endValue) {
        return BezierUtil.CalculateBezierPointForQuadratic(t, startValue, mControlPoint1,endValue);
    }
}