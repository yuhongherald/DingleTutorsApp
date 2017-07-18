package orbital.dingletutors;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Herald on 18/7/2017.
 * I'm assuming that Arraylist keeps its order after operations
 */

public class RecurringLesson implements Serializable {
    public static final int nextWeek = 7 * 24 * 60 * 60 * 1000;
    private static final long serialVersionUID = 9L;
    private final RecurringLesson recurringLesson = null;
    private static final int recurringCap = 52;

    public int time;
    public int hours;
    public int minutes;
    public int duration;
    public String name;
    public String level;
    public ArrayList<Student> students;

    public RecurringLessonMap parent;
    public ArrayList<Lesson> lessons;
    public Date startDate; // key (without hours and minutes), does not change
    public Date lastDate;
    public int initialSessions; // this has only been used in construction

    public RecurringLesson(int hours, int minutes, int initialSessions, String startDate) {
        this.time = hours * 60 + minutes;
        this.hours = hours;
        this.minutes = minutes;
        this.lessons = new ArrayList<Lesson>();
        this.initialSessions = initialSessions;
        try {
            this.startDate = addTime(this.time, CalendarFragment.formatter.parse(startDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.lastDate = addTime(-1 * nextWeek, this.startDate);
    }

    public boolean changeSessionNumber(int sessionDiff) {
        if (sessionDiff + this.lessons.size() <= 0) {
            return delete();
        }
        if (sessionDiff + this.lessons.size() > recurringCap) {
            return false;
        }
        if (sessionDiff < 0) {
            for (int i = 0; i > sessionDiff; i--) {
                this.lessons.get(this.lessons.size() - 1).delete();
            }
            // not sure about this
            this.lastDate = this.lessons.get(this.lessons.size() - 1).lessonDate;
        }
        // check for conflict
        Date tempDate = new Date();
        tempDate.setTime(lastDate.getTime());
        for (int i = 0; i < sessionDiff; i++) {
            tempDate = addTime(nextWeek, tempDate);
            Lesson lesson = Lesson.remap(tempDate);
            if (lesson != null) {
                return false;
            }
        }
        // add
        for (int i = 0; i < sessionDiff; i++) {
            lastDate = addTime(nextWeek, lastDate);
            Lesson lesson = Lesson.init(lastDate);
            copyToLesson(lesson);
        }
        return true;
    }

    public boolean delete() {
        for (Lesson lesson : lessons) {
            lesson.delete();
        }
        if (parent == null) {
            return false;
        }
        parent.remove(this.time, this.startDate);
        return true;
    }

    public void copyToLesson(Lesson lesson) {
        lesson.recurringLesson = this;
        // time has been done already
        lesson.duration = this.duration;
        lesson.name = this.name;
        lesson.level = this.level;
        lesson.students = this.students; // not duplicating
    }

    public static Date addTime(int time, Date date) {
        date.setTime(date.getTime() + time * 60 * 1000);
        return date;
    }
}
