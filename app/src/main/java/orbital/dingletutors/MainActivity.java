package orbital.dingletutors;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Modification of the Action bar so space isnt wasted
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view =getSupportActionBar().getCustomView();

        // onClickListener for the notification button
        // can change to what we want when decided
        ImageButton notificationBtn = (ImageButton) view.findViewById(R.id.notificationBtn);
        notificationBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "Notification button is clicked", Toast.LENGTH_LONG).show();
            }
        });

        ViewPager pager = (ViewPager) findViewById(R.id.viewPager);
        pager.setAdapter(new CustomPagerAdapter(getSupportFragmentManager()));

        // Adding the buttons
        LinearLayout buttonPanel = (LinearLayout) findViewById(R.id.buttonPanel);
        ButtonCollection collection = new ButtonCollection(buttonPanel, this);

        collection.addButton(pager, "Calendar", 0);
        collection.addButton(pager, "Next", 1);
        collection.finalizeButtons();

    }

    private class CustomPagerAdapter extends FragmentPagerAdapter {

        public CustomPagerAdapter(FragmentManager manager){
            super(manager);
        }

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
