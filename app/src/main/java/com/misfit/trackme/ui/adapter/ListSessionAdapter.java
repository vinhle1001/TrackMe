package com.misfit.trackme.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.misfit.trackme.R;
import com.misfit.trackme.database.dto.LocationDto;
import com.misfit.trackme.database.dto.SessionDto;
import com.misfit.trackme.helper.DateTimeHelper;

import java.util.List;

/**
 * Created by VinhLe on Jun, 2018.
 */
public class ListSessionAdapter extends RecyclerView.Adapter<ListSessionAdapter.ViewHolder>
{

    private Context mContext;
    private List<SessionDto> mSessions;

    public ListSessionAdapter(Context context, List<SessionDto> sessions)
    {
        this.mContext = context;
        this.mSessions = sessions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_session, parent, false);
        return new ViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        int displayPosition = getItemCount() - position - 1;
        SessionDto session = mSessions.get(displayPosition);

        if (mContext != null)
        {
            Glide.with(mContext)
                    .load(getUrlImageStaticMap(session))
                    .into(holder.mIVMap);
        }

        holder.mTVDistance.setText(String.format("%.2f km", session.getDistance() / 1000));
        holder.mTVSpeed.setText(String.format("%.2f km/h", session.getAverageSpeed() * 3.6));
        holder.mTVDuration.setText(DateTimeHelper.parseStringToTime(session.getDurationTime()));
    }

    @Override
    public int getItemCount()
    {
        if (mSessions == null)
        {
            return 0;
        }
        return mSessions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {

        ImageView mIVMap;
        TextView mTVDistance;
        TextView mTVSpeed;
        TextView mTVDuration;

        public ViewHolder(View itemView)
        {
            super(itemView);

            mIVMap = (ImageView) itemView.findViewById(R.id.img_map);
            mTVDistance = (TextView) itemView.findViewById(R.id.text_distance);
            mTVSpeed = (TextView) itemView.findViewById(R.id.text_speed);
            mTVDuration = (TextView) itemView.findViewById(R.id.text_timer);
        }
    }

    private String getUrlImageStaticMap(SessionDto session)
    {
        String path = "";
        for (LocationDto location : session.getListLocation())
        {
            if (location.getIsStarted() == 1)
            {
                if (path.endsWith("|"))
                {
                    path = path.substring(0, path.length() - 1);
                }

                path += "&path=color:red|weight:5|";
            }
            path += String.format("%.6f,%.6f", location.getLatitude(), location.getLongitude()) + "|";
        }

        if (!TextUtils.isEmpty(path))
        {
            path = path.substring(0, path.length() - 1);
        }
        return "http://maps.googleapis.com/maps/api/staticmap?size=480x360" + path + "&sensor=false";
    }
}
