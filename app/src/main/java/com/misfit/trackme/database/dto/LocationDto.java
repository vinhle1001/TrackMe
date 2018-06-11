package com.misfit.trackme.database.dto;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by VinhLe on Jun, 2018.
 */
@Entity(tableName = "locations")
public class LocationDto
{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "is_started")
    private int mIsStarted; // 1: started, 0: none

    @ColumnInfo(name = "latitude")
    private double mLatitude;

    @ColumnInfo(name = "longitude")
    private double mLongitude;

    @ColumnInfo(name = "distance")
    private double mDistance;

    @ColumnInfo(name = "speed")
    private double mSpeed;

    @ColumnInfo(name = "created_time")
    private long mCreatedTime;

    @ColumnInfo(name = "session_id")
    private long mSessionId;

    public long getId()
    {
        return mId;
    }

    public void setId(long id)
    {
        this.mId = id;
    }

    public int getIsStarted() {
        return mIsStarted;
    }

    public void setIsStarted(int isStarted)
    {
        this.mIsStarted = isStarted;
    }

    public double getLatitude()
    {
        return mLatitude;
    }

    public void setLatitude(double latitude)
    {
        this.mLatitude = latitude;
    }

    public double getLongitude()
    {
        return mLongitude;
    }

    public void setLongitude(double longitude)
    {
        this.mLongitude = longitude;
    }

    public double getDistance()
    {
        return mDistance;
    }

    public void setDistance(double distance)
    {
        this.mDistance = distance;
    }

    public double getSpeed()
    {
        return mSpeed;
    }

    public void setSpeed(double speed)
    {
        this.mSpeed = speed;
    }

    public long getCreatedTime()
    {
        return mCreatedTime;
    }

    public void setCreatedTime(long createdTime)
    {
        this.mCreatedTime = createdTime;
    }

    public long getSessionId()
    {
        return mSessionId;
    }

    public void setSessionId(long sessionId) {
        this.mSessionId = sessionId;
    }

    @Override
    public boolean equals(Object obj)
    {
        return obj != null &&
                obj instanceof LocationDto &&
                ((LocationDto) obj).getId() > 0 &&
                ((LocationDto) obj).getId() == mId;
    }
}
