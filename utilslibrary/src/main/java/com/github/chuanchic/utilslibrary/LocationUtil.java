package com.github.chuanchic.utilslibrary;

import android.Manifest;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

import java.util.List;

/**
 * 位置工具类
 */
public class LocationUtil {

    /**
     * 获取经纬度
     */
    public static double[] getLngAndLat(Context context) {
        String[] permissions = new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};
        double[] lngAndLat = {0, 0};

        List<String> deniedPermissions = PermissionUtil.getDeniedPermissions(context, permissions);
        if(deniedPermissions.size() == 0) {//权限已授予
            try {
                LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                if(locationManager != null){
                    if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){//从网络获取
                        if(NetworkUtil.networkEnable(context)){//网络可用
                            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null) {
                                lngAndLat[0] = location.getLongitude();
                                lngAndLat[1] = location.getLatitude();
                            }
                        }
                    }
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {//从gps获取
                        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null) {
                            lngAndLat[0] = location.getLongitude();
                            lngAndLat[1] = location.getLatitude();
                        }
                    }

                }
            }catch (SecurityException e){
                e.printStackTrace();
            }
        }

        return lngAndLat;
    }

}
