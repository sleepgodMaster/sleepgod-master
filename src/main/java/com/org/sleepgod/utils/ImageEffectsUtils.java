package com.org.sleepgod.utils;
//版权归bearever所有

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

//图片的饱和度，对比度，亮度调节！！
public class ImageEffectsUtils {
  private static int imgHeight,imgWidth;

  //设置饱和度
  public static Bitmap Saturation(Bitmap map, float s){//s的值在0-1

    imgHeight = map.getHeight();
    imgWidth = map.getWidth();
    Bitmap bmp = Bitmap.createBitmap(imgWidth, imgHeight,
      Config.ARGB_8888);
    ColorMatrix cMatrix = new ColorMatrix();
    // 设置饱和度  .
    cMatrix.setSaturation(s);
    Paint paint = new Paint();
    paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));
    Canvas canvas = new Canvas(bmp);
    // 在Canvas上绘制一个已经存在的Bitmap。这样，dstBitmap就和srcBitmap一摸一样了
    canvas.drawBitmap(map, 0, 0, paint);
    return bmp;
  }

  //设置对比度
  public static Bitmap Contrast(Bitmap map, float c){//c的值0-1
    imgHeight = map.getHeight();
    imgWidth = map.getWidth();
    Bitmap bmp = Bitmap.createBitmap(imgWidth, imgHeight,
      Config.ARGB_8888);
    float contrast = c;
    ColorMatrix cMatrix = new ColorMatrix();
    cMatrix.set(new float[] { contrast, 0, 0, 0, 0, 0,
      contrast, 0, 0, 0,// 改变对比度
      0, 0, contrast, 0, 0, 0, 0, 0, 1, 0 });

    Paint paint = new Paint();
    paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

    Canvas canvas = new Canvas(bmp);
    // 在Canvas上绘制一个已经存在的Bitmap。这样，dstBitmap就和srcBitmap一摸一样了
    canvas.drawBitmap(map, 0, 0, paint);
    return bmp;
  }
  //设置亮度
  public static Bitmap Brightness(Bitmap map, int b){//b的值正数变亮，负数变暗

    imgHeight = map.getHeight();
    imgWidth = map.getWidth();
    Bitmap bmp = Bitmap.createBitmap(imgWidth, imgHeight,
      Config.ARGB_8888);
    int brightness = b;
    ColorMatrix cMatrix = new ColorMatrix();
    cMatrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1,
      0, 0, brightness,// 改变亮度
      0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });

    Paint paint = new Paint();
    paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

    Canvas canvas = new Canvas(bmp);
    // 在Canvas上绘制一个已经存在的Bitmap。这样，dstBitmap就和srcBitmap一摸一样了
    canvas.drawBitmap(map, 0, 0, paint);
    return bmp ;
  }


}
