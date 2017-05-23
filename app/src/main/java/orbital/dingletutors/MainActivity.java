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
import java.util.Calendar;


public class MainActivity extends AppCompatActivity {
    public static boolean active = false;

    @Override
    public void onStop() {
        try {
            CalendarMap.map.save();
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
            if (!BackgroundNotification.initialized) {
                Log.v("BackgroundNotification", "not initialized");
                (new BackgroundNotification()).onReceive(this, new Intent().setAction("android.intent.action.BOOT_COMPLETED"));
            }
            while (CalendarMap.isInitializing || CalendarMap.map == null) {
                Thread.sleep(10);
            }
            // for testing
            if (CalendarMap.map.get("5-2017") != null) {
                Log.v("CalendarMap", "Retrieved stored month");
            }
            MonthMap testMonth = new MonthMap();
            testMonth.put(Calendar.getInstance().getTime(), new DayMap());
            CalendarMap.map.put("5-2017", testMonth);
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
}
