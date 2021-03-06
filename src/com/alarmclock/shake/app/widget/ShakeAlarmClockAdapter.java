
package com.alarmclock.shake.app.widget;

import java.text.DateFormatSymbols;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alarmclock.shake.app.R;
import com.alarmclock.shake.app.model.ShakeAlarmClock;
import com.alarmclock.shake.app.provider.DefaultDao;
import com.alarmclock.shake.app.utils.Constants;
import com.alarmclock.shake.app.utils.Utils;

public class ShakeAlarmClockAdapter extends CursorAdapter {

    public static final int INVALID_POSITION = -1;

    private int mOpenPosition = INVALID_POSITION;

    private Fragment mFragment;

    private Context mContext;

    private String[] date;

    private DefaultDao mDao;

    public ShakeAlarmClockAdapter(Fragment fragment, Context context, Cursor c) {
        super(context, c, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mFragment = fragment;
        mContext = context;
        // mShakeAlarmClocks = shakeAlarmClocks;
        date = new DateFormatSymbols().getShortWeekdays();
        mDao = new DefaultDao(context);
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
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        final int position = cursor.getPosition();
        if (cursor != null) {
            final ViewHolder holder = (ViewHolder)view.getTag();
            final ShakeAlarmClock shakeAlarmClock = mDao.getClock(cursor);
            if (shakeAlarmClock != null) {
                holder.timeTv.setText(shakeAlarmClock.getTime());
                holder.infoTv.setText(shakeAlarmClock.getName());
                holder.nameTv.setText(shakeAlarmClock.getName());
                if (isOpenAtPosition(position)) {
                    holder.moreLayout.setVisibility(View.VISIBLE);
                    holder.infoTv.setText("");
                } else {
                    holder.moreLayout.setVisibility(View.GONE);
                    String str = shakeAlarmClock.getName();
                    if (shakeAlarmClock.isRepeat()) {
                        if (!TextUtils.isEmpty(shakeAlarmClock.getName())) {
                            str += ":";
                        }
                        for (int i = 1; i < shakeAlarmClock.getDayInt().length; i++) {
                            if (shakeAlarmClock.getDayInt()[i] == 1) {
                                str += (date[i] + " ");
                            }
                        }
                    }
                    holder.infoTv.setText(str);
                }

                holder.cbRepeat.setChecked(shakeAlarmClock.isRepeat());
                if (holder.cbRepeat.isChecked()) {
                    holder.day_layout.setVisibility(View.VISIBLE);
                } else {
                    holder.day_layout.setVisibility(View.GONE);
                }

                holder.clickLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        troggleOpenPosition(position);
                    }
                });

                holder.s.setChecked(shakeAlarmClock.isOpen());

                holder.s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        shakeAlarmClock.setOpen(isChecked);
                        mDao.updateClock(shakeAlarmClock, true);
                    }
                });

                holder.cbRepeat
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                            boolean isChecked) {
                        if (isChecked) {
                            holder.day_layout.setVisibility(View.VISIBLE);
                        } else {
                            holder.day_layout.setVisibility(View.GONE);
                        }
                        shakeAlarmClock.setRepeat(isChecked);
                        mDao.updateClock(shakeAlarmClock, true);
                    }
                });

                for (int i = 1; i < shakeAlarmClock.getDayBoolean().length; i++) {
                    holder.tb[i].setChecked(shakeAlarmClock.getDayBoolean()[i]);
                }
                holder.tb[1]
                        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView,
                                    boolean isChecked) {
                                shakeAlarmClock.setDayBoolean(isChecked, 1);
                                mDao.updateClock(shakeAlarmClock, true);
                            }
                        });
                holder.tb[2]
                        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView,
                                    boolean isChecked) {
                                shakeAlarmClock.setDayBoolean(isChecked, 2);
                                mDao.updateClock(shakeAlarmClock, true);
                            }
                        });
                holder.tb[3]
                        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView,
                                    boolean isChecked) {
                                shakeAlarmClock.setDayBoolean(isChecked, 3);
                                mDao.updateClock(shakeAlarmClock, true);
                            }
                        });
                holder.tb[4]
                        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView,
                                    boolean isChecked) {
                                shakeAlarmClock.setDayBoolean(isChecked, 4);
                                mDao.updateClock(shakeAlarmClock, true);
                            }
                        });
                holder.tb[5]
                        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView,
                                    boolean isChecked) {
                                shakeAlarmClock.setDayBoolean(isChecked, 5);
                                mDao.updateClock(shakeAlarmClock, true);
                            }
                        });
                holder.tb[6]
                        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView,
                                    boolean isChecked) {
                                shakeAlarmClock.setDayBoolean(isChecked, 6);
                                mDao.updateClock(shakeAlarmClock, true);
                            }
                        });
                holder.tb[7]
                        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView,
                                    boolean isChecked) {
                                shakeAlarmClock.setDayBoolean(isChecked, 7);
                                mDao.updateClock(shakeAlarmClock, true);
                            }
                        });

                holder.nameTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });

                if (!TextUtils.isEmpty(shakeAlarmClock.getRingName())) {
                    holder.ringNameTv.setText(shakeAlarmClock.getRingName());
                } else {
                    holder.ringNameTv.setText(mContext.getString(R.string.alarm_silent));
                }
                holder.ringNameTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
                                RingtoneManager.TYPE_ALARM);
                        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,
                                mContext.getString(R.string.set_ringtone));
                        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
                        Uri uri;
                        if (TextUtils.isEmpty(shakeAlarmClock.getRingUri())) {
                            uri = null;
                        } else {
                            uri = Uri.parse(shakeAlarmClock.getRingUri());
                        }
                        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, uri);
                        mFragment.startActivityForResult(intent, Constants.RESULTCODE_CHOOSE_RING);
                    }
                });

                holder.cbVibrate.setChecked(shakeAlarmClock.isVibrate());
                holder.cbVibrate
                .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                            boolean isChecked) {
                        shakeAlarmClock.setVibrate(isChecked);
                        mDao.updateClock(shakeAlarmClock, true);
                    }
                });
            }
        }
    }

    @Override
    public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
        View convertView = LayoutInflater.from(mContext).inflate(R.layout.alarmclock_item, null);
        ViewHolder holder = new ViewHolder();
        holder.timeTv = (TextView)convertView.findViewById(R.id.time);
        holder.timeTv.setTypeface(Utils.getRobotoLightTypeface(mContext));
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
        holder.ringNameTv = (TextView)convertView.findViewById(R.id.ring_name);
        holder.cbVibrate = (CheckBox)convertView.findViewById(R.id.cb_vibrate);
        convertView.setTag(holder);
        return convertView;
    }

    static class ViewHolder {
        TextView timeTv, infoTv, nameTv, ringNameTv;

        Switch s;

        LinearLayout clickLayout, moreLayout, day_layout;

        CheckBox cbRepeat, cbVibrate;

        ToggleButton[] tb = new ToggleButton[8];
    }

    @Override
    public ShakeAlarmClock getItem(int position) {
        ShakeAlarmClock shakeAlarmClock = mDao.getClock((Cursor)super.getItem(position));
        return shakeAlarmClock;
    }



}
