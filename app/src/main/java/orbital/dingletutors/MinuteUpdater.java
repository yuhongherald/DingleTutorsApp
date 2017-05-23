package orbital.dingletutors;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Herald on 22/5/2017.
 */

// Calculates remaining time to next minute and sleeps for that long
public class MinuteUpdater extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("MinuteUpdater","called");

    }
}
