package orbital.dingletutors;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Herald on 22/5/2017.
 */

// Calculates remaining time to next minute and sleeps for that long
public class MinuteUpdater extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("MinuteUpdater","called");
        createNotification(context, null);
        if (CalendarMap.map != null) {
            String date = new SimpleDateFormat("mmHHddMMYYYY").format(Calendar.getInstance().getTime());
            MonthMap month = CalendarMap.map.get(Integer.parseInt(date.substring(6, 8)) + "-" +
                                                 Integer.parseInt(date.substring(8, 12)));
            if (month != null) {
                DayMap day = month.get(date.substring(4, 6));
                if (day != null) {
                    Object event = day.get(date.substring(2, 4) + ":" + date.substring(0, 2));
                    if (event != null) {
                        // use methods defined to supply notification with info
                        // createNotification(context, event);
                    }
                }
            }
        }
        Log.v("MinuteUpdater","no updates");
    }

    private void createNotification(Context context, Object event) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.dingle)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setAction("android.intent.action.MAIN");
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(0, mBuilder.build());
    }
}
