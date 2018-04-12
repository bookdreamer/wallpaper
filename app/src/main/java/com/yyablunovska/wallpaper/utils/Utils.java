package com.yyablunovska.wallpaper.utils;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.yyablunovska.wallpaper.R;

import java.io.IOException;

/**
 * @author yuliia.yablunovska on 3/10/18.
 */

public class Utils {

    public static final String BASE_API_URL = "https://api.unsplash.com/";
    public static final String APPLICATION_ID = "1669d967a1434f46c678e6eec826dcab64ab2f4bcbbbe09689f72c7f466436f9";

    private static final String TAG = Utils.class.getSimpleName();

    public static void changeMainFragment(FragmentActivity activity, Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .commit();
    }

    public static void changeMainFragmentWithBack(FragmentActivity activity, Fragment fragment) {
        activity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public static boolean setWallpaper(Activity activity, Bitmap bitmap) {
        final WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);
        try {
            wallpaperManager.setBitmap(bitmap);
            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error on setting wallpaper: " + e);
        }
        return false;
    }
}
