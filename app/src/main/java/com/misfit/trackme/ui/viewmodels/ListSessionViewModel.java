package com.misfit.trackme.ui.viewmodels;

import android.content.Context;

import com.misfit.trackme.repository.ISessionRepository;
import com.misfit.trackme.repository.SessionRepository;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by VinhLe on Jun, 2018.
 */
public class ListSessionViewModel extends BaseViewModel implements IListSessionViewModel
{

    private ISessionRepository mISessionRepository;
    private ISessionRepository getSessionRepository()
    {
        if (mISessionRepository == null)
        {
            mISessionRepository = new SessionRepository(getContext());
        }
        return mISessionRepository;
    }

    public ListSessionViewModel(Context context)
    {
        super(context);
    }

    @Override
    public void getAllSession(Subscriber subscriber)
    {
        getSessionRepository().findAll()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
