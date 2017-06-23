package orbital.dingletutors;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Herald on 22/5/2017.
 */

public class MonthMap extends HashMap<String, DayMap> {
    public static final long serialVersionUID = 1002L;
    public String key; // format x-xxxx, not padded with 0s
    public CalendarMap parent;
    public MonthMap(String key, CalendarMap parent) {
        this.key = key;
        this.parent = parent;
        parent.put(key, this);
    }

    public static MonthMap init(String key, @NonNull CalendarMap parent) {
        MonthMap temp = parent.get(key);
        if (temp != null) {
            return temp;
        } else {
            return new MonthMap(key, parent);
        }
    }

    public boolean delete() {return (this.parent.remove(this.key) != null);}

    @Override
    public DayMap put(String key, DayMap value) {
        while (CalendarMap.updating) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        CalendarMap.updating = true;
        DayMap temp = super.put(key, value);
        CalendarMap.updating = false;
        return temp;
    }

    @Override
    public DayMap get(Object key) {
        while (CalendarMap.updating) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        CalendarMap.updating = true;
        DayMap temp = super.get(key);
        CalendarMap.updating = false;
        return temp;
    }

    @Override
    public DayMap remove(Object key) {
        while (CalendarMap.updating) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        CalendarMap.updating = true;
        DayMap temp = super.remove(key);
        CalendarMap.updating = false;
        return temp;
    }
}
