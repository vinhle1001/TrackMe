package com.misfit.trackme.repository;

import android.content.Context;

import com.misfit.trackme.database.AppData;

/**
 * Created by VinhLe on Jun, 2018.
 */
public abstract class BaseRepository
{
    private Context mContext;

    public BaseRepository(Context context)
    {
        this.mContext = context;
    }

    public Context getContext()
    {
        return mContext;
    }

    protected AppData getAppData()
    {
        if (mContext != null)
        {
            return AppData.getInstance(mContext);
        }
        return null;
    }
}
