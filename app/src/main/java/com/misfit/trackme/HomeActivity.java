package com.misfit.trackme;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class HomeActivity extends AppCompatActivity
{
    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(mOnMapReadyCallback);
    }

    private OnMapReadyCallback mOnMapReadyCallback = new OnMapReadyCallback()
    {
        @Override
        public void onMapReady(GoogleMap googleMap)
        {
            mGoogleMap = googleMap;

            // Add a marker in Sydney, Australia, and move the camera.
            LatLng sydney = new LatLng(-34, 151);
            mGoogleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    };
}
