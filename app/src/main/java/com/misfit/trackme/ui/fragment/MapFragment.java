package com.misfit.trackme.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.misfit.trackme.R;
import com.misfit.trackme.database.dto.LocationDto;
import com.misfit.trackme.helper.DateTimeHelper;
import com.misfit.trackme.helper.LoggerHelper;
import com.misfit.trackme.service.LocationService;
import com.misfit.trackme.ui.viewmodels.IMapFragmentViewModel;
import com.misfit.trackme.ui.viewmodels.MapFragmentViewModel;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by VinhLe on Jun, 2018.
 */
public class MapFragment extends Fragment
{
    private static final String TAG = "MapFragment";
    public static final String SESSION_ID_KEY = "SESSION_ID_KEY";
    public static final String TOTAL_MILLISECOND_KEY = "TOTAL_MILLISECOND_KEY";
    public static final String TIME_START_KEY = "TIME_START_KEY";
    public static final String IS_RECORDING_KEY = "IS_RECORDING_KEY";

    private View mRootView;
    private SupportMapFragment mSupportMapFragment;
    private GoogleMap mGoogleMap;
    private TextView mTVDistance;
    private TextView mTVSpeed;
    private TextView mTVTimer;

    private LinkedList<LocationDto> mListLocation = new LinkedList<LocationDto>();
    private LatLng mLastLatLng;
    private Marker mLastMarker;
    private Handler mHandler = new Handler();
    private int mSessionId;
    private long mTimeStarted = 0;
    private long mTotalTime = 0;
    private boolean mIsRecording = false;

    private IMapFragmentViewModel mIMapFragmentViewModel;
    private IMapFragmentViewModel getMapFragmentViewModel()
    {
        if (mIMapFragmentViewModel == null)
        {
            mIMapFragmentViewModel = new MapFragmentViewModel(getActivity());
        }
        return mIMapFragmentViewModel;
    }

    @Override
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mRootView = inflater.inflate(R.layout.fragment_map, container, false);

        mTVDistance = mRootView.findViewById(R.id.text_distance);
        mTVSpeed = mRootView.findViewById(R.id.text_speed);
        mTVTimer = mRootView.findViewById(R.id.text_timer);

        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle bundle)
    {
        super.onActivityCreated(bundle);

        mSupportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mSupportMapFragment.getMapAsync(mOnMapReadyCallback);

        if (bundle == null)
        {
            mSessionId = getArguments().getInt(SESSION_ID_KEY);
            startLocationService(mSessionId);
        }
        else
        {
            mSessionId = bundle.getInt(SESSION_ID_KEY);
            mTimeStarted = bundle.getLong(TIME_START_KEY);
            mTotalTime = bundle.getLong(TOTAL_MILLISECOND_KEY);
            mIsRecording = bundle.getBoolean(IS_RECORDING_KEY);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getMapFragmentViewModel().getLocations(getOnSubscriber(), mSessionId);
        // Register broadcast
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(
                mLocationReceiver,
                new IntentFilter(LocationService.IntentFilter)
        );
        if (mIsRecording)
        {
            updateTimer();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle)
    {
        if (bundle != null)
        {
            bundle.putInt(SESSION_ID_KEY, mSessionId);
            bundle.putLong(TIME_START_KEY, mTimeStarted);
            bundle.putLong(TOTAL_MILLISECOND_KEY, mTotalTime);
            bundle.putBoolean(IS_RECORDING_KEY, mIsRecording);
        }
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mListLocation.clear();
        mGoogleMap.clear();
        // Unregister broadcast
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mLocationReceiver);
        mHandler.removeCallbacks(mUpdateTimerRunnable);
    }

    private void moveTo(double latitude, double longitude, boolean isStart)
    {
        if (mGoogleMap != null)
        {
            LatLng position = new LatLng(latitude, longitude);

            if (isStart)
            {
                mGoogleMap.addMarker(new MarkerOptions().position(position).title("You started here!"));
                mLastMarker = null;
            }
            else
            {
                mGoogleMap.addPolyline(new PolylineOptions().add(mLastLatLng, position).width(5).color(Color.RED));
                if (mLastMarker != null)
                {
                    LoggerHelper.d(TAG, "Marker has removed!");
                    mLastMarker.remove();
                }
                mLastMarker = mGoogleMap.addMarker(new MarkerOptions().position(position).title("You're in here!"));
            }
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, 18.0f));
            mLastLatLng = position;
        }
    }

    private OnMapReadyCallback mOnMapReadyCallback = new OnMapReadyCallback()
    {
        @Override
        public void onMapReady(GoogleMap googleMap)
        {
            mGoogleMap = googleMap;
        }
    };

    private BroadcastReceiver mLocationReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            getMapFragmentViewModel().getLocations(getOnSubscriber(), mSessionId);
        }
    };

    public void startLocationService()
    {
        startLocationService(mSessionId);
    }

    public void startLocationService(int sessionId)
    {
        if (sessionId > 0 && getActivity() != null)
        {
            this.mIsRecording = true;
            this.mTimeStarted = (new Date().getTime());
            Intent serviceIntent = new Intent(getContext(), LocationService.class);
            serviceIntent.putExtra(SESSION_ID_KEY, sessionId);
            getActivity().startService(serviceIntent);

            updateTimer();
        }
    }

    public void stopLocationService()
    {
        if (getActivity() != null)
        {
            Intent serviceIntent = new Intent(getContext(), LocationService.class);
            getActivity().stopService(serviceIntent);

            mTotalTime += new Date().getTime() - mTimeStarted;
            mHandler.removeCallbacks(mUpdateTimerRunnable);
            this.mIsRecording = false;
        }
    }

    private Subscriber getOnSubscriber()
    {
        return new Subscriber<List<LocationDto>>()
        {
            @Override
            public void onCompleted()
            {

            }

            @Override
            public void onError(Throwable e)
            {

            }

            @Override
            public void onNext(List<LocationDto> dtos)
            {
                onUpdateLocations(dtos);
            }
        };
    }

    private void onUpdateLocations(List<LocationDto> dtos)
    {
        LinkedList<LocationDto> listNotDraw = new LinkedList();
        double distance = 0;
        double currentSpeed = 0;

        for (LocationDto dto : dtos)
        {
            if (dto.getIsStarted() != 1)
            {
                distance += dto.getDistance();
                currentSpeed = dto.getSpeed();
            }

            if (!mListLocation.contains(dto))
            {
                listNotDraw.add(dto);
            }
        }
        mListLocation.clear();
        mListLocation.addAll(dtos);
        // draw
        for (LocationDto dto : listNotDraw)
        {
            moveTo(dto.getLatitude(), dto.getLongitude(), dto.getIsStarted() == 1);
        }

        mTVDistance.setText(String.format("%.2f km", distance / 1000));
        mTVSpeed.setText(String.format("%.2f km/h", currentSpeed * 3.6));
    }

    private void updateTimer()
    {
        Date currentDate = new Date();
        long duration = currentDate.getTime() - mTimeStarted + mTotalTime;

        mTVTimer.setText(DateTimeHelper.parseStringToTime(duration));
        mHandler.postDelayed(mUpdateTimerRunnable, 1000);
    }

    private Runnable mUpdateTimerRunnable = new Runnable()
    {
        @Override
        public void run()
        {
            updateTimer();
        }
    };
}
