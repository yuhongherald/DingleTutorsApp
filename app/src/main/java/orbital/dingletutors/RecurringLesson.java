package orbital.dingletutors;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Herald on 18/7/2017.
 * I'm assuming that Arraylist keeps its order after operations
 */

public class RecurringLesson implements Serializable {
    public static final boolean isRecurring = false;

    public static final int nextWeek = 7 * 24 * 60 * 60 * 1000;
    private static final long serialVersionUID = 1011L;
    private static final int recurringCap = 12;

    public int time;
    public int hours;
    public int minutes;

    public Date startDate; // key (day of input + time), does not change
    public int weeks;

    public int duration;
    public String name;
    public String level;
    public ArrayList<Student> students;

    public boolean[] daysOfWeek;
    public int sessionsAWeek;
    public RecurringLessonMap parent;
    public ArrayList<Lesson> lessons;

    public RecurringLesson(int hours, int minutes, int weeks, boolean[] daysOfWeek) {
        Log.v("Recurring lesson", weeks + " weeks");
        this.time = hours * 60 + minutes;
        this.hours = hours;
        this.minutes = minutes;
        this.lessons = new ArrayList<Lesson>();
        this.weeks = weeks;
        this.daysOfWeek = daysOfWeek;
        this.sessionsAWeek = 0;
        for (int i = 0; i < 7; i++) {
            if (daysOfWeek[i]) {
                this.sessionsAWeek++;
            }
        }
    }

    // if valid, puts into recurring map and calendar
    public Lesson validate(Context context, Date startDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        Date currentDate = calendar.getTime();
        int startIndex = (calendar.get(Calendar.DAY_OF_WEEK) + 5) % 7;
        Log.v("startIndex", Integer.toString(startIndex));
        if (startDate.after(currentDate)) {
            startIndex = (startIndex + 1) % 7;
            currentDate.setTime(currentDate.getTime() + MinuteUpdater.nextDay);
        }

        ArrayList<Date> dates = new ArrayList<>();
        Lesson result = null;

        for (int i = 0; i < weeks * 7; i++) {
            if (daysOfWeek[startIndex]) {
                result = Lesson.remap(currentDate);
                if (result != null) {
                    return result;
                }
                dates.add(addTime(0, currentDate));
            }
            startIndex = (startIndex + 1) % 7;
            currentDate.setTime(currentDate.getTime() + MinuteUpdater.nextDay);
        }
        // SUCCESS!
        if (isRecurring) {
            if (MinuteUpdater.recurringLessonMap == null) {
                MinuteUpdater.loadMap(context);
            }
            this.parent = MinuteUpdater.recurringLessonMap;
            MinuteUpdater.recurringLessonMap.add(this);
        }
        Lesson temp;
        for (Date date : dates) {
            Log.v("Recurring lesson", CalendarFragment.formatter.format(date));
            temp = Lesson.init(date);
            copyToLesson(temp);
            lessons.add(temp);

        }
        return null;
    }

    public void remove(Lesson lesson) {
        lessons.remove(lesson);
        while (lessons.size() < sessionsAWeek * weeks) {
            weeks--;
        }
        if (lessons.isEmpty()) {
            delete();
        }
    }

    public boolean delete() {
        ArrayList<Lesson> temp = (ArrayList<Lesson>) lessons.clone();
        for (Lesson lesson : temp) {
            lesson.delete();
        }
        if (parent == null) {
            return false;
        }
        return parent.remove(this);
    }

    public void copyToLesson(Lesson lesson) {
        if (isRecurring) {
            lesson.recurringLesson = this;
        }
        // time has been done already
        lesson.duration = this.duration;
        lesson.name = this.name;
        lesson.level = this.level;
        lesson.students = this.students; // not duplicating
    }

    // in case you want to use this
    public boolean removeRecurring() {
        ArrayList<Lesson> lessonsCopy = new ArrayList<>(lessons);
        for (Lesson lesson : lessonsCopy) {
            lesson.recurringLesson = null;
        }
        return parent.remove(this);
    }

    // returns the ceiling
    public static int weeksBefore(Date first, Date second) {
        long msDiff = second.getTime() - first.getTime();
        int temp = (int) (msDiff / nextWeek);
        if (msDiff - nextWeek * temp > 0) {
            return temp + 1;
        } else {
            return temp;
        }
    }

    public static Date addTime(int time, Date date) {
        Date newDate = new Date();
        newDate.setTime(date.getTime() + time * 60 * 1000);
        return newDate;
    }
}
