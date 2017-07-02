package orbital.dingletutors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.mindorks.placeholderview.PlaceHolderView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    public static boolean active = false;
    private Popup popup;
    private TextView notificationCount;

    public String[] categoryTitles = new String[]{"Home", "Lesson History", "Notifications", "Students", "Finances"};
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private DrawerCallBack mCallBack;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (notificationCount != null) {
                notificationCount.setText(Integer.toString(MinuteUpdater.minuteQueue.size()));
            }
            if (popup != null && popup.isVisible()) {
                popup.updateList();
            }
        }
    };

    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("orbital.dingletutors.UPDATE_MAIN");
        registerReceiver(receiver, filter);
        super.onResume();
        MinuteUpdater.mainAppRunning = true;
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
        MinuteUpdater.mainAppRunning = false;
    }

    @Override
    public void onStop() {
        try {
            MinuteUpdater.mainAppRunning = false;
            MinuteUpdater.calendarMap.save();
            LessonPresetMap.map.save();
            StudentPresetMap.map.save();
            LessonHistoryMap.map.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onStop();
        active = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        load();
        // test();

//        Modification of the Action bar so space isnt wasted
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        final View view =getSupportActionBar().getCustomView();

        notificationCount = (TextView) view.findViewById(R.id.notificationCount);

        mDrawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView)findViewById(R.id.drawerView);
        setupDrawer();
        mCallBack = new DrawerCallBack();
        DrawerMenuItem.setDrawerCallBack(mCallBack);
        mCallBack.onHomeMenuSelected();


        // onClickListener for the notification button
        // can change to what we want when decided
        Button checkinBtn =(Button) view.findViewById(R.id.checkinBtn);
        checkinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Popup popup = new PopupCheckIn(getApplicationContext(), view, view.getWidth() * 3 / 4, view.getWidth() * 3 / 4, 0);
                popup.show();
            }
        });
        ImageButton notificationBtn = (ImageButton) view.findViewById(R.id.notificationBtn);
        notificationBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                popup = new PopupNotification(getApplicationContext(), view, view.getWidth() * 3 / 4, view.getWidth() * 3 / 4, 0);
                popup.show();
            }
        });

        // mark activity is running
        active = true;

    }

    private void setupDrawer(){
        mDrawerView
                .addView(new DrawerHeader())
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_HOME))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_LESSON_HISTORY))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_NOTIFICATIONS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_STUDENTS))
                .addView(new DrawerMenuItem(this.getApplicationContext(), DrawerMenuItem.DRAWER_MENU_ITEM_FINANCES));


    }

    private class DrawerCallBack implements DrawerMenuItem.IDrawerCallBack{

        Fragment fragment;

        public void doTransaction(Fragment fragment){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.drawer_container, fragment)
                .commit();
        }

        @Override
        public void onHomeMenuSelected() {
            fragment = CalendarFragment.newInstance();
            doTransaction(fragment);
        }

        @Override
        public void onLessonHistoryMenuSelected() {
            fragment = LessonHistoryFragment.newInstance();
            doTransaction(fragment);
        }

        @Override
        public void onNotificationsSelected() {
            fragment = NotificationFragment.newInstance();
            doTransaction(fragment);
        }

        @Override
        public void onStudentsSelected() {
            fragment = ViewStudentFragment.newInstance();
            doTransaction(fragment);
        }

        @Override
        public void onFinancesSelected() {
            // add stuff
        }
    }

    public void load() {
        // init class and students presets
        try {
            LessonPresetMap.mapDir = new File(getFilesDir(), "/map");
            LessonPresetMap.mapDir.mkdirs();
            LessonPresetMap.map = LessonPresetMap.init("lessons.map");
            StudentPresetMap.mapDir = new File(getFilesDir(), "/map");
            StudentPresetMap.mapDir.mkdirs();
            StudentPresetMap.map = StudentPresetMap.init("students.map");
            LessonHistoryMap.mapDir = new File(getFilesDir(), "/map");
            LessonHistoryMap.mapDir.mkdirs();
            LessonHistoryMap.map = LessonHistoryMap.init("history.map");

            if (!BackgroundNotification.initialized) {
                Log.v("BackgroundNotification", "not initialized");
                (new BackgroundNotification()).onReceive(this, new Intent().setAction("android.intent.action.BOOT_COMPLETED"));
            }
            while (CalendarMap.isInitializing || MinuteUpdater.calendarMap == null) {
                Thread.sleep(10);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void test() {
        try {
            // for testing
            if (MinuteUpdater.calendarMap.get("6-2017") != null && !MinuteUpdater.calendarMap.isEmpty()) {
                Log.v("CalendarMap", "Retrieved stored month");
            }
            // to remove all the stuff we have been adding
//            CalendarMap.map.clear();
//            MonthMap testMonth = new MonthMap("6-2017", CalendarMap.map);
//            final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
//            DayMap testDay = new DayMap(formatter.format(Calendar.getInstance().getTime()), testMonth);
//            Lesson temp = new Lesson("01", "00", "test", 0, testDay);
            // this is for test save only
            MinuteUpdater.calendarMap.save();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
