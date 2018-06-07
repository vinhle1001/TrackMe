package com.misfit.trackme.helper;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by VinhLe on Jun, 2018.
 */
public final class PermissionHelper
{

    /**
     * @param context
     * @param permission
     * Helper to check permission is granted
     */
    public static boolean checkPermission(Context context, @NonNull String permission)
    {
        // Return when activity is null
        if (context == null)
        {
            return false;
        }

        return ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED;
    }

    /**
     * @param activity
     * @param permission
     * @param requestCode
     * Helper to request permission
     */
    public static void requestPermission(Activity activity, @NonNull String permission, int requestCode)
    {
        // Return when activity is null
        if (activity == null)
        {
            return;
        }

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED)
        {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS))
            {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            }
            else
            {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        else
        {
            // Permission has already been granted
        }
    }

}
