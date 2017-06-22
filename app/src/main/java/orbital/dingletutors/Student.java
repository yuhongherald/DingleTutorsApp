package orbital.dingletutors;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Herald on 31/5/2017.
 */

public class Student {
    public String studentName; // also the key
    public Lesson parent;
    public String clientName;
    public String clientNo;
    public String UID;
    public static final TreeMap<String, Student> studentMap = new TreeMap<>();
    public static final ArrayList<Student> studentList = new ArrayList<>();

    public Student(String studentName, String clientName, String clientNo) {
        this.studentName = studentName;
        this.clientName = clientName;
        this.clientNo = clientNo;
        this.UID = studentName+clientName+clientNo;
        studentMap.put(this.UID, this);
        int addedPos = studentMap.headMap(this.UID).size();
        studentList.add(addedPos, this);
//        this.parent = parent;
//        // trying to make it unique, hope it does not affect sorting too badly
//        if (studentName != null && clientName != null && clientNo != null) {
//            parent.put(studentName + clientName + clientNo, this);
//        }
    }

    public static void editStudent(String UID, String studentName, String clientName, String clientNo){
        Student student = studentMap.get(UID);
        if (student == null) {
            Log.v("Add student", "Student doesn't exist!!");
            return;
        }
        studentMap.remove(UID);
        new Student(studentName, clientName, clientNo);
    }

    public boolean remap(@NonNull String studentName,@NonNull String clientName,@NonNull String clientNo) {
//        if (this.parent.get(studentName+clientName+clientNo) != null) {
//            return false;
//        }
//        delete();
        this.studentName = studentName;
        this.clientName = clientName;
        this.clientNo = clientNo;
//        this.parent.put(studentName+clientName+clientNo, this);
        return true;
    }

//    public boolean delete() {
//        // non-unique keys so I try something different
//        return this.studentName != null && this.clientName != null && this.clientNo != null && this.parent.remove(this) != null;
//    }
}
