package orbital.dingletutors;

import android.support.annotation.NonNull;

/**
 * Created by Herald on 31/5/2017.
 */

public class Student {
    public String studentName; // also the key
    public Lesson parent;
    public String clientName;
    public String clientNo;

    public Student(String studentName, String clientName, String clientNo) {
        this.studentName = studentName;
        this.clientName = clientName;
        this.clientNo = clientNo;
//        this.parent = parent;
//        // trying to make it unique, hope it does not affect sorting too badly
//        if (studentName != null && clientName != null && clientNo != null) {
//            parent.put(studentName + clientName + clientNo, this);
//        }
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
