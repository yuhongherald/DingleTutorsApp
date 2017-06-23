package orbital.dingletutors;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.util.Calendar;

/**
 * Created by Herald on 22/5/2017.
 */

public class BackgroundNotification extends BroadcastReceiver {
    public static boolean initialized = false;
    public static MinuteUpdater updater;
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != "android.intent.action.BOOT_COMPLETED") {
//            Log.v("BackgroundNotification", intent.getAction());
            return;
        }
        initialized = true;
        Log.v("BroadcastReceiver", "Initialized");
        MinuteQueue.mapDir = new File(context.getFilesDir(), "/map");
        MinuteQueue.mapDir.mkdirs();
        CalendarMap.mapDir = new File(context.getFilesDir(), "/map");
        CalendarMap.mapDir.mkdirs();
        CalendarMap.isInitializing = true;
        try {
            MinuteUpdater.minuteQueue = MinuteQueue.init("queue.map");
            MinuteUpdater.calendarMap = CalendarMap.init("notifications.map");
        } catch (Exception e) {
            e.printStackTrace();
        }
        CalendarMap.isInitializing = false;

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
    }
}
