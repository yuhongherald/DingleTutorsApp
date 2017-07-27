package orbital.dingletutors;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TreeMap;

/**
 * Created by Herald on 31/5/2017.
 */

public class Lesson implements Serializable {

    public static final long serialVersionUID = 1004L;
    public DayMap parent;
    public int time; // in minutes, also the key
    public Date lessonDate;
    public int hours;
    public int minutes;
    public String displayTime;
    public int duration;
    public String name;
    public String level;
    public ArrayList<Student> students;
    public ArrayList<String> summaryReport;
    public boolean checkedIn;
    public RecurringLesson recurringLesson;

    public Date nextReportCheck;

    Lesson(int hours,int minutes, @NonNull DayMap parent) {
        this.time = hours * 60 + minutes;
        this.hours = hours;
        this.minutes = minutes;
        this.displayTime = String.format("%02d", hours) + ":" + String.format("%02d", minutes);
        this.displayTime += (hours >= 12)
                ? "PM" : "AM";
        this.parent = parent;
        this.name = NewLessonFragment.subjectNames[0];
        this.level = NewLessonFragment.educationLevels[0];
        this.duration = NewLessonFragment.durationStringToInt.get(NewLessonFragment.durations[0]);
        this.checkedIn = false;
        this.summaryReport = new ArrayList<>();
        try {
            this.lessonDate = RecurringLesson.addTime(this.time, CalendarFragment.formatter.parse(parent.key));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.students = new ArrayList<>();
        parent.put(this.time, this);
    }

    public static Lesson init(Date date) {
        String stringDate = new SimpleDateFormat("mmHHddMMyyyy").format(date);
        MonthMap monthMap = MonthMap.init(Integer.parseInt(stringDate.substring(6, 8)) + "-" +
                Integer.parseInt(stringDate.substring(8, 12)), MinuteUpdater.calendarMap);
        DayMap dayMap = DayMap.init(CalendarFragment.formatter.format(date), monthMap);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Lesson lesson = dayMap.get(calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE));
        if (lesson != null) {
            Log.v("expected", "expected");
            return lesson;
        } else {
            lesson = new Lesson(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), dayMap);
            if (remap(date) == null) {
                Log.v("unexpected", "unexpected");
            }
            return lesson;
        }
    }

    public boolean delete() {
        boolean result = this.parent.remove(this.time) != null;
        if (this.recurringLesson != null) {
            this.recurringLesson.remove(this);
        }
        if (this.parent.isEmpty()) {
            this.parent.delete();
        }
//        if (MinuteUpdater.minuteQueue != null) {
//            MinuteUpdater.minuteQueue.remove(this);
//        }

        return result;
    }

    public boolean deleteAll() {
        if (this.recurringLesson != null) {
            return this.recurringLesson.delete();
        }
        return false;
    }

    public void checkIn(Context context) {
        if (checkedIn) {
            Log.v("Check-In", "Attempting to check in a lesson which has already been checked in");
            return;
        }
        // put into lesson history map and remove from calendar map
        delete();
        if (MinuteUpdater.lessonHistoryMap == null) {
            MinuteUpdater.loadMap(context);
        }
        nextReportCheck = RecurringLesson.addTime(duration, Calendar.getInstance().getTime());
        MinuteUpdater.lessonHistoryMap.add(this);
        MinuteUpdater.lessonWithoutReports.add(this);
        checkedIn = true;
    }

    // delete, init a new one and add the fields in
    public static Lesson remap(Date date) {
        String stringDate = new SimpleDateFormat("mmHHddMMyyyy").format(date);
        MonthMap monthMap = MonthMap.init(Integer.parseInt(stringDate.substring(6, 8)) + "-" +
                Integer.parseInt(stringDate.substring(8, 12)), MinuteUpdater.calendarMap);
        DayMap dayMap = DayMap.init(CalendarFragment.formatter.format(date), monthMap);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Lesson lesson = dayMap.get(calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE));
        return lesson;
    }

    // in case you want to use this
    public boolean removeRecurring() {
        if (recurringLesson == null) {
            return false;
        }
        return recurringLesson.removeRecurring();
    }
    public boolean areSummaryReportsComplete(){
        return this.summaryReport.size() == this.students.size();
    }

    /**
     * For writing of next summary report
     */
    public Student getNextStudent(){
        return this.students.get(this.summaryReport.size());
    }

    public long minutesBefore() {
        // we need a date for the lesson
//        Date lessonDate = null;
//        try {
//            lessonDate = CalendarFragment.formatter.parse(this.parent.key);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        long diff = lessonDate.getTime()
//                + (this.time * 60 * 1000)
                - Calendar.getInstance().getTime().getTime();
        long diffMinutes = diff / 60 / 1000;
        return diffMinutes;
    }

    public void setSummaryReport(@NonNull String summaryReport){
        this.summaryReport.add(summaryReport);
    }

    public String getSummaryReport(int pos){
        return this.summaryReport.get(pos);

    }
}
