package com.sc.clgg.tool.helper;

import android.net.Uri;
import android.os.Environment;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.TakePhotoOptions;

import java.io.File;

/**
 * @author：lvke
 * @date：2018/10/23 18:18
 */
public final class TakePhotoHelper {

    private Uri getOutPutUri(){
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return Uri.fromFile(file);
    }

    /**
     * 相机 + 不剪裁
     */
    public void onPikeByTake(TakePhoto takePhoto) {
        configOption(takePhoto);
        takePhoto.onPickFromCapture(getOutPutUri());
    }

    /**
     * 图库 + 不剪裁
     */
    public void onPickBySelect(TakePhoto takePhoto, int limit) {
        configOption(takePhoto);
        takePhoto.onPickMultiple(limit);
    }

    /**
     * 相机 + 剪裁
     */
    public void onPickFromCaptureWithCrop(TakePhoto takePhoto) {
        configOption(takePhoto);
        takePhoto.onPickFromCaptureWithCrop(getOutPutUri(), getCropOptions());
    }

    /**
     * 图库 + 剪裁
     */
    public void onPickMultipleWithCrop(TakePhoto takePhoto, int limit) {
        configOption(takePhoto);
        if (limit > 1) {
            takePhoto.onPickMultipleWithCrop(limit, getCropOptions());
        } else {
            takePhoto.onPickFromGalleryWithCrop(getOutPutUri(), getCropOptions());
        }
    }


    public void configOption(TakePhoto takePhoto) {
        configCompress(takePhoto);

        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(true);
        takePhoto.setTakePhotoOptions(builder.create());
    }

    /**
     *
     * @return TakePhoto压缩配置
     */
    private void configCompress(TakePhoto takePhoto) {
        CompressConfig config;
        config = new CompressConfig.Builder().setMaxSize(102400)
                .setMaxPixel(800)
                .enableReserveRaw(true)
                .create();
        takePhoto.onEnableCompress(config, true);
    }


    /**
     *
     * @return TakePhoto剪裁配置
     */
    private CropOptions getCropOptions() {
        int height = 800;
        int width = 800;

        CropOptions.Builder builder = new CropOptions.Builder();
        //宽/高
        builder.setAspectX(width).setAspectY(height);
        //宽x高
        //builder.setOutputX(width).setOutputY(height);
        builder.setWithOwnCrop(false);
        return builder.create();
    }

}
