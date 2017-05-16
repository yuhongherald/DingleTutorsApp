package orbital.dingletutors;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.roomorama.caldroid.CaldroidFragment;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    LinearLayout bottomPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // for top panel
        LinearLayout topPanel = (LinearLayout) findViewById(R.id.topPanel);
        ImageView logo = new ImageView(this);
        logo.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 30f
        ));

        logo.setImageResource(R.drawable.dingle);
        topPanel.addView(logo);
        View topBlank = new View(this);
        topBlank.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 50f
        ));
        topPanel.addView(topBlank);
        LinearLayout notifications = new LinearLayout(this);
        // have to add image and number of notifications into grids
        notifications.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 20f
        ));
        ImageView notificationImage = new ImageView(this);
        notificationImage.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        notificationImage.setImageResource(R.drawable.notification);
        notifications.addView(notificationImage);
        topPanel.addView(notifications);
        // just some comment
        // for centre panel
        LinearLayout centrePanel = (LinearLayout) findViewById(R.id.centrePanel);
        LinearLayout buttons = new LinearLayout(this);
        buttons.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 30f
        ));
        centrePanel.addView(buttons);
        LinearLayout noticeBoard = new LinearLayout(this);
        noticeBoard.setLayoutParams(new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.MATCH_PARENT, 70f
        ));
        centrePanel.addView(noticeBoard);

        // for bottomPanel (we change this as needed)
        bottomPanel = (LinearLayout) findViewById(R.id.bottomPanel);
        BaseActivity calendar = new CalendarActivity(this);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.bottomPanel, calendar.getFragment());
        t.commit();

    }
}
