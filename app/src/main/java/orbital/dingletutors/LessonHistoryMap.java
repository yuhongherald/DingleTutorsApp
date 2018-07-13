package orbital.dingletutors;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Herald on 19/6/2017.
 */

public class LessonHistoryMap extends ArrayList<Lesson> {
    private static final long serialVersionUID = 8L;
    // may have to change between sd and phone memory
//    public static final String data = Environment.getDataDirectory().getPath();
//    public static final String root = "/DingleTutors/";
    public static final int historyDays = 90;
    public static final int historyTime = historyDays * 24 * 60;
    public static boolean updating = false;
    public String fileName;

    public LessonHistoryMap(String fileName) {
        //super();
        this.fileName = fileName;
    }

    @Override
    public boolean add(Lesson lesson) {
        while (updating) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        updating = true;
        boolean result = super.add(lesson);
        updating = false;
        return result;
    }

    @Override
    public Lesson get(int index) {
        while (updating) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        updating = true;
        Lesson result = super.get(index);
        updating = false;
        return result;

    }

    @Override
    public boolean remove(Object lesson) {
        while (updating) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        updating = true;
        boolean result = super.remove(lesson);
        updating = false;
        return result;
    }

    public void updateHistory() {
        if (isEmpty()) {
            return;
        }
        Lesson lesson = get(0);
        // this thing is not sorted so it does not work, need to use treemap
        Date earliest = RecurringLesson.addTime(historyTime, Calendar.getInstance().getTime());
        while (lesson.lessonDate.before(earliest)) {
            remove(lesson);
            if (isEmpty()) {
                return;
            }
            lesson = get(0);
        }
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
