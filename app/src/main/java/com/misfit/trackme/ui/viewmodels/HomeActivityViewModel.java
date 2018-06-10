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
public class HomeActivityViewModel extends BaseViewModel implements IHomeActivityViewModel
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

    public HomeActivityViewModel(Context context)
    {
        super(context);
    }

    @Override
    public void createSession(Subscriber subscriber)
    {
        getSessionRepository().createSession()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
