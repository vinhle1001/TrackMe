package com.misfit.trackme.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.misfit.trackme.database.dto.SessionDto;

import java.util.List;

/**
 * Created by VinhLe on Jun, 2018.
 */
@Dao
public interface SessionDao extends BaseDao<SessionDto>
{

    @Query("SELECT * FROM sessions ORDER BY id")
    List<SessionDto> findAll();

}
