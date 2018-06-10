package com.misfit.trackme.repository;

import android.content.Context;

import com.misfit.trackme.database.dto.LocationDto;
import com.misfit.trackme.database.dto.SessionDto;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;

/**
 * Created by VinhLe on Jun, 2018.
 */
public class SessionRepository extends BaseRepository implements ISessionRepository
{
    public SessionRepository(Context context)
    {
        super(context);
    }

    @Override
    public Observable<List<SessionDto>> findAll()
    {
        return Observable.fromCallable(new Callable<List<SessionDto>>()
        {
            @Override
            public List<SessionDto> call() throws Exception
            {
                List<SessionDto> listSessionDto = getAppData().sessionDao().findAll();
                List<LocationDto> listLocationDto = getAppData().locationDao().findAll();

                int i = 0;
                int j = 0;
                int maxi = listSessionDto.size();
                int maxj = listLocationDto.size();
                while (i < maxi)
                {
                    SessionDto session = listSessionDto.get(i);
                    double speed = 0;
                    double distance = 0;
                    long timeStart = 0;
                    long time = 0;
                    int countPaused = 0;
                    while (j < maxj)
                    {
                        LocationDto location = listLocationDto.get(j);
                        if (location.getSessionId() == session.getId())
                        {
                            timeStart = location.getIsStarted() == 1 ? location.getCreatedTime() - time: timeStart;
                            countPaused += location.getIsStarted() == 1 ? 1 : 0;
                            distance += location.getIsStarted() == 1 ? 0 : location.getDistance();
                            speed += location.getSpeed();
                            time = location.getCreatedTime() - timeStart;

                            session.getListLocation().add(location);
                            session.setDistance(distance);
                            session.setDurationTime(time);
                            session.setCountPaused(countPaused);
                            session.setAverageSpeed(session.getListLocation().size() - countPaused > 0 ?
                                    speed / (session.getListLocation().size() - countPaused) : session.getAverageSpeed());

                            j++;
                        }
                        else
                        {
                            break;
                        }
                    }
                    i++;
                }

                return listSessionDto;
            }
        });
    }

    @Override
    public Observable<SessionDto> createSession()
    {
        return Observable.fromCallable(new Callable<SessionDto>()
        {
            @Override
            public SessionDto call() throws Exception
            {
                SessionDto sessionDto = new SessionDto();
                sessionDto.setStartTime((new Date()).getTime());

                long[] ids = getAppData().sessionDao().insert(sessionDto);
                sessionDto.setId(ids[0]);

                return sessionDto;
            }
        });
    }
}
