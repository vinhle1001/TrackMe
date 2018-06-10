package com.misfit.trackme;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.misfit.trackme.database.dto.SessionDto;
import com.misfit.trackme.helper.PermissionDefine;
import com.misfit.trackme.helper.PermissionHelper;
import com.misfit.trackme.ui.fragment.ListSessionFragment;
import com.misfit.trackme.ui.fragment.MapFragment;
import com.misfit.trackme.ui.view.ControllerView;
import com.misfit.trackme.ui.view.IControllerClickListener;
import com.misfit.trackme.ui.viewmodels.HomeActivityViewModel;
import com.misfit.trackme.ui.viewmodels.IHomeActivityViewModel;

import rx.Subscriber;

/**
 * Created by VinhLe on Jun, 2018.
 */
public class HomeActivity extends AppCompatActivity implements IControllerClickListener
{
    private ControllerView mControllerView;
    private IHomeActivityViewModel mIHomeActivityViewModel;
    private IHomeActivityViewModel getHomeActivityViewModel()
    {
        if (mIHomeActivityViewModel == null)
        {
            mIHomeActivityViewModel = new HomeActivityViewModel(getApplicationContext());
        }
        return mIHomeActivityViewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mControllerView = (ControllerView) findViewById(R.id.controller_view);
        mControllerView.setControllerClickListener(this);

        PermissionHelper.requestPermission(this, PermissionDefine.ACCESS_FINE_LOCATION.getPermission(), PermissionDefine.ACCESS_FINE_LOCATION.getRequestCode());
        PermissionHelper.requestPermission(this, PermissionDefine.ACCESS_COARSE_LOCATION.getPermission(), PermissionDefine.ACCESS_COARSE_LOCATION.getRequestCode());

        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new ListSessionFragment())
                    .addToBackStack(ListSessionFragment.class.getName())
                    .commit();
        }
    }

    @Override
    protected void onStart()
    {
        super.onStart();
    }

    @Override
    public void onStartRecording()
    {
        getHomeActivityViewModel().createSession(getOnSubscriber(Actions.CREATE_SESSION));
    }

    @Override
    public void onPauseRecording()
    {
        Fragment topFragment = getSupportFragmentManager().findFragmentByTag(MapFragment.class.getName());
        if (topFragment != null && topFragment instanceof MapFragment)
        {
            ((MapFragment) topFragment).stopLocationService();
        }
    }

    @Override
    public void onStopRecording()
    {
        Fragment mapFragment = getSupportFragmentManager().findFragmentByTag(MapFragment.class.getName());
        if (mapFragment != null && mapFragment instanceof MapFragment)
        {
            ((MapFragment) mapFragment).stopLocationService();
        }
        getSupportFragmentManager().popBackStack();

        Fragment listFragment = getSupportFragmentManager().findFragmentByTag(ListSessionFragment.class.getName());
        if (listFragment != null && listFragment instanceof ListSessionFragment)
        {
            ((ListSessionFragment) listFragment).shouldReloadList();
        }
    }

    @Override
    public void onResumeRecording()
    {
        Fragment topFragment = getSupportFragmentManager().findFragmentByTag(MapFragment.class.getName());
        if (topFragment != null && topFragment instanceof MapFragment)
        {
            ((MapFragment) topFragment).startLocationService();
        }
    }

    private MapFragment createMapFragment(int sessionId)
    {
        MapFragment mapFragment = new MapFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(MapFragment.SESSION_ID_KEY, sessionId);
        mapFragment.setArguments(bundle);
        return mapFragment;
    }

    private Subscriber getOnSubscriber(final Actions action)
    {
        return new Subscriber()
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
            public void onNext(Object o)
            {
                switch (action)
                {
                    case CREATE_SESSION:
                        SessionDto sessionDto = (SessionDto) o;
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, createMapFragment(((Long) sessionDto.getId()).intValue()))
                                .addToBackStack(MapFragment.class.getName())
                                .commit();
                        break;
                }
            }
        };
    }

    // Enums
    private enum Actions
    {
        CREATE_SESSION
    }
}
