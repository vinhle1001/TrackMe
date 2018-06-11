package com.misfit.trackme.service;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.misfit.trackme.database.AppData;
import com.misfit.trackme.database.dto.LocationDto;
import com.misfit.trackme.helper.DistanceHelper;
import com.misfit.trackme.helper.LoggerHelper;
import com.misfit.trackme.helper.PermissionHelper;
import com.misfit.trackme.ui.fragment.MapFragment;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by VinhLe on Jun, 2018.
 */
public class LocationService extends Service
{
    static final String TAG = "LocationService";
    static final long NOTIFY_INTERVAL = 15 * 1000L;     // 15s

    public static final String IntentFilter = "com.misfit.trackme.service.LocationService.REQUEST_SUCCESS";

    private LocationManager mLocationManager;
    private Location mLocation;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;

    private double mLatitude = -1;
    private double mLongitude = -1;

    private int mSessionId = -1;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        LoggerHelper.d(TAG, "OnStartCommand");

        if (intent != null)
        {
            mSessionId = intent.getIntExtra(MapFragment.SESSION_ID_KEY, 0);
        }

        if (mSessionId > 0)
        {
            requestLocation();
            mTimer = new Timer();
            mTimer.schedule(new TimerTaskToGetLocation(), 0, NOTIFY_INTERVAL);
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        LoggerHelper.d(TAG, "OnCreate");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        LoggerHelper.d(TAG, "onTaskRemoved");
//        forceStop();
//        requestLocation();
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy()
    {
        LoggerHelper.d(TAG, "onDestroy");
        forceStop();
        requestLocation();
        super.onDestroy();
    }

    private void forceStop()
    {
        if (mTimer != null)
        {
            mTimer.cancel();
        }
    }

    private void getCurrentLocation()
    {
        boolean isPermissionEnable = PermissionHelper.checkPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (!isPermissionEnable)
        {
            LoggerHelper.d(TAG, "Don't enable permission!");
            return;
        }

        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        boolean isGPSEnable = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnable = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnable && !isNetworkEnable)
        {
            LoggerHelper.d(TAG, "Don't enable both GPS and Network!");
        }
        else
        {
            if (isNetworkEnable)
            {
                mLocation = null;
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, new LocationListener());
                if (mLocationManager != null)
                {
                    mLocation = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (mLocation != null)
                    {
                        updateLocation(mLocation);
                        mLatitude = mLocation.getLatitude();
                        mLongitude = mLocation.getLongitude();
                    }
                }
            }

            if (isGPSEnable)
            {
                mLocation = null;
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, new LocationListener());
                if (mLocationManager != null)
                {
                    mLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (mLocation != null)
                    {
                        updateLocation(mLocation);
                        mLatitude = mLocation.getLatitude();
                        mLongitude = mLocation.getLongitude();
                    }
                }
            }
        }
    }

    private void updateLocation(Location location)
    {
        double distance = -1;
        if (mLatitude == -1 && mLongitude == -1)
        {

        }
        else
        {
            distance = DistanceHelper.getDistanceFromLatLonInKm(location.getLatitude(), location.getLongitude(), mLatitude, mLongitude);
        }
        insertLocation(mSessionId, location.getLatitude(), location.getLongitude(), distance);
    }

    private void sendBroadcast()
    {
        Intent intent = new Intent(IntentFilter);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void requestLocation()
    {
        mHandler.post(new Runnable()
        {
            @Override
            public void run()
            {
                getCurrentLocation();
            }
        });
    }

    private class TimerTaskToGetLocation extends TimerTask
    {
        @Override
        public void run()
        {
            requestLocation();
        }
    }

    private class LocationListener implements android.location.LocationListener
    {
        @Override
        public void onLocationChanged(Location location)
        {
            LoggerHelper.d(TAG, "onLocationChanged with " + location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            LoggerHelper.d(TAG, "onStatusChanged with " + provider);
        }

        @Override
        public void onProviderEnabled(String provider)
        {
            LoggerHelper.d(TAG, "onProviderEnabled with " + provider);
        }

        @Override
        public void onProviderDisabled(String provider)
        {
            LoggerHelper.d(TAG, "onProviderDisabled with " + provider);
        }
    }

    private void insertLocation(final int sessionId, final double latitude, final double longitude, final double distance)
    {
        Thread insertThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                LocationDto locationDto = new LocationDto();
                locationDto.setSessionId(sessionId);
                locationDto.setLatitude(latitude);
                locationDto.setLongitude(longitude);
                locationDto.setIsStarted(distance == -1 ? 1 : 0);
                locationDto.setSpeed(distance == -1 ? 0 : distance * 1000 / NOTIFY_INTERVAL);
                locationDto.setCreatedTime((new Date().getTime()));

                AppData.getInstance(getApplicationContext()).locationDao().insert(locationDto);

                sendBroadcast();
                LoggerHelper.d(TAG, "insertLocation successful");
            }
        });

        insertThread.start();
    }
}
