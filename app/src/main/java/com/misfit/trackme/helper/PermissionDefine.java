package com.misfit.trackme.helper;

import android.Manifest;

/**
 * Created by VinhLe on Jun, 2018.
 */
public enum PermissionDefine
{
    // Permissions
    ACCESS_FINE_LOCATION(Manifest.permission.ACCESS_FINE_LOCATION, 0x123),
    ACCESS_COARSE_LOCATION(Manifest.permission.ACCESS_COARSE_LOCATION, 0x124);


    final String mPermission;
    final int mRequestCode;
    PermissionDefine(String permission, int requestCode)
    {
        this.mPermission = permission;
        this.mRequestCode = requestCode;
    }

    public String getPermission()
    {
        return mPermission;
    }

    public int getRequestCode()
    {
        return mRequestCode;
    }
}
