package orbital.dingletutors;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.support.design.widget.TabLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    public static boolean active = false;

    @Override
    public void onStop() {
        try {
            MinuteUpdater.calendarMap.save();
            LessonPresetMap.map.save();
            StudentPresetMap.map.save();
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

        // init class and students presets
        try {
            LessonPresetMap.mapDir = new File(getFilesDir(), "/map");
            LessonPresetMap.mapDir.mkdirs();
            LessonPresetMap.map = LessonPresetMap.init("lessons.map");
            StudentPresetMap.mapDir = new File(getFilesDir(), "/map");
            StudentPresetMap.mapDir.mkdirs();
            StudentPresetMap.map = StudentPresetMap.init("students.map");

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
        // test();

//        Modification of the Action bar so space isnt wasted
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view =getSupportActionBar().getCustomView();

        // tabs for each page
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Calendar"));
        tabLayout.addTab(tabLayout.newTab().setText("Random Tab"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // onClickListener for the notification button
        // can change to what we want when decided
        ImageButton notificationBtn = (ImageButton) view.findViewById(R.id.notificationBtn);
        notificationBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "Notification button is clicked", Toast.LENGTH_LONG).show();
            }
        });

        final ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab){
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // mark activity is running
        active = true;
        // Adding the buttons
//        LinearLayout buttonPanel = (LinearLayout) findViewById(R.id.buttonPanel);
//        ButtonCollection collection = new ButtonCollection(buttonPanel, this);
//
//        collection.addButton(pager, "Calendar", 0);
//        collection.addButton(pager, "Next", 1);
//        collection.finalizeButtons();

    }

    private class CustomPagerAdapter extends FragmentPagerAdapter {

        private CustomPagerAdapter(FragmentManager manager){ super(manager); }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return CalendarFragment.newInstance();
                case 1:
                    return RandomFragment.newInstance("Hi");
                default:
                    return RandomFragment.newInstance("bye");
            }
        }

        @Override
        public int getCount() {
            return 2;
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
