package com.misfit.trackme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.misfit.trackme.helper.PermissionDefine;
import com.misfit.trackme.helper.PermissionHelper;
import com.misfit.trackme.service.LocationService;
import com.misfit.trackme.ui.fragment.MapFragment;

public class HomeActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        PermissionHelper.requestPermission(this, PermissionDefine.ACCESS_FINE_LOCATION.getPermission(), PermissionDefine.ACCESS_FINE_LOCATION.getRequestCode());
        PermissionHelper.requestPermission(this, PermissionDefine.ACCESS_COARSE_LOCATION.getPermission(), PermissionDefine.ACCESS_COARSE_LOCATION.getRequestCode());

        Intent serviceIntent = new Intent(this, LocationService.class);
        startService(serviceIntent);
    }

    @Override
    protected void onStart()
    {
        super.onStart();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, new MapFragment()).commit();
    }
}
