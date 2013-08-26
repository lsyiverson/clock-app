
package com.alarmclock.shake.app.widget;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alarmclock.shake.app.R;
import com.alarmclock.shake.app.model.ShakeAlarmClock;

public class ShakeAlarmClockAdapter extends BaseAdapter {

    public static final int INVALID_POSITION = -1;

    private int mOpenPosition = INVALID_POSITION;

    private Context mContext;

    private ArrayList<ShakeAlarmClock> mShakeAlarmClocks;

    private Typeface mRoboto_Light;

    public ShakeAlarmClockAdapter(Context context, ArrayList<ShakeAlarmClock> shakeAlarmClocks ){
        mContext = context;
        mShakeAlarmClocks = shakeAlarmClocks;
        mRoboto_Light = Typeface.createFromAsset(context.getAssets(),"fonts/Roboto-Light.ttf");
    }

    public int getOpenPosition() {
        return mOpenPosition;
    }

    public boolean isOpenAtPosition(int pos) {
        return mOpenPosition == pos;
    }

    public void setOpenPosition(int pos) {
        mOpenPosition = pos;
    }

    protected void troggleOpenPosition(int pos) {
        int oldOpenPos = getOpenPosition();
        if (oldOpenPos == pos) {
            setOpenPosition(INVALID_POSITION);
        } else {
            setOpenPosition(pos);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mShakeAlarmClocks.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return mShakeAlarmClocks.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;

        if(null != convertView && null != convertView.getTag()) {
            holder = (ViewHolder)convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.alarmclock_item, null);
            holder.timeTv = (TextView)convertView.findViewById(R.id.time);
            holder.timeTv.setTypeface(mRoboto_Light);
            holder.infoTv = (TextView)convertView.findViewById(R.id.info);
            holder.clickLayout = (LinearLayout)convertView.findViewById(R.id.click_layout);
            holder.moreLayout = (LinearLayout)convertView.findViewById(R.id.more_layout);
            convertView.setTag(holder);
        }

        final ShakeAlarmClock shakeAlarmClock = mShakeAlarmClocks.get(position);
        if (shakeAlarmClock != null) {
            holder.timeTv.setText(shakeAlarmClock.getTime());
            holder.infoTv.setText(shakeAlarmClock.getInfo());
            if (isOpenAtPosition(position)) {
                holder.moreLayout.setVisibility(View.VISIBLE);
            }else {
                holder.moreLayout.setVisibility(View.GONE);
            }
        }


        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                troggleOpenPosition(position);
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView timeTv;
        TextView infoTv;
        LinearLayout clickLayout;
        LinearLayout moreLayout;
    }

}
