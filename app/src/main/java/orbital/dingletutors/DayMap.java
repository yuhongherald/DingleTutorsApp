package orbital.dingletutors;

import android.os.Bundle;

import java.util.Date;
import java.util.TreeMap;

/**
 * Created by Herald on 22/5/2017.
 */

public class DayMap extends TreeMap<Integer, Bundle> {
    public String key; // dd MMM YYYY format
    public DayMap(String key) {
        this.key = key;
    }

    @Override
    public Bundle put(Integer key, Bundle value) {
        while (CalendarMap.updating) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        CalendarMap.updating = true;
        Bundle temp = super.put(key, value);
        CalendarMap.updating = false;
        return temp;
    }

    @Override
    public Bundle get(Object key) {
        while (CalendarMap.updating) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        CalendarMap.updating = true;
        Bundle temp = super.get(key);
        CalendarMap.updating = false;
        return temp;
    }

    @Override
    public Bundle remove(Object key) {
        while (CalendarMap.updating) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        CalendarMap.updating = true;
        Bundle temp = super.remove(key);
        CalendarMap.updating = false;
        return temp;
    }
}
