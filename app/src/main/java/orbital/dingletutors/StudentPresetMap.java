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
import java.util.TreeMap;

/**
 * Created by Herald on 1/6/2017.
 * Place arraylist because need to support multiple sorting
 * If not can duplicate tree to allow sort by name, client name or client no
 */

public class StudentPresetMap implements Serializable {

    private static final long serialVersionUID = 7L;
    public String fileName;

    public TreeMap<Student, Integer> studentMap;// int not used
    public ArrayList<Student> studentList;

    public StudentPresetMap(String fileName) {
        //super();
        this.fileName = fileName;
        this.studentMap = new TreeMap<>();
        this.studentList = new ArrayList<>();
    }

    public static StudentPresetMap init(String fileName) throws Exception {
        File f = new File(MinuteUpdater.mapDir, fileName);
        if(f.exists() && !f.isDirectory()) {
            Log.v("StudentPresetMap", "directory found");
            FileInputStream fileInputStream  = new FileInputStream(f);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            StudentPresetMap map;
            try {
                map = (StudentPresetMap) objectInputStream.readObject();
            } catch(InvalidClassException e) {
                // this happens when an old map is stored, data will be lost using current imp
                Log.v("StudentPresetMap", "incompatible map");
                objectInputStream.close();
                return new StudentPresetMap(fileName);
            }
            objectInputStream.close();
            return map;
        } else {
            Log.v("StudentPresetMap", "directory not found");
            return new StudentPresetMap(fileName);
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
