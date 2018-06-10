package com.misfit.trackme.repository;

import com.misfit.trackme.database.dto.LocationDto;

import java.util.List;

import rx.Observable;

public interface ILocationRepository
{
    Observable<List<LocationDto>> findLocationsBySessionId(int sessionId);
}
