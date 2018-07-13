package orbital.dingletutors;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

/**
 * Created by Herald on 19/5/2017.
 */

public class CalendarMap extends HashMap<String, MonthMap> {

    public static final long serialVersionUID = 1001L;
    public static boolean updating = false;

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
        File f = new File(MinuteUpdater.mapDir, fileName);
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
        File outputFile = new File(MinuteUpdater.mapDir, this.fileName);
        // outputFile.mkdirs();
        outputFile.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

        objectOutputStream.writeObject(this);
        objectOutputStream.close();
    }
}
