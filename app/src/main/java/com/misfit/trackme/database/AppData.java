package com.misfit.trackme.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.misfit.trackme.database.dao.LocationDao;
import com.misfit.trackme.database.dao.SessionDao;
import com.misfit.trackme.database.dto.LocationDto;
import com.misfit.trackme.database.dto.SessionDto;

/**
 * Created by VinhLe on Jun, 2018.
 */
@Database(entities = { LocationDto.class, SessionDto.class }, version = 1)
public abstract class AppData extends RoomDatabase
{

    private static final String DB_NAME = "track_me.db";
    private static volatile AppData instance;

    public static synchronized AppData getInstance(Context context)
    {
        if (instance == null)
        {
            instance = create(context);
        }
        return instance;
    }

    private static AppData create(final Context context)
    {
        return Room.databaseBuilder(
                context,
                AppData.class,
                DB_NAME).build();
    }

    // Daos
    public abstract LocationDao locationDao();
    public abstract SessionDao sessionDao();
}
