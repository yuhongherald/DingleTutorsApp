package orbital.dingletutors;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

/**
 * Created by Herald on 31/5/2017.
 */

public class Student implements Serializable, Comparable<Student> {
    public String studentName; // also the key
    public String clientName;
    public String clientNo;
//    public String UID;
    public static final long serialVersionUID = 1005L;
    public static final TreeMap<Student, Student> studentMap = new TreeMap<>();
    public static final ArrayList<Student> studentList = new ArrayList<>();

    public Student(String studentName, String clientName, String clientNo) {
        this.studentName = studentName;
        this.clientName = clientName;
        this.clientNo = clientNo;
        // this.UID = studentName+clientName+clientNo;
        studentMap.put(this, this);
        int addedPos = studentMap.headMap(this).size();
        studentList.add(addedPos, this);
//        this.parent = parent;
//        // trying to make it unique, hope it does not affect sorting too badly
//        if (studentName != null && clientName != null && clientNo != null) {
//            parent.put(studentName + clientName + clientNo, this);
//        }
    }

    public static void editStudent(Student student, String studentName, String clientName, String clientNo){
        if (!studentMap.containsKey(student)) {
            Log.v("Add student", "Student doesn't exist!!");
            return;
        }
        studentMap.remove(student);
        new Student(studentName, clientName, clientNo);
    }

    public boolean remap(@NonNull String studentName,@NonNull String clientName,@NonNull String clientNo) {
        Student newStudent = new Student(studentName, clientName, clientNo);
        Lesson parent = null;
        if (parent.students.contains(newStudent)) {
            return false;
        }
        // delete();
        parent.students.add(newStudent);
        Collections.sort(parent.students);
        return true;
    }

    @Override
    public int compareTo(@NonNull Student o) {
        int result = studentName.compareTo(o.studentName);
        if (result == 0) {
            result = clientName.compareTo(o.clientName);
            if (result == 0) {
                return clientNo.compareTo(o.clientNo);
            } else {
                return result;
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Student)) {
            return false;
        }
        Student temp = (Student) other;
        return studentName.equals(temp.studentName) &&
                clientName.equals(temp.clientName) &&
                clientNo.equals(temp.clientNo);
    }

//    public boolean delete() {
//        // non-unique keys so I try something different
//        return this.studentName != null && this.clientName != null && this.clientNo != null && this.parent.remove(this) != null;
//    }
}
