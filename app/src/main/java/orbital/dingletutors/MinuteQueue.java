package orbital.dingletutors;

import android.content.Context;
import android.content.Intent;
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

/**
 * Created by Herald on 9/6/2017.
 * Now changed to only hold a timestamp.
 */

public class MinuteQueue implements Serializable {

    private static final long serialVersionUID = 102L;

    public String fileName;
    public Date lastUpdated;

    public MinuteQueue(String fileName) {
        this.fileName = fileName;
        this.lastUpdated = Calendar.getInstance().getTime();
    }

    public static MinuteQueue init(String fileName) throws Exception {
        File f = new File(MinuteUpdater.mapDir, fileName);
        if(f.exists() && !f.isDirectory()) {
            Log.v("MinuteQueue", "directory found");
            FileInputStream fileInputStream  = new FileInputStream(f);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            MinuteQueue map;
            try {
                map = (MinuteQueue) objectInputStream.readObject();
            } catch(InvalidClassException e) {
                // this happens when an old map is stored, data will be lost using current imp
                Log.v("MinuteQueue", "incompatible map");
                objectInputStream.close();
                return new MinuteQueue(fileName);
            }
            objectInputStream.close();
            return map;
        } else {
            Log.v("MinuteQueue", "directory not found");
            return new MinuteQueue(fileName);
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
