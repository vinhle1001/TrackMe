package com.misfit.trackme.repository;

import com.misfit.trackme.database.dto.SessionDto;

import java.util.List;

import rx.Observable;

public interface ISessionRepository
{
    Observable<List<SessionDto>> findAll();

    Observable<SessionDto> createSession();
}
