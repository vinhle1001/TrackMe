package com.misfit.trackme.ui.viewmodels;

import android.content.Context;

/**
 * Created by VinhLe on Jun, 2018.
 */
public abstract class BaseViewModel
{
    private Context mContext;

    public BaseViewModel(Context context) {
        this.mContext = context;
    }

    public Context getContext() {
        return mContext;
    }
}
