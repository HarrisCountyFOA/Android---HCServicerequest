package servicesequest.hctx.net.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class AppPermission {

    static int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private static boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }


    public static boolean checkLocationPermission(Context context) {
        if (shouldAskPermission()) {
            int loc = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION);
            int loc2 = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION);
            List<String> listPermissionsNeeded = new ArrayList<>();
            if (loc2 != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (loc != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            }
            return true;
        }
        return true;
    }

    public static boolean checkCameraPermission(Context context) {
        if (shouldAskPermission()) {
            int camera = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA);
            int storage = ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            List<String> listPermissionsNeeded = new ArrayList<>();

            if (camera != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
            }
           // if (storage != PackageManager.PERMISSION_GRANTED) {
               // listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //}
            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions((Activity) context, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
                return false;
            }
            return true;
        }
        return true;
    }
}
