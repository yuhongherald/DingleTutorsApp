package orbital.dingletutors;

import android.support.annotation.NonNull;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TreeMap;

/**
 * Created by Herald on 31/5/2017.
 */

public class Lesson{
//    public static final String[] indexes = {"time", "displayTime", "name", "level"};
//    public static final String[] levels = {"default", "another one", "try me"};

    public DayMap parent;
    public int time; // in minutes, also the key
    public Date lessonDate;
    public int hours;
    public int minutes;
    public int duration;
    public String name;
    public String level;
    public Student[] students;

    Lesson(int hours,int minutes, int duration, @NonNull String name, @NonNull String level, Student[] students, @NonNull DayMap parent) {
        this.time = hours * 60 + minutes;
        if (parent.get(this.time) == null) {
            parent.put(this.time, this);
        } else {
            Log.v("Lesson", "conflict");
        }
//        this.time = 0;
        this.hours = hours;
        this.minutes = minutes;
        this.name = name;
        this.level = level;
        this.parent = parent;
        this.duration = duration;
        try {
            this.lessonDate = CalendarFragment.formatter.parse(parent.key);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.students = students;
    }

    public boolean delete() {
        return this.parent.remove(this.time) != null;
    }

    public String remap(int hours,int minutes, int duration, @NonNull String name, @NonNull String level, Student[] students) {
        this.delete();
        this.time = hours * 60 + minutes;
        Lesson temp = parent.get(this.time);
        if (temp == null) {
            parent.put(this.time, this);
        } else {
            Log.v("Lesson", "conflict");
            return temp.name;
        }
        this.hours = hours;
        this.minutes = minutes;
        this.name = name;
        this.level = level;
        this.duration = duration;
        this.students = students;
        return null;
    }

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
