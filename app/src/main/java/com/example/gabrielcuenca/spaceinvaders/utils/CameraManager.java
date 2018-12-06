package com.example.gabrielcuenca.spaceinvaders.utils;

import android.content.Context;
import android.content.pm.PackageManager;

public final class CameraManager {

    public static final int REQUEST_IMAGE_CAPTURE = 1;


    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            return false;
        }
    }

}
