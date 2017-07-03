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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by Herald on 22/5/2017.
 */

// Calculates remaining time to next minute and sleeps for that long
public class MinuteUpdater extends BroadcastReceiver {
    public static boolean mainAppRunning = false;
    public static CalendarMap calendarMap;
    public static MinuteQueue minuteQueue;
    public static Context context;
    public static boolean isInitializing = false;

    public static File mapDir;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("MinuteUpdater","called");
        //createNotification(context, null);
        if (calendarMap == null || minuteQueue == null || context == null) {
            // my stuff keeps getting garbage collected, somehow this only needs to be done once
            Log.v("MinuteUpdater", "retrieving garbage collected stuff");
            loadMap(context);
        }
        if (calendarMap != null) {
            // we calculate
            boolean loop = true;
            Date previous = minuteQueue.lastUpdated;
            Date current;
            while (loop) {
                // this ensures that dates missed when powered off is made up for
                checkDate(previous);
                current = Calendar.getInstance().getTime();
                if (previous.after(current) ||
                        CalendarFragment.formatter.format(previous).equals(
                        CalendarFragment.formatter.format(current))) {
                    // can stop when is after or is the same day
                    loop = false;
                } else {
                    previous.setTime(previous.getTime() + (24 * 60 * 60 * 1000));
                }
            }
            minuteQueue.lastUpdated = Calendar.getInstance().getTime();
        }

        int size = minuteQueue.size();
        // we create 1 notification with number of lessons
        if (size > 0) {
            Lesson firstLesson = minuteQueue.get(0);
            createNotification(context, firstLesson, firstLesson.minutesBefore(), size);
        }
//        for (Lesson lesson : MinuteQueue.map) {
//            createNotification(context, lesson, MinuteQueue.map.minutesBefore(lesson));
//        }
        // Log.v("MinuteUpdater","no updates");
    }

    public static void loadMap(Context context) {
        MinuteUpdater.context = context;
        if (mapDir == null) {
            mapDir = new File(context.getFilesDir(), "/map");
            mapDir.mkdirs();
        }
        isInitializing = true;
        try {
            minuteQueue = MinuteQueue.init("queue.map");
            calendarMap = CalendarMap.init("notifications.map");
        } catch (Exception e) {
            e.printStackTrace();
        }
        isInitializing = false;
    }

    private static void checkDate(Date rawDate) {
        String date = new SimpleDateFormat("mmHHddMMYYYY").format(rawDate);
        MonthMap month = calendarMap.get(Integer.parseInt(date.substring(6, 8)) + "-" +
                                             Integer.parseInt(date.substring(8, 12)));
        if (month != null) {
            DayMap day = month.get(CalendarFragment.formatter.format(rawDate));
            if (day != null) {
                Log.v("Day", "lesson present");
                // Check all lessons on the day itself
                Set<Map.Entry<Integer, Lesson>> set = day.entrySet();
                Lesson lesson;
                for (Map.Entry<Integer, Lesson> entry : set) {
                    lesson = entry.getValue();
                    if (lesson.minutesBefore() <= minuteQueue.advMinutes) {
                        Log.v("Lesson", "added to queue");
                        minuteQueue.add(lesson);
                    }
                }
//                // have to set advanced reminder here
//                Lesson lesson = day.get(Integer.parseInt(date.substring(2, 4)) * 60 +
//                                        Integer.parseInt(date.substring(0, 2)));
//                if (lesson != null) {
//                    // use methods defined to supply notification with info
//                    Log.v("Lesson", "added to queue");
//                    queue.add(lesson);
//                }
            }
        }
    }

    private void createNotification(Context context, Lesson lesson, long minutes, int lessons) {
        if (mainAppRunning) {
            Intent i = new Intent(context, MainActivity.class);
            i.setAction("orbital.dingletutors.UPDATE_MAIN");
            context.sendBroadcast(i);
            return;
        }
        String message = "";
        if (minutes < 0) {
            minutes = - minutes;
            message = "You are " + Long.toString(minutes) + " minutes late for your lesson!";
        } else {
            message = "Your lesson is in " + Long.toString(minutes) + " minutes!";
        }
        message += "You have " + lessons + " lessons waiting!";
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.dingle) // have to change this probably
                        .setContentTitle(lesson.name)
                        .setContentText(message);
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
