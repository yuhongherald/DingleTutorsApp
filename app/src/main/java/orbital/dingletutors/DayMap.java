package orbital.dingletutors;

import android.os.Bundle;

import java.util.Date;
import java.util.TreeMap;

/**
 * Created by Herald on 22/5/2017.
 */

public class DayMap extends TreeMap<Integer, Lesson> {
    public String key; // dd MMM YYYY format
    public MonthMap parent;
    public DayMap(String key, MonthMap parent) {
        this.key = key;
        this.parent = parent;
        parent.put(key, this);
    }

    public boolean delete() {
        return (this.parent.remove(this.key) != null);
    }

    @Override
    public Lesson put(Integer key, Lesson value) {
        while (CalendarMap.updating) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        CalendarMap.updating = true;
        Lesson temp = super.put(key, value);
        CalendarMap.updating = false;
        return temp;
    }

    @Override
    public Lesson get(Object key) {
        while (CalendarMap.updating) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        CalendarMap.updating = true;
        Lesson temp = super.get(key);
        CalendarMap.updating = false;
        return temp;
    }

    @Override
    public Lesson remove(Object key) {
        while (CalendarMap.updating) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        CalendarMap.updating = true;
        Lesson temp = super.remove(key);
        CalendarMap.updating = false;
        return temp;
    }
}
