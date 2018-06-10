package com.misfit.trackme.ui.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.misfit.trackme.R;

/**
 * Created by VinhLe on Jun, 2018.
 */
public class ControllerView extends FrameLayout
{
    private View mRootView;
    private ImageButton mIBStartRecording;
    private ImageButton mIBPauseRecording;
    private ImageButton mIBStopRecording;
    private ImageButton mIBResumeRecording;
    private RelativeLayout mRLContainerLayoutStopState;

    private IControllerClickListener mControllerClickListener;
    private ControllerState mSate = ControllerState.NONE;

    public ControllerView(@NonNull Context context)
    {
        this(context, null);
    }

    public ControllerView(@NonNull Context context, @Nullable AttributeSet attrs)
    {
        this(context, attrs, -1);
    }

    public ControllerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom)
    {
        super.onLayout(changed, left, top, right, bottom);
        updateUI();
    }

    public void setControllerClickListener(IControllerClickListener controllerClickListener)
    {
        this.mControllerClickListener = controllerClickListener;
    }

    private void init(Context context, AttributeSet attrs)
    {
        mRootView = LayoutInflater.from(context).inflate(R.layout.layout_controller, this);

        mIBStartRecording = (ImageButton) mRootView.findViewById(R.id.btn_record);
        mIBPauseRecording = (ImageButton) mRootView.findViewById(R.id.btn_pause);
        mIBStopRecording = (ImageButton) mRootView.findViewById(R.id.btn_stop);
        mIBResumeRecording = (ImageButton) mRootView.findViewById(R.id.btn_resume);
        mRLContainerLayoutStopState = (RelativeLayout) mRootView.findViewById(R.id.stop_container);

        mIBStartRecording.setOnClickListener(mViewOnClickListener);
        mIBPauseRecording.setOnClickListener(mViewOnClickListener);
        mIBStopRecording.setOnClickListener(mViewOnClickListener);
        mIBResumeRecording.setOnClickListener(mViewOnClickListener);
    }

    private void updateUI()
    {
        mIBStartRecording.setVisibility(View.GONE);
        mIBPauseRecording.setVisibility(View.GONE);
        mRLContainerLayoutStopState.setVisibility(View.GONE);

        switch (mSate)
        {
            case NONE:
                mIBStartRecording.setVisibility(View.VISIBLE);
                break;
            case RECORDING:
                mIBPauseRecording.setVisibility(View.VISIBLE);
                break;
            case PAUSED:
                mRLContainerLayoutStopState.setVisibility(View.VISIBLE);
                break;
        }
    }

    private OnClickListener mViewOnClickListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            if (mControllerClickListener == null)
            {
                // Nothings
            }
            else if (v.getId() == mIBStartRecording.getId())
            {
                mControllerClickListener.onStartRecording();
                mSate = ControllerState.RECORDING;
            }
            else if (v.getId() == mIBPauseRecording.getId())
            {
                mControllerClickListener.onPauseRecording();
                mSate = ControllerState.PAUSED;
            }
            else if (v.getId() == mIBStopRecording.getId())
            {
                mControllerClickListener.onStopRecording();
                mSate = ControllerState.NONE;
            }
            else if (v.getId() == mIBResumeRecording.getId())
            {
                mControllerClickListener.onResumeRecording();
                mSate = ControllerState.RECORDING;
            }

            updateUI();
        }
    };
}
