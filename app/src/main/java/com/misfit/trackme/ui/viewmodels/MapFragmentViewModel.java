package com.misfit.trackme.ui.viewmodels;

import android.content.Context;

import com.misfit.trackme.repository.ILocationRepository;
import com.misfit.trackme.repository.LocationRepository;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by VinhLe on Jun, 2018.
 */
public class MapFragmentViewModel extends BaseViewModel implements IMapFragmentViewModel
{

    private ILocationRepository mILocationRepository;
    private ILocationRepository getLocationRepository()
    {
        if (mILocationRepository == null)
        {
            mILocationRepository = new LocationRepository(getContext());
        }
        return mILocationRepository;
    }

    public MapFragmentViewModel(Context context)
    {
        super(context);
    }

    @Override
    public void getLocations(Subscriber subscriber, int sessionId)
    {
        getLocationRepository().findLocationsBySessionId(sessionId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
