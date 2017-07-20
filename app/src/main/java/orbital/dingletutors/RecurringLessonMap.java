package orbital.dingletutors;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Herald on 18/7/2017.
 */

public class RecurringLessonMap extends ArrayList<RecurringLesson> implements Serializable {
    private static final long serialVersionUID = 1010L;
    private final String fileName;

    public RecurringLessonMap(String fileName) {
        //super();
        this.fileName = fileName;
    }

    public static RecurringLessonMap init(String fileName) throws Exception {
        File f = new File(MinuteUpdater.mapDir, fileName);
        if(f.exists() && !f.isDirectory()) {
            Log.v("RecurringLessonMap", "directory found");
            FileInputStream fileInputStream  = new FileInputStream(f);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            RecurringLessonMap map;
            try {
                map = (RecurringLessonMap) objectInputStream.readObject();
            } catch(InvalidClassException e) {
                // this happens when an old map is stored, data will be lost using current imp
                Log.v("RecurringLessonMap", "incompatible map");
                objectInputStream.close();
                return new RecurringLessonMap(fileName);
            }
            objectInputStream.close();
            return map;
        } else {
            Log.v("RecurringLessonMap", "directory not found");
            return new RecurringLessonMap(fileName);
        }
    }

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

    public static int dayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }
}
