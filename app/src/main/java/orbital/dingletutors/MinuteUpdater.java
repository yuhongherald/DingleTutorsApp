package orbital.dingletutors;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Herald on 22/5/2017.
 */

// Calculates remaining time to next minute and sleeps for that long
public class MinuteUpdater extends BroadcastReceiver {
    public static ArrayList<Lesson> queue = new ArrayList<Lesson>();
    public static boolean updating = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("MinuteUpdater","called");
        //createNotification(context, null);
        if (CalendarMap.map != null) {
            Date rawDate = Calendar.getInstance().getTime();
            String date = new SimpleDateFormat("mmHHddMMYYYY").format(rawDate);
            MonthMap month = CalendarMap.map.get(Integer.parseInt(date.substring(6, 8)) + "-" +
                                                 Integer.parseInt(date.substring(8, 12)));
            if (month != null) {
                DayMap day = month.get(CalendarFragment.formatter.format(rawDate));
                if (day != null) {
                    Log.v("Day", "lesson present");
                    // have to set advanced reminder here
                    Lesson lesson = day.get(Integer.parseInt(date.substring(2, 4)) * 60 +
                                            Integer.parseInt(date.substring(0, 2)));
                    if (lesson != null) {
                        // use methods defined to supply notification with info
                        Log.v("Lesson", "added to queue");
                        lesson.countdown = 0;
                        queue.add(lesson);
                    }
                }
            }
        }
        for (Lesson lesson : queue) {
            createNotification(context, lesson, lesson.countdown);
            if (lesson.countdown > 0) {
                lesson.countdown--;
            }
        }
        // Log.v("MinuteUpdater","no updates");
    }

    private void createNotification(Context context, Lesson lesson, int minutes) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.dingle) // have to change this probably
                        .setContentTitle(lesson.name)
                        .setContentText("Your lesson is in " + minutes + " minutes!");
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setAction("android.intent.action.MAIN");
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT // not sure what flag to put here
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(0, mBuilder.build());
        // added for ringtone notification
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
