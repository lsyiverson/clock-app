package com.alarmclock.shake.app.fragment;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alarmclock.shake.app.R;
import com.alarmclock.shake.app.model.ShakeAlarmClock;
import com.alarmclock.shake.app.provider.DefaultDao;
import com.alarmclock.shake.app.utils.Constants;
import com.alarmclock.shake.app.utils.Utils;
import com.alarmclock.shake.app.widget.ShakeAlarmClockAdapter;

public class ClockListFragment extends Fragment{

    private ListView mListView;

    private ArrayList<ShakeAlarmClock> mShakeAlarmClocks;

    private ShakeAlarmClockAdapter mAdapter;

    private ActionBarActivity mActivity;

    private DefaultDao mDao;

    private LoaderCallback mLoaderCallback;

    private int mLoderId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.clock_list, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = (ActionBarActivity)getActivity();
        setHasOptionsMenu(true);
        mDao = new DefaultDao(mActivity);
        mShakeAlarmClocks = mDao.getClocks();
        mLoaderCallback = new LoaderCallback();
        mActivity.getSupportLoaderManager().initLoader(mLoderId, null, mLoaderCallback);
        mListView = (ListView)getView().findViewById(R.id.list);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK)
        {
            return;
        }
        switch (requestCode)
        {
            case Constants.RESULTCODE_CHOOSE_RING:
                try
                {
                    int position = mAdapter.getOpenPosition();
                    Uri pickedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    String title = Utils.getTitleByUri(mActivity, pickedUri);
                    ShakeAlarmClock shakeAlarmClock = mAdapter.getItem(position);
                    shakeAlarmClock.setRingName(title);
                    shakeAlarmClock.setRingUri(String.valueOf(pickedUri));
                    mDao.updateClock(shakeAlarmClock, true);
                }
                catch (Exception e)
                {
                }
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case 1:
                ShakeAlarmClock shakeAlarmClock = new ShakeAlarmClock();
                shakeAlarmClock.setTime("8:30");
                mDao.insertClock(shakeAlarmClock, true);
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class LoaderCallback implements LoaderCallbacks<Cursor> {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            CursorLoader loader = mDao.queryClocks(mActivity);
            return loader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if (cursor.getCount()!=0) {
                if (mAdapter == null) {
                    mAdapter = new ShakeAlarmClockAdapter(ClockListFragment.this, mActivity, cursor);
                    mListView.setAdapter(mAdapter);
                }
                mAdapter.swapCursor(cursor);
                mAdapter.notifyDataSetChanged();
            }
        }
        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mAdapter.swapCursor(null);
        }
    }
}
