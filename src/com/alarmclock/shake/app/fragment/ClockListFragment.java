package com.alarmclock.shake.app.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alarmclock.shake.app.R;
import com.alarmclock.shake.app.model.ShakeAlarmClock;
import com.alarmclock.shake.app.widget.ShakeAlarmClockAdapter;

public class ClockListFragment extends Fragment{

    private ListView mListView;

    private ArrayList<ShakeAlarmClock> mShakeAlarmClocks;

    private ShakeAlarmClockAdapter mAdapter;

    private ActionBarActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.clock_list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (ActionBarActivity)getActivity();
        ShakeAlarmClock shakeAlarmClock = new ShakeAlarmClock();
        shakeAlarmClock.setTime("12:00");
        shakeAlarmClock.setInfo("test");
        mShakeAlarmClocks = new ArrayList<ShakeAlarmClock>();
        for (int i = 0; i < 10; i++) {
            mShakeAlarmClocks.add(shakeAlarmClock);
        }
        mAdapter = new ShakeAlarmClockAdapter(mActivity, mShakeAlarmClocks);
        mListView = (ListView)getView().findViewById(R.id.list);
        mListView.setAdapter(mAdapter);
    }

}
