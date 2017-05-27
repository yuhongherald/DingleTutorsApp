package orbital.dingletutors;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Herald on 22/5/2017.
 */

public class MonthMap extends HashMap<String, DayMap> {
    public String key; // format x-xxxx, not padded with 0s
    public MonthMap(String key) {
        this.key = key;
    }

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
