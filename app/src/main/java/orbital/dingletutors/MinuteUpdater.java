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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by Herald on 22/5/2017.
 */

public class MinuteUpdater extends BroadcastReceiver {
    public static final int lessonCode = 1337;
    public static final int reportCode = 1338;
    public static final String lessonIntent = "lesson";
    public static final String reportIntent = "report";

    public static boolean mainAppRunning = false;
    public static CalendarMap calendarMap;
    public static MinuteQueue minuteQueue;
    public static RecurringLessonMap recurringLessonMap;
    public static Context context;
    public static boolean isInitializing = false;
    public static boolean isRunning = false;

    public static File mapDir;
    public static LessonPresetMap lessonPresetMap;
    public static LessonHistoryMap lessonHistoryMap;
    public static LessonHistoryMap lessonWithoutReports;
    public static StudentPresetMap studentPresetMap;

    public static final int nextDay = 24 * 60 * 60 * 1000;
    public static final int reportInterval = 60;
    public static final int advMinutes = 30;
    public static final int advMS = advMinutes * 60 * 1000;
    @Override
    public void onReceive(Context context, Intent intent) {
        isRunning = true;
        Log.v("MinuteUpdater","called");
        //lessonNotification(context, null);
        if (calendarMap == null || minuteQueue == null || lessonHistoryMap == null || this.context == null) {
            // my stuff keeps getting garbage collected, somehow this only needs to be done once
            Log.v("MinuteUpdater", "retrieving garbage collected stuff");
            loadMap(context);
        }
        lessonHistoryMap.updateHistory();
        // lessonWithoutReports.updateHistory(); // currently no support for notificationfragment

        Date currentDate = Calendar.getInstance().getTime();
        ArrayList<Lesson> reportLessons = (ArrayList<Lesson>) lessonWithoutReports.clone();
        Lesson pickedLesson = null;
        for (Lesson lesson : reportLessons) {
            if (lesson.nextReportCheck != null && lesson.nextReportCheck.before(currentDate)) {
                Log.v("Report", "upcoming");
                lesson.nextReportCheck = RecurringLesson.addTime(reportInterval, lesson.nextReportCheck);
                pickedLesson = lesson;
            }
        }

        if (pickedLesson != null) {
            reportNotification(context, pickedLesson);
        }

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
                previous.setTime(previous.getTime() + nextDay);
            }
        }
        minuteQueue.lastUpdated = Calendar.getInstance().getTime();

        ArrayList<Lesson> lessons = getLessons();
        if (!lessons.isEmpty()) {
            lessonNotification(context, lessons.get(0));
        }

        saveMap(); // inefficient saving!
        isRunning = false;
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
            lessonHistoryMap = LessonHistoryMap.init("history.map");
            lessonWithoutReports = LessonHistoryMap.init("reports.map");
        } catch (Exception e) {
            e.printStackTrace();
        }
        isInitializing = false;
    }

    public static void saveMap() {
        try {
            minuteQueue.save();
            calendarMap.save();
            lessonHistoryMap.save();
            lessonWithoutReports.save();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void lessonNotification(Context context, Lesson lesson) {
//        if (mainAppRunning) {
//            Intent i = new Intent(context, MainActivity.class);
//            i.setAction("orbital.dingletutors.UPDATE_MAIN");
//            context.sendBroadcast(i);
//            return;
//        }
        String message = "Your " + lesson.name + " lesson is in " + lesson.minutesBefore() + " minutes!";

        createNotification(lesson, message, context, lessonIntent, lessonCode);
    }

    private void reportNotification(Context context, Lesson lesson) {
        String message = "Your " + lesson.name + " has incomplete reports.";
        createNotification(lesson, message, context, reportIntent, reportCode);
    }

    private void createNotification(Lesson lesson, String message, Context context, String extra, int code) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.dingle) // have to change this probably
                        .setContentTitle(lesson.name)
                        .setContentText(message);
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setAction("android.intent.action.MAIN");
        resultIntent.putExtra(MainActivity.intent, extra);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT // not sure what flag to put here
                );
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(code, mBuilder.build());
        // added for ringtone notification
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void checkDate(Date rawDate) {
        Calendar calendar = Calendar.getInstance();
        MonthMap month = MinuteUpdater.calendarMap.get((calendar.get(Calendar.MONTH) + 1)+ "-" +
                calendar.get(Calendar.YEAR));

        ArrayList<Lesson> lessons = new ArrayList<>();
        if (month != null) {
            getDayLessons(lessons, month.get(CalendarFragment.formatter.format(rawDate)));
        }
    }

    public static ArrayList<Lesson> getLessons() {
        Calendar calendar = Calendar.getInstance();
        MonthMap month = MinuteUpdater.calendarMap.get((calendar.get(Calendar.MONTH) + 1)+ "-" +
                calendar.get(Calendar.YEAR));

        ArrayList<Lesson> lessons = new ArrayList<>();
        if (month != null) {
            Date date = calendar.getTime();
            getDayLessons(lessons, month.get(CalendarFragment.formatter.format(date)));
            Date secondDate = new Date();
            secondDate.setTime(date.getTime() + advMS); // in case it spans over a day
            if (!CalendarFragment.formatter.format(date).equals(CalendarFragment.formatter.format(secondDate))) {
                getDayLessons(lessons, month.get(CalendarFragment.formatter.format(date)));
            }
        }
        if (NotificationFragment.imgNotification != null) {
            if (lessons.isEmpty()) {
                NotificationFragment.imgNotification.setImageDrawable(null);
            } else {
                NotificationFragment.imgNotification.setImageDrawable(NotificationFragment.img);
            }
        }
        int size = lessons.size();
        if (MainActivity.notificationCount != null) {
            MainActivity.notificationCount.setText(Integer.toString(size));
        }
        if (MainActivity.oldNotificationCount != null) {
            MainActivity.oldNotificationCount.setText(Integer.toString(size));
        }
        return lessons;
    }

    private static void getDayLessons(ArrayList<Lesson> lessons, DayMap day) {
        if (day != null) {
            // Check all lessons on the day itself
            CalendarMap.updating = true;
            Set<Map.Entry<Integer, Lesson>> set = day.entrySet();
            ArrayList<Map.Entry<Integer, Lesson>> tempSet = new ArrayList<Map.Entry<Integer, Lesson>>(set);
            CalendarMap.updating = false;
            Lesson lesson;

            boolean initiallyEmpty = day.isEmpty();

            for (Map.Entry<Integer, Lesson> entry : tempSet) {
                lesson = entry.getValue();
                if (lesson.minutesBefore() < 0) {
                    Log.v("Lesson expired", lesson.name);
                    lesson.delete();
                } else if (lesson.minutesBefore() <= advMinutes) {
                    lessons.add(lesson);
                }
            }
            if (day.isEmpty() != initiallyEmpty && CalendarFragment.thisFragment != null) {
                try {
                    CalendarFragment.thisFragment.recolorDay(CalendarFragment.formatter.parse(day.key));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
