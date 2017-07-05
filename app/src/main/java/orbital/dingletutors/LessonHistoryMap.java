package orbital.dingletutors;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by user on 19/6/2017.
 */

public class LessonHistoryMap extends ArrayList<Lesson> {
    private static final long serialVersionUID = 1008L;
    // may have to change between sd and phone memory
//    public static final String data = Environment.getDataDirectory().getPath();
//    public static final String root = "/DingleTutors/";

    public String fileName;

    public LessonHistoryMap(String fileName) {
        //super();
        this.fileName = fileName;
    }

    public static LessonHistoryMap init(String fileName) throws Exception {
        File f = new File(MinuteUpdater.mapDir, fileName);
        if(f.exists() && !f.isDirectory()) {
            Log.v("LessonHistoryMap", "directory found");
            FileInputStream fileInputStream  = new FileInputStream(f);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            LessonHistoryMap map;
            try {
                map = (LessonHistoryMap) objectInputStream.readObject();
            } catch(InvalidClassException e) {
                // this happens when an old map is stored, data will be lost using current imp
                Log.v("LessonHistoryMap", "incompatible map");
                objectInputStream.close();
                return new LessonHistoryMap(fileName);
            }
            objectInputStream.close();
            return map;
        } else {
            Log.v("LessonHistoryMap", "directory not found");
            return new LessonHistoryMap(fileName);
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
