package com.misfit.trackme.database.dto;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by VinhLe on Jun, 2018.
 */
@Entity(tableName = "sessions")
public class SessionDto
{

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "count_paused")
    private long mCountPaused;

    @ColumnInfo(name = "start_time")
    private long mStartTime;

    @ColumnInfo(name = "end_time")
    private long mEndTime;

    @ColumnInfo(name = "is_delete")
    private int mIsDelete;

    @ColumnInfo(name = "average_speed")
    private double mAverageSpeed;

    public long getId()
    {
        return mId;
    }

    public void setId(long id)
    {
        this.mId = id;
    }

    public long getCountPaused()
    {
        return mCountPaused;
    }

    public void setCountPaused(long countPaused)
    {
        this.mCountPaused = countPaused;
    }

    public long getStartTime()
    {
        return mStartTime;
    }

    public void setStartTime(long startTime)
    {
        this.mStartTime = startTime;
    }

    public long getEndTime()
    {
        return mEndTime;
    }

    public void setEndTime(long endTime)
    {
        this.mEndTime = endTime;
    }

    public int getIsDelete()
    {
        return mIsDelete;
    }

    public void setIsDelete(int isDelete)
    {
        this.mIsDelete = isDelete;
    }

    public double getAverageSpeed()
    {
        return mAverageSpeed;
    }

    public void setAverageSpeed(double averageSpeed)
    {
        this.mAverageSpeed = averageSpeed;
    }
}
