package com.misfit.trackme.ui.view;

/**
 * Created by VinhLe on Jun, 2018.
 */
public interface IControllerClickListener
{

    boolean onStartRecording();
    void onPauseRecording();
    void onStopRecording();
    void onResumeRecording();

}
