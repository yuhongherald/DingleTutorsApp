package orbital.dingletutors;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

/**
 * Created by Herald on 31/5/2017.
 */

public class Lesson extends TreeMap<String, Student> {
    public static final String[] indexes = {"time", "displayTime", "name", "level"};
    public static final String[] levels = {"default"};

    public final DayMap parent;
    public final int time; // in minutes, also the key

    public final String displayTime; // in xx:yy 24h
    public final String name;
    public final int level;

    Lesson(int hours, int minutes, String name, int level, DayMap parent) {
        this.time = hours * 60 + minutes;
        this.displayTime = hours + ":" + minutes;
        this.name = name;
        this.level = level;
        this.parent = parent;
        if (parent.get(this.time) == null) {
            parent.put(this.time, this);
        } else {
            Log.v("Lesson", "conflict");
        }
    }

    public boolean delete() {
        return (this.parent.remove(this) != null);
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
