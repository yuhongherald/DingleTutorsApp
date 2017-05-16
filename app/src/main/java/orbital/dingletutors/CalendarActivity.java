package orbital.dingletutors;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.roomorama.caldroid.CaldroidFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Herald on 16/5/2017.
 */

public class CalendarActivity implements BaseActivity {
    CaldroidFragment caldroidFragment;

    public CalendarActivity(Context context) {
        caldroidFragment = new CaldroidFragment();
        Calendar calendar = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt(CaldroidFragment.MONTH, calendar.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, calendar.get(Calendar.YEAR));
        caldroidFragment.setArguments(args);

        // have to color days that have notifications. Should we:
        // 1 color only when month is displayed
        // 2 color everything on init

    }

    @Override
    public void save() {

    }

    @Override
    public void load() {
        // need to add/remove dates accordingly
        caldroidFragment.refreshView();
    }

    @Override
    public Fragment getFragment() {
        return caldroidFragment;
    }
}
