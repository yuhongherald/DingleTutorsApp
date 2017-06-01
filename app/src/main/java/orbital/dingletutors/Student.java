package orbital.dingletutors;

import android.support.annotation.NonNull;

/**
 * Created by Herald on 31/5/2017.
 */

public class Student {
    public final String studentName; // also the key
    public Lesson parent;
    public final String clientName;
    public final String clientNo;

    public Student(String studentName, String clientName, String clientNo, Lesson parent) {
        this.studentName = studentName;
        this.clientName = clientName;
        this.clientNo = clientNo;
        this.parent = parent;
        // trying to make it unique, hope it does not affect sorting too badly
        parent.put(studentName + clientName + clientNo, this);
    }

    public boolean delete() {
        // non-unique keys so I try something different
        return this.parent.remove(this) != null;
    }
}
