
package com.alarmclock.shake.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alarmclock.shake.app.fragment.ClockListFragment;


public class MainActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] mDrawerTitles;
    private int[] mDrawerIcons;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private ActionBar mActionBar;
    private final int INVALID_POSITION = -1;
    private final int CLOCK_LIST_POSITION = 0;
    private int mPostion = 0;
    private int mPostionTemp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitle = mDrawerTitle = getTitle();
        mDrawerTitles = getResources().getStringArray(R.array.drawer_arrays);
        mDrawerIcons = new int[]{R.drawable.icon_test,R.drawable.icon_test,R.drawable.icon_test,R.drawable.icon_test};
        mActionBar = getSupportActionBar();
        setupView();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
                ) {
            @Override
            public void onDrawerClosed(View view) {
                mPostion = mPostionTemp;
                mActionBar.setTitle(mTitle);
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mPostion = INVALID_POSITION;
                mActionBar.setTitle(mDrawerTitle);
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }

        //        AlarmManager am=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
        //        Calendar calendar=Calendar.getInstance();
        //        calendar.set(Calendar.HOUR_OF_DAY, 10);
        //        calendar.set(Calendar.MINUTE, 49);
        //
        //        Date date = calendar.getTime();
        //
        //        ShakeAlarmClock shakeAlarmClock = new ShakeAlarmClock();
        //        shakeAlarmClock.setId(1);
        //        shakeAlarmClock.setTime(Utils.TIME_FORMAT.format(date));
        //        shakeAlarmClock.setName("");
        //        shakeAlarmClock.setOpen(true);
        //        shakeAlarmClock.setRepeat(false);
        //        shakeAlarmClock.setVibrate(true);
        //        shakeAlarmClock.setDayString("");
        //        shakeAlarmClock.setRingName("");
        //        Uri alertUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        //        shakeAlarmClock.setRingUri(alertUri.toString());
        //
        //        Intent intent=new Intent(this, AlarmActivity.class);
        //        intent.putExtra(Constants.ALARM_RAW_DATA, shakeAlarmClock);
        //
        //        PendingIntent pendingIntent=PendingIntent.getActivity(this, shakeAlarmClock.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //        am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        //
        //        Utils.addAlarmIcon(this);
    }

    private void setupView(){
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new DrawerAdapter(this, mDrawerIcons, mDrawerTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        mPostionTemp = position;
        Fragment fragment = null;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:
                fragment = new ClockListFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            default:
                break;
        }
        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        mActionBar.setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    class DrawerAdapter extends BaseAdapter{
        private LayoutInflater inflater;
        private String[] texts;
        private int[] icons;
        private Context context;

        public DrawerAdapter(Context ctx, int[] icons, String[] texts) {
            super();
            this.texts = texts;
            this.icons = icons;
            this.context = ctx;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return texts.length;
        }

        @Override
        public Object getItem(int position) {
            return texts[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.drawer_item, null);
            ImageView icon = (ImageView) convertView
                    .findViewById(R.id.icon);
            TextView text = (TextView) convertView
                    .findViewById(R.id.text);
            icon.setBackgroundResource(icons[position]);
            text.setText(texts[position]);
            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        switch (mPostion) {
            case CLOCK_LIST_POSITION:
                MenuItemCompat.setShowAsAction(menu.add(0, 1, 0, getString(R.string.add_clock))
                        .setIcon(R.drawable.ic_action_alerts_and_states_add_alarm),
                        MenuItemCompat.SHOW_AS_ACTION_IF_ROOM
                        | MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
                break;
            default:
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }else {
            return super.onOptionsItemSelected(item);
        }
    }
}
