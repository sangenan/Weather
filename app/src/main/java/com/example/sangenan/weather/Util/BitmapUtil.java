package com.example.sangenan.weather.Util;

import android.content.res.Resources;
import android.graphics.BitmapFactory;

import com.example.sangenan.weather.R;

/**
 * Created by kuwakuzukusunoki on 2017/1/29.
 */

public class BitmapUtil {
    public static int getBirdsResource(int times){
        switch (times % 16){
            case 1:
                return R.drawable.blue01;
            case 2:
                return R.drawable.blue02;
            case 3:
                return R.drawable.blue03;
            case 4:
                return R.drawable.blue04;
            case 5:
                return R.drawable.blue05;
            case 6:
                return R.drawable.blue06;
            case 7:
                return R.drawable.blue07;
            case 8:
                return R.drawable.blue08;
            case 9:
                return R.drawable.blue09;
            case 10:
                return R.drawable.blue10;
            case 11:
                return R.drawable.blue11;
            case 12:
                return R.drawable.blue12;
            case 13:
                return R.drawable.blue13;
            case 14:
                return R.drawable.blue14;
            case 15:
                return R.drawable.blue15;
            case 16:
                return R.drawable.blue16;
            default:
                return R.drawable.blue01;
        }
    }
    public static float getBitmapScale(Resources res,int id){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, id, options);
        if (options.outHeight != 0)
            return options.outWidth / options.outHeight;
        return 0;
    }
}
