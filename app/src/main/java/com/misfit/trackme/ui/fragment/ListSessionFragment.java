package com.misfit.trackme.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.misfit.trackme.R;
import com.misfit.trackme.database.dto.SessionDto;
import com.misfit.trackme.ui.adapter.ListSessionAdapter;
import com.misfit.trackme.ui.viewmodels.IListSessionViewModel;
import com.misfit.trackme.ui.viewmodels.ListSessionViewModel;

import java.util.List;

import rx.Subscriber;

/**
 * Created by VinhLe on Jun, 2018.
 */
public class ListSessionFragment extends Fragment
{

    private View mRootView;
    private RecyclerView mRVSessions;
    private IListSessionViewModel mIListSessionViewModel;
    private IListSessionViewModel getListSessionViewModel()
    {
        if (mIListSessionViewModel == null)
        {
            mIListSessionViewModel = new ListSessionViewModel(getActivity());
        }
        return mIListSessionViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        mRootView = inflater.inflate(R.layout.fragment_list_session, container, false);
        mRVSessions = (RecyclerView) mRootView.findViewById(R.id.recycler_view_list_session);
        mRVSessions.setLayoutManager(new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false));
        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        shouldReloadList();
    }

    public void shouldReloadList()
    {
        getListSessionViewModel().getAllSession(getOnSubscriber());
    }

    private Subscriber getOnSubscriber()
    {
        return new Subscriber<List<SessionDto>>()
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
            public void onNext(List<SessionDto> dtos)
            {
                ListSessionAdapter adapter = new ListSessionAdapter(getActivity(), dtos);
                mRVSessions.setAdapter(adapter);
            }
        };
    }
}
