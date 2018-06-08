package com.misfit.trackme.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

/**
 * Created by VinhLe on Jun, 2018.
 */
@Dao
public interface BaseDao<T>
{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insert(T... repos);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int update(T... repos);

    @Delete
    int delete(T... repos);

}
