package orbital.dingletutors;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.TreeMap;

/**
 * Created by Herald on 1/6/2017.
 */

public class LessonPresetMap extends TreeMap<String, Lesson> {

    private static final long serialVersionUID = 1006L;
    // may have to change between sd and phone memory
//    public static final String data = Environment.getDataDirectory().getPath();
//    public static final String root = "/DingleTutors/";

    public String fileName;

    public LessonPresetMap(String fileName) {
        //super();
        this.fileName = fileName;
    }

    public static LessonPresetMap init(String fileName) throws Exception {
        File f = new File(MinuteUpdater.mapDir, fileName);
        if(f.exists() && !f.isDirectory()) {
            Log.v("LessonPresetMap", "directory found");
            FileInputStream fileInputStream  = new FileInputStream(f);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            LessonPresetMap map;
            try {
                map = (LessonPresetMap) objectInputStream.readObject();
            } catch(InvalidClassException e) {
                // this happens when an old map is stored, data will be lost using current imp
                Log.v("LessonPresetMap", "incompatible map");
                objectInputStream.close();
                return new LessonPresetMap(fileName);
            }
            objectInputStream.close();
            return map;
        } else {
            Log.v("LessonPresetMap", "directory not found");
            return new LessonPresetMap(fileName);
        }
    }

    // for deleting the file when treemap is empty
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
