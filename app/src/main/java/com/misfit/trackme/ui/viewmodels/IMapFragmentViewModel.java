package com.misfit.trackme.ui.viewmodels;

import rx.Subscriber;

/**
 * Created by VinhLe on Jun, 2018.
 */
public interface IMapFragmentViewModel
{
    void getLocations(Subscriber subscriber, int sessionId);
}
