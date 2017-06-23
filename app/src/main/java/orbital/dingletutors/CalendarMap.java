package orbital.dingletutors;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Herald on 19/5/2017.
 */

public class CalendarMap extends HashMap<String, MonthMap> {
    // Key is month and year, value is a Hashmap for the month
    // For month Hashmaps, key is day, value is TreeMap
    // For day Treemap, key is start time, value is data associated

    // used to hold the CalendarMap if needed
    public static final long serialVersionUID = 1001L;
    public static File mapDir;
    public static boolean isInitializing = false;
    public static boolean updating = false;
    // may have to change between sd and phone memory
//    public static final String data = Environment.getDataDirectory().getPath();
//    public static final String root = "/DingleTutors/";

    public String fileName;

    public CalendarMap(String fileName) {
        //super();
        this.fileName = fileName;
    }

    @Override
    public MonthMap put(String key, MonthMap value) {
        while (updating) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        updating = true;
        MonthMap temp = super.put(key, value);
        updating = false;
        return temp;
    }

    @Override
    public MonthMap get(Object key) {
        while (updating) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        updating = true;
        MonthMap temp = super.get(key);
        updating = false;
        return temp;
    }

    @Override
    public MonthMap remove(Object key) {
        while (updating) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        updating = true;
        MonthMap temp = super.remove(key);
        updating = false;
        return temp;
    }

    public static CalendarMap init(String fileName) throws Exception {
        File f = new File(mapDir, fileName);
        if(f.exists() && !f.isDirectory()) {
            Log.v("CalendarMap", "directory found");
            FileInputStream fileInputStream  = new FileInputStream(f);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            CalendarMap map;
            try {
                map = (CalendarMap) objectInputStream.readObject();
            } catch(InvalidClassException e) {
                // this happens when an old map is stored, data will be lost using current imp
                Log.v("CalendarMap", "incompatible map");
                objectInputStream.close();
                return new CalendarMap(fileName);
            }
            objectInputStream.close();
            return map;
        } else {
            Log.v("CalendarMap", "directory not found");
            return new CalendarMap(fileName);
        }
    }

    // for deleting the file when hashmap is empty
    public void delete() {

    }

    // either do autosave or save on app close
    public void save() throws Exception {
        // create a File object for the output file
        File outputFile = new File(mapDir, this.fileName);
        // outputFile.mkdirs();
        outputFile.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(this);
        objectOutputStream.close();
    }
}
