package com.misfit.trackme.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.misfit.trackme.service.LocationService;

/**
 * Created by VinhLe on Jun, 2018.
 */
public class MapFragment extends SupportMapFragment
{

    private GoogleMap mGoogleMap;
    private LatLng mLastLatLng;

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);

        getMapAsync(mOnMapReadyCallback);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        // Register broadcast
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                mLocationReceiver,
                new IntentFilter(LocationService.IntentFilter)
        );
    }

    @Override
    public void onPause()
    {
        super.onPause();
        // Unregister broadcast
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mLocationReceiver);
    }

    private void moveTo(double latitude, double longitude)
    {
        if (mGoogleMap != null)
        {
            LatLng position = new LatLng(latitude, longitude);

            if (mLastLatLng == null)
            {
                mGoogleMap.addMarker(new MarkerOptions().position(position).title("You're in here!"));
            }
            else
            {
                mGoogleMap.addPolyline(new PolylineOptions().add(mLastLatLng, position).width(5).color(Color.RED));
            }
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 18.0f));
            mLastLatLng = position;
        }
    }

    private OnMapReadyCallback mOnMapReadyCallback = new OnMapReadyCallback()
    {
        @Override
        public void onMapReady(GoogleMap googleMap)
        {
            mGoogleMap = googleMap;
        }
    };

    private BroadcastReceiver mLocationReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            double latitude = intent.getDoubleExtra(LocationService.Latitude, 0);
            double longitude = intent.getDoubleExtra(LocationService.Longitude, 0);

            moveTo(latitude, longitude);
        }
    };
}
