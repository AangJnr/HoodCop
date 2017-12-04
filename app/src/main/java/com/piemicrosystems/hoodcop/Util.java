package com.piemicrosystems.hoodcop;

/**
 * Created by aangjnr on 14/11/2017.
 */


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v4.util.Pair;
import android.view.Display;
import android.view.WindowManager;

import java.util.HashMap;

/**
 * Created by Arcane on 7/24/2017.
 */

public class Util {

    private static final Integer PERMISSION_REQUEST_CODE = 9999;
    private static final String TAG = Util.class.getSimpleName();
    private static Integer permissionStringRes;
    private static HashMap<Integer, Pair<Pair<Activity, OnPermissionCallback>, String[]>> permissionMap = new HashMap<>();
    private static int screenWidth = 0;
    private static int screenHeight = 0;

    public static void checkPermission(Activity activity, String[] permissions,
                                       OnPermissionCallback permissionsResultCallback) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean req = false;

            for (int i = 0; i < permissions.length; i++) {
                if (activity.checkSelfPermission(permissions[i]) == PackageManager.PERMISSION_DENIED) {
                    req = true;
                }
            }
            if (!req)
                permissionsResultCallback.onPermissionGranted();
            else {
                permissionMap.clear();

                permissionStringRes = R.string.pleaseEnablePermission;
                permissionMap.put(PERMISSION_REQUEST_CODE, new Pair<>(new Pair<>(activity,
                        permissionsResultCallback), permissions));

                activity.requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        } else {
            permissionsResultCallback.onPermissionGranted();
        }
    }

    public static int dipsToPixels(Context ctx, float dips) {
        final float scale = ctx.getResources().getDisplayMetrics().density;
        int px = (int) (dips * scale + 0.5f);
        return px;
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;


    }

    public static boolean checkInternetConnection(Context context) {


        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // ARE WE CONNECTED TO THE NET
        return conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected();

    }


    @SuppressWarnings("MissingPermission")
    public interface OnPermissionCallback {
        void onPermissionGranted();

        void onPermissionDenied();
    }


}