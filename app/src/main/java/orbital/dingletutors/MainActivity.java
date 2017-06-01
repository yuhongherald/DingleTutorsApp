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
import android.widget.LinearLayout;
import android.widget.Toast;
import android.support.design.widget.TabLayout;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeMap;


public class MainActivity extends AppCompatActivity {
    public static boolean active = false;

    @Override
    public void onStop() {
        try {
            CalendarMap.map.save();
            ClassPresetMap.map.save();
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

        try {
            // init class and students presets
            ClassPresetMap.mapDir = new File(getFilesDir(), "/map");
            ClassPresetMap.mapDir.mkdirs();
            ClassPresetMap.map = ClassPresetMap.init("classes.map");
            StudentPresetMap.mapDir = new File(getFilesDir(), "/map");
            StudentPresetMap.mapDir.mkdirs();
            StudentPresetMap.map = StudentPresetMap.init("students.map");


            if (!BackgroundNotification.initialized) {
                Log.v("BackgroundNotification", "not initialized");
                (new BackgroundNotification()).onReceive(this, new Intent().setAction("android.intent.action.BOOT_COMPLETED"));
            }
            while (CalendarMap.isInitializing || CalendarMap.map == null) {
                Thread.sleep(10);
            }
            // for testing
            if (CalendarMap.map.get("5-2017") != null && !CalendarMap.map.isEmpty()) {
                Log.v("CalendarMap", "Retrieved stored month");
            }
            MonthMap testMonth = new MonthMap("6-2017", CalendarMap.map);
            final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
            String dayKey = formatter.format(Calendar.getInstance().getTime());
            testMonth.put(dayKey, new DayMap(dayKey, testMonth));
            CalendarMap.map.put(testMonth.key, testMonth);
            // this is for test save only
            CalendarMap.map.save();

        } catch (Exception e) {
            e.printStackTrace();
        }

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

    // Button methods for XML to access
    public static void addLesson(View v) {
        Log.v("Button", "Add lesson");
        // create a new popup
        // Popup<String> p = new Popup<String>(v.getContext(), v, null, "Add new lesson", R.layout.add_lesson, null);
    }

    public void viewLessons(View v) {
        // I create a popup window and supply it with DayMap
        // and the list element format
        // and the button on click listener
//        Popup popup = new Popup<Integer>(this, v, CalendarFragment.selectedDay,
//                "view lessons", R.layout.view_lesson, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MainActivity.addLesson(v);
//            }
//        });
//        popup.updateList();
//        popup.showPopup();
    }

    public void editLesson(View v) {
        Log.v("Button", "Edit lesson");
        // v.getTag(Tags.data);
    }

    public void removeLesson(View v) {
        Log.v("Button", "Remove lesson");
        // ((TreeMap<String, Bundle>) v.getTag(Tags.parent)).remove((String) v.getTag(Tags.key));
    }

}
