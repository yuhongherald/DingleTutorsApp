package orbital.dingletutors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by Herald on 9/6/2017.
 */

public class PopupCheckIn extends Popup {
    View viewStudents;

    PopupCheckIn(Context context, View anchor, int width, int x, int y) {
        super(context, anchor, width, x, y);
        // we get our stuff and put into popupview
        updateList();
    }

    @Override
    public void updateList() {
        viewStudents = null;
        final LinearLayout list = (LinearLayout) popupView.findViewById(R.id.container).findViewById(R.id.list);
        list.removeAllViewsInLayout();
        ((TextView) popupView.findViewById(R.id.title)).setText("Check in");

        Date rawDate = Calendar.getInstance().getTime();
        String date = new SimpleDateFormat("mmHHddMMYYYY").format(rawDate);
        MonthMap month = MinuteUpdater.calendarMap.get(Integer.parseInt(date.substring(6, 8)) + "-" +
                Integer.parseInt(date.substring(8, 12)));
        int count = 0;
        if (month != null) {
            DayMap day = month.get(CalendarFragment.formatter.format(rawDate));
            if (day != null) {
                // Check all lessons on the day itself
                Set<Map.Entry<Integer, Lesson>> set = day.entrySet();
                RelativeLayout layout;
                for (Map.Entry<Integer, Lesson> entry : set) {
                    final Lesson lesson = entry.getValue();
                    if (lesson.minutesBefore() <= 15  && lesson.minutesBefore() >= -15) {
                        count++;
                        final int index = count;
                        layout = (RelativeLayout)((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                .inflate(R.layout.view_lesson_checkin, null);
                        ((TextView) layout.findViewById(R.id.time)).setText(lesson.displayTime);
                        ((TextView) layout.findViewById(R.id.name)).setText(lesson.name);
                        ((TextView) layout.findViewById(R.id.level)).setText(lesson.level);
                        ((TextView) layout.findViewById(R.id.size)).setText(Integer.toString(lesson.students.size()));
                        layout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (viewStudents != null) {
                                    list.removeView(viewStudents);
                                }
                                viewStudents = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                        .inflate(R.layout.linear_scrollable, null);
//                                Set<Map.Entry<String, Student>> set = lesson.entrySet();
                                LinearLayout newList = (LinearLayout) viewStudents.findViewById(R.id.list);
                                RelativeLayout newLayout;
                                for (Student student : lesson.students) {
                                    newLayout = (RelativeLayout)((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                            .inflate(R.layout.view_student_checkin, null);
//                                    final Student student = entry.getValue();
                                    TextView editName = (TextView) newLayout.findViewById(R.id.name);
                                    TextView editClient = (TextView) newLayout.findViewById(R.id.client);
                                    TextView editNumber = (TextView) newLayout.findViewById(R.id.number);
                                    if (student.studentName != null) {
                                        editName.setText(student.studentName);
                                    }
                                    if (student.clientName != null) {
                                        editClient.setText(student.clientName);
                                    }
                                    if (student.clientNo != null) {
                                        editNumber.setText(student.clientNo);
                                    }
                                    newList.addView(newLayout);
                                }
                                list.addView(viewStudents, index);
                            }
                        });
                        ((Button) layout.findViewById(R.id.checkin)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // add the lesson into history and remove it from calendar
                                MinuteUpdater.lessonHistoryMap.add(lesson);
                                String tempDate = lesson.parent.key;
                                lesson.delete();
                                if (CalendarFragment.thisFragment != null) {
                                    try {
                                        CalendarFragment.thisFragment.recolorDay(
                                                CalendarFragment.formatter.parse(tempDate));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                                // MinuteUpdater.minuteQueue.remove(lesson);
                                hide();
                            }
                        });

                        list.addView(layout);
                    }
                }
            }
        }
    }
}
