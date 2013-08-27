
package com.alarmclock.shake.app.widget;

import java.text.DateFormatSymbols;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alarmclock.shake.app.R;
import com.alarmclock.shake.app.model.ShakeAlarmClock;

public class ShakeAlarmClockAdapter extends BaseAdapter {

    public static final int INVALID_POSITION = -1;

    private int mOpenPosition = INVALID_POSITION;

    private Context mContext;

    private ArrayList<ShakeAlarmClock> mShakeAlarmClocks;

    private Typeface mRoboto_Light;

    private String[] date;

    public ShakeAlarmClockAdapter(Context context, ArrayList<ShakeAlarmClock> shakeAlarmClocks) {
        mContext = context;
        mShakeAlarmClocks = shakeAlarmClocks;
        mRoboto_Light = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Light.ttf");
        date = new DateFormatSymbols().getShortWeekdays();
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

        if (null != convertView && null != convertView.getTag()) {
            holder = (ViewHolder)convertView.getTag();
        } else {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.alarmclock_item, null);
            holder.timeTv = (TextView)convertView.findViewById(R.id.time);
            holder.timeTv.setTypeface(mRoboto_Light);
            holder.nameTv = (TextView)convertView.findViewById(R.id.name);
            holder.infoTv = (TextView)convertView.findViewById(R.id.info);
            holder.clickLayout = (LinearLayout)convertView.findViewById(R.id.click_layout);
            holder.moreLayout = (LinearLayout)convertView.findViewById(R.id.more_layout);
            holder.s = (Switch)convertView.findViewById(R.id.switch_btn);
            holder.day_layout = (LinearLayout)convertView.findViewById(R.id.day_layout);
            holder.cbRepeat = (CheckBox)convertView.findViewById(R.id.cb_repeat);
            holder.tb[1] = (ToggleButton)convertView.findViewById(R.id.tb_1);
            holder.tb[1].setText(date[1]);
            holder.tb[1].setTextOn(date[1]);
            holder.tb[1].setTextOff(date[1]);
            holder.tb[2] = (ToggleButton)convertView.findViewById(R.id.tb_2);
            holder.tb[2].setText(date[2]);
            holder.tb[2].setTextOn(date[2]);
            holder.tb[2].setTextOff(date[2]);
            holder.tb[3] = (ToggleButton)convertView.findViewById(R.id.tb_3);
            holder.tb[3].setText(date[3]);
            holder.tb[3].setTextOn(date[3]);
            holder.tb[3].setTextOff(date[3]);
            holder.tb[4] = (ToggleButton)convertView.findViewById(R.id.tb_4);
            holder.tb[4].setText(date[4]);
            holder.tb[4].setTextOn(date[4]);
            holder.tb[4].setTextOff(date[4]);
            holder.tb[5] = (ToggleButton)convertView.findViewById(R.id.tb_5);
            holder.tb[5].setText(date[5]);
            holder.tb[5].setTextOn(date[5]);
            holder.tb[5].setTextOff(date[5]);
            holder.tb[6] = (ToggleButton)convertView.findViewById(R.id.tb_6);
            holder.tb[6].setText(date[6]);
            holder.tb[6].setTextOn(date[6]);
            holder.tb[6].setTextOff(date[6]);
            holder.tb[7] = (ToggleButton)convertView.findViewById(R.id.tb_7);
            holder.tb[7].setText(date[7]);
            holder.tb[7].setTextOn(date[7]);
            holder.tb[7].setTextOff(date[7]);
            convertView.setTag(holder);
        }

        final ShakeAlarmClock shakeAlarmClock = mShakeAlarmClocks.get(position);
        if (shakeAlarmClock != null) {
            holder.timeTv.setText(shakeAlarmClock.getTime());
            holder.infoTv.setText(shakeAlarmClock.getName());
            holder.nameTv.setText(shakeAlarmClock.getName());
            if (isOpenAtPosition(position)) {
                holder.moreLayout.setVisibility(View.VISIBLE);
            } else {
                holder.moreLayout.setVisibility(View.GONE);
            }
            holder.cbRepeat.setChecked(true);
            if (holder.cbRepeat.isChecked()) {
                holder.day_layout.setVisibility(View.VISIBLE);
            }else {
                holder.day_layout.setVisibility(View.GONE);
            }

            holder.clickLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    troggleOpenPosition(position);
                }
            });

            holder.s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                }
            });

            holder.cbRepeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (holder.cbRepeat.isChecked()) {
                        holder.day_layout.setVisibility(View.VISIBLE);
                    }else {
                        holder.day_layout.setVisibility(View.GONE);
                    }
                }
            });
        }
        return convertView;
    }

    static class ViewHolder {
        TextView timeTv;

        TextView infoTv;

        TextView nameTv;

        Switch s;

        LinearLayout clickLayout;

        LinearLayout moreLayout;

        LinearLayout day_layout;

        CheckBox cbRepeat;

        ToggleButton[] tb = new ToggleButton[8];
    }

}
