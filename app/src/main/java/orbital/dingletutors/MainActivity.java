package orbital.dingletutors;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mindorks.placeholderview.PlaceHolderView;

import java.io.File;


public class MainActivity extends AppCompatActivity {
    public static boolean active = false;
    private Popup popup;
    public static TextView notificationCount;
    public static TextView oldNotificationCount;

    public String[] categoryTitles = new String[]{"Home", "Lesson History", "Notifications", "Students", "Finances"};
    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private DrawerCallBack mCallBack;

    // don't really need this anymore
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
//            Log.v("Notification", "attempting update");
//            int size = MinuteUpdater.getLessons().size();
//            if (notificationCount != null) {
//                Log.v("Notification", "attempting update");
//                notificationCount.setText(size);
//            }
//            if (oldNotificationCount != null) {
//                notificationCount.setText(size);
//            }
//            if (popup != null && popup.isVisible()) {
//                popup.updateList();
//            }
        }
    };

    @Override
    protected void onResume() {
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(MinuteUpdater.notificationCode);

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
            MinuteUpdater.minuteQueue.save();
            MinuteUpdater.recurringLessonMap.save();
            MinuteUpdater.lessonPresetMap.save();
            MinuteUpdater.studentPresetMap.save();
            MinuteUpdater.lessonHistoryMap.save();
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
        NotificationFragment.img = ContextCompat.getDrawable(this, R.drawable.notification);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        final View view = getSupportActionBar().getCustomView();

        oldNotificationCount = (TextView) view.findViewById(R.id.notificationCount);
        // tabs for each page
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
//        tabLayout.addTab(tabLayout.newTab().setText("Calendar"));
//        tabLayout.addTab(tabLayout.newTab().setText("Lesson History"));
//        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

//        drawerLayout =  (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawerList = (ListView) findViewById(R.id.left_drawer);
////
//        drawerList.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.drawer_list, categoryTitles));
//        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        MinuteUpdater.getLessons(); // force a load
        mDrawer = (DrawerLayout)findViewById(R.id.drawerLayout);
        mDrawerView = (PlaceHolderView)findViewById(R.id.drawerView);

        setupDrawer();
        mCallBack = new DrawerCallBack();
        DrawerMenuItem.setDrawerCallBack(mCallBack);

//        Button checkinBtn =(Button) view.findViewById(R.id.checkinBtn);
//        checkinBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Popup popup = new PopupCheckIn(getApplicationContext(), view, view.getWidth() * 3 / 4, view.getWidth() * 3 / 4, 0);
//                popup.show();
//            }
//        });
        ImageButton notificationBtn = (ImageButton) view.findViewById(R.id.notificationBtn);
        notificationBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mCallBack.onNotificationsSelected();

//                popup = new PopupNotification(getApplicationContext(), view, view.getWidth() * 3 / 4, view.getWidth() * 3 / 4, 0);
//                popup.show();
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && "MinuteUpdater".equals(bundle.get("MainActivity"))) {
            mCallBack.onNotificationsSelected();
        } else {
            mCallBack.onHomeMenuSelected();
        }

        // request permission for sms
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS},1);

//        final ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
//        pager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));
//
//        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
//            @Override
//            public void onTabSelected(TabLayout.Tab tab){
//                pager.setCurrentItem(tab.getPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });

        // mark activity is running
        active = true;

    }

    private void setupDrawer(){
        mDrawerView
//                .addView(new DrawerHeader())
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

//    private class CustomPagerAdapter extends FragmentPagerAdapter {
//
//        private CustomPagerAdapter(FragmentManager manager){ super(manager); }
//
//        @Override
//        public Fragment getItem(int position) {
//            switch(position) {
//                case 0:
//                    return CalendarFragment.newInstance();
//                case 1:
//                    return LessonHistoryFragment.newInstance();
//                default:
//                    return LessonHistoryFragment.newInstance();
//            }
//        }
//
//        @Override
//        public int getCount() {
//            return 2;
//        }
//    }

    public void load() {
        // init class and students presets
        try {
            if (MinuteUpdater.mapDir == null) {
                MinuteUpdater.mapDir = new File(getFilesDir(), "/map");
                MinuteUpdater.mapDir.mkdirs();
            }
            MinuteUpdater.recurringLessonMap = RecurringLessonMap.init("recurringLessons.map");
            MinuteUpdater.lessonPresetMap = LessonPresetMap.init("lessons.map");
            MinuteUpdater.studentPresetMap = StudentPresetMap.init("students.map");
            MinuteUpdater.lessonHistoryMap = LessonHistoryMap.init("history.map");

            if (!BackgroundNotification.initialized) {
                Log.v("BackgroundNotification", "not initialized");
                (new BackgroundNotification()).onReceive(this, new Intent().setAction("android.intent.action.BOOT_COMPLETED"));
            }
            while (MinuteUpdater.isInitializing || MinuteUpdater.calendarMap == null) {
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
