package com.misfit.trackme.repository;

import android.content.Context;

import com.misfit.trackme.database.dto.LocationDto;

import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;

/**
 * Created by VinhLe on Jun, 2018.
 */
public class LocationRepository extends BaseRepository implements ILocationRepository
{
    public LocationRepository(Context context)
    {
        super(context);
    }

    @Override
    public Observable<List<LocationDto>> findLocationsBySessionId(final int sessionId)
    {
        return Observable.fromCallable(new Callable<List<LocationDto>>()
        {
            @Override
            public List<LocationDto> call() throws Exception
            {
                return getAppData().locationDao().findLocationsBySessionId(sessionId);
            }
        });
    }
}
