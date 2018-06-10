package com.misfit.trackme.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.misfit.trackme.database.dto.LocationDto;

import java.util.List;

/**
 * Created by VinhLe on Jun, 2018.
 */
@Dao
public interface LocationDao extends BaseDao<LocationDto>
{

    @Query("SELECT * FROM locations WHERE session_id = :sessionId ORDER BY created_time")
    List<LocationDto> findLocationsBySessionId(int sessionId);

    @Query("SELECT * FROM locations ORDER BY session_id, created_time")
    List<LocationDto> findAll();
}
