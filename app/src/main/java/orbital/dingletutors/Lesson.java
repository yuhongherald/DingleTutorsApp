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

public class Lesson extends TreeMap<String, Student> {
    public static final String[] indexes = {"time", "displayTime", "name", "level"};
    public static final String[] levels = {"default", "another one", "try me"};

    public DayMap parent;
    public int time; // in minutes, also the key

    public String hours;
    public String minutes;
    public String name;
    public int level;

    Lesson(String hours, String minutes, String name, int level, @NonNull DayMap parent) {
        if (hours != null && minutes != null) {
            this.time = Integer.parseInt(hours) * 60 + Integer.parseInt(minutes);
            if (parent.get(this.time) == null) {
                parent.put(this.time, this);
            } else {
                Log.v("Lesson", "conflict");
            }
        }
        this.time = 0;
        this.hours = hours;
        this.minutes = minutes;
        this.name = name;
        this.level = level;
        this.parent = parent;
    }

    public boolean delete() {
        return this.parent.remove(this.time) != null;
    }

    public String remap(@NonNull String hours,@NonNull String minutes,@NonNull String name, int level) {
        this.delete();
        this.time = Integer.parseInt(hours) * 60 + Integer.parseInt(minutes);
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

    @Override
    public Student put(String key, Student value) {
        while (CalendarMap.updating) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        CalendarMap.updating = true;
        Student temp = super.put(key, value);
        CalendarMap.updating = false;
        return temp;
    }

    @Override
    public Student get(Object key) {
        while (CalendarMap.updating) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        CalendarMap.updating = true;
        Student temp = super.get(key);
        CalendarMap.updating = false;
        return temp;
    }

    @Override
    public Student remove(Object key) {
        while (CalendarMap.updating) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        CalendarMap.updating = true;
        Student temp = super.remove(key);
        CalendarMap.updating = false;
        return temp;
    }

}