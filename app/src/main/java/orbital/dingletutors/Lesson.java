package orbital.dingletutors;

import android.app.Activity;
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
//    public static final String[] indexes = {"time", "displayTime", "name", "level"};
//    public static final String[] levels = {"default", "another one", "try me"};

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

    Lesson(int hours,int minutes, @NonNull DayMap parent) {
        this.time = hours * 60 + minutes;
//        this.time = 0;
        this.hours = hours;
        this.minutes = minutes;
        this.displayTime = String.format("%02d", hours) + ":" + String.format("%02d", minutes);
        this.parent = parent;
        this.name = NewLessonFragment.subjectNames[0];
        this.level = NewLessonFragment.educationLevels[0];
        this.duration = NewLessonFragment.durationStringToInt.get(NewLessonFragment.durations[0]);
//        try {
//            this.lessonDate = CalendarFragment.formatter.parse(parent.key);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        this.students = new ArrayList<Student>();
        parent.put(this.time, this);
    }

    public static Lesson init(Date date) {
        String stringDate = new SimpleDateFormat("mmHHddMMYYYY").format(date);
        MonthMap monthMap = MonthMap.init(Integer.parseInt(stringDate.substring(6, 8)) + "-" +
                Integer.parseInt(stringDate.substring(8, 12)), MinuteUpdater.calendarMap);
        DayMap dayMap = DayMap.init(CalendarFragment.formatter.format(date), monthMap);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Lesson lesson = dayMap.get(calendar.get(Calendar.HOUR) * 60 + calendar.get(Calendar.MINUTE));
        if (lesson != null) {
            return lesson;
        } else {
            return new Lesson(calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), dayMap);
        }
    }

    public boolean delete() {
        boolean result = this.parent.remove(this.time) != null;
        if (this.parent.isEmpty()) {
            this.parent.delete();
        }
        return result;
    }
    // delete, init a new one and add the fields in
    public static Lesson remap(Date date) {
        String stringDate = new SimpleDateFormat("mmHHddMMYYYY").format(date);
        MonthMap monthMap = MonthMap.init(Integer.parseInt(stringDate.substring(6, 8)) + "-" +
                Integer.parseInt(stringDate.substring(8, 12)), MinuteUpdater.calendarMap);
        DayMap dayMap = DayMap.init(CalendarFragment.formatter.format(date), monthMap);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Lesson lesson = dayMap.get(calendar.get(Calendar.HOUR) * 60 + calendar.get(Calendar.MINUTE));
        return lesson;
    }
//    public String remap(int hours,int minutes, int duration, @NonNull String name, @NonNull String level, ArrayList<Student> students) {
//        this.delete();
//        this.time = hours * 60 + minutes;
//        Lesson temp = parent.get(this.time);
//        if (temp == null) {
//            parent.put(this.time, this);
//        } else {
//            Log.v("Lesson", "conflict");
//            return temp.name;
//        }
//        this.hours = hours;
//        this.minutes = minutes;
//        this.name = name;
//        this.level = level;
//        this.duration = duration;
//        this.students = students;
//        return null;
//    }

    public long minutesBefore() {
        // we need a date for the lesson
        Date lessonDate = null;
        try {
            lessonDate = CalendarFragment.formatter.parse(this.parent.key);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = lessonDate.getTime() + (this.time * 60 * 1000) - Calendar.getInstance().getTime().getTime();
        long diffMinutes = diff / 60 / 1000;
        return diffMinutes;
    }

//    @Override
//    public Student put(String key, Student value) {
//        while (CalendarMap.updating) {
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        CalendarMap.updating = true;
//        Student temp = super.put(key, value);
//        CalendarMap.updating = false;
//        return temp;
//    }
//
//    @Override
//    public Student get(Object key) {
//        while (CalendarMap.updating) {
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        CalendarMap.updating = true;
//        Student temp = super.get(key);
//        CalendarMap.updating = false;
//        return temp;
//    }
//
//    @Override
//    public Student remove(Object key) {
//        while (CalendarMap.updating) {
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        CalendarMap.updating = true;
//        Student temp = super.remove(key);
//        CalendarMap.updating = false;
//        return temp;
//    }

}
