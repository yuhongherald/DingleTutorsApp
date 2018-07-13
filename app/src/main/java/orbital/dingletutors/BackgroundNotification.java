package orbital.dingletutors;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Herald on 22/5/2017.
 */

public class BackgroundNotification extends BroadcastReceiver {
    public static boolean initialized = false;
    public static MinuteUpdater updater;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (initialized == true) {
            Log.v("BackgroundNotification", "Multiple initialzation.");
            return;
        }
        Log.v("BackgroundNotification", intent.getAction());
        initialized = true;
        Log.v("BroadcastReceiver", "Initialized");
        MinuteUpdater.loadMap(context);

        Intent newIntent = new Intent(context, MinuteUpdater.class);
        intent.setAction("orbital.dingletutors.MINUTE_ACTION");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //alarm.cancel(pendingIntent);
        // somehow this thing is not very precise
        alarm.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000*60, pendingIntent);
        Log.v("BackgroundNotification", "Done");
    }

}
