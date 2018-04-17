package com.sc.clgg.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;

import com.sc.clgg.config.ConstantValue;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 图片处理
 *
 * @author ZhangYi 2014-4-1 上午10:44:02
 */
public class ImgUtil {

    private static ImgUtil mImgUtil;

    public static ImgUtil getInstance() {
        if (null == mImgUtil) {
            mImgUtil = new ImgUtil();
        }
        return mImgUtil;
    }

    /**
     * 高斯模糊
     *
     * @author ZhangYi 2014-4-18 16:33:04
     */
    public Bitmap convertImg2Blur(Bitmap bitmap) {
        // 高斯矩阵
        int[] gauss = new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1};
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixColor = 0;
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int delta = 20; // 值越小图片会越亮，越大则越暗
        int idx = 0;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        for (int i = 1, length = height - 1; i < length; i++) {
            for (int k = 1, len = width - 1; k < len; k++) {
                idx = 0;
                for (int m = -1; m <= 1; m++) {
                    for (int n = -1; n <= 1; n++) {
                        pixColor = pixels[(i + m) * width + k + n];
                        pixR = Color.red(pixColor);
                        pixG = Color.green(pixColor);
                        pixB = Color.blue(pixColor);
                        newR = newR + pixR * gauss[idx];
                        newG = newG + pixG * gauss[idx];
                        newB = newB + pixB * gauss[idx];
                        idx++;
                    }
                }
                newR /= delta;
                newG /= delta;
                newB /= delta;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBmp;
    }

    /**
     * 把图片切成正方形的
     *
     * @author TangWei 2013-12-10下午5:16:18
     */
    public Bitmap cutSquareBitmap(Bitmap bitmap) {
        try {
            Bitmap result;
            int w = bitmap.getWidth();// 输入长方形宽
            int h = bitmap.getHeight();// 输入长方形高
            int nw;// 输出正方形宽
            if (w > h) { // 宽大于高
                nw = h;
                result = Bitmap.createBitmap(bitmap, (w - nw) / 2, 0, nw, nw);
            } else {// 高大于宽
                nw = w;
                result = Bitmap.createBitmap(bitmap, 0, (h - nw) / 2, nw, nw);
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    /**
     * 存图片到sdcard
     *
     * @author Michael.Zhang
     */
    public void savaBitmap2SDCard(Bitmap bitmap, String img_name) throws Exception {
        File file = new File(ConstantValue.SDCARD_PATH);
        if (!file.exists()) {
            file.mkdir();
        }

        File imageFile = new File(file, img_name);
        imageFile.createNewFile();
        FileOutputStream fos = new FileOutputStream(imageFile);
        /* 尺寸修改 */
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width >= ConstantValue.SCREEN_WIDHT * 2 / 3) {
            /* 图片尺寸大于当前屏幕宽度 */
            int newWidth = ConstantValue.SCREEN_WIDHT * 2 / 3;
            int newHeight = newWidth * height / width;
            bitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        }

		/* 质量修改 保存 */
        bitmap.compress(CompressFormat.JPEG, 90, fos);
        fos.flush();
        fos.close();
    }

    /**
     * 尺寸比例 压缩图片
     *
     * @param newWidth 新的宽度,根据此宽度计算高度
     * @return
     */
    public Bitmap scaleImg(Bitmap bm, int newWidth) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        int newHeight = newWidth * height / width;

        return Bitmap.createScaledBitmap(bm, newWidth, newHeight, false);
    }
}
