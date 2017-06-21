package orbital.dingletutors;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Map;
import java.util.Set;

/**
 * Created by Herald on 19/6/2017.
 *
 * There is 4 types of notifications (potentially)
 * Pending lessons
 * Pending lesson requests
 * Pending reports
 * Pending invoices
 *
 * Will be working on pending lessons first.
 * Notifications can be dismissed, deleted or both. Dismiss or delete all.
 */

public class PopupNotification extends Popup {
    View extraPanel;
    View viewStudents;
    PopupNotification(Context context, View anchor, int width, int x, int y) {
        super(context, anchor, width, x, y);
        updateList();
    }

    @Override
    public void updateList() {
        extraPanel = null;
        viewStudents = null;
        final LinearLayout list = (LinearLayout) popupView.findViewById(R.id.container).findViewById(R.id.list);
        list.removeAllViewsInLayout();
        ((TextView) popupView.findViewById(R.id.title)).setText("Notifications");
        RelativeLayout lessons = (RelativeLayout)((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.notification_header, null);
        ((TextView) lessons.findViewById(R.id.name)).setText("Pending lessons");
        ((TextView) lessons.findViewById(R.id.number)).setText(Integer.toString(MinuteUpdater.minuteQueue.size()));
        lessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("test", "test");
                if (extraPanel != null) {
                    list.removeView(extraPanel);
                }
                extraPanel = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.linear_scrollable, null);
                final LinearLayout newList = (LinearLayout) extraPanel.findViewById(R.id.list);
                RelativeLayout layout;
                int count = 0;
                for (Lesson lesson : MinuteUpdater.minuteQueue) {
                    count++;
                    final Lesson finalLesson = lesson;
                    final int index = count;
                    layout = (RelativeLayout)((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                            .inflate(R.layout.view_lesson_checkin, null);
                    ((TextView) layout.findViewById(R.id.time)).setText(lesson.hours + ":" + lesson.minutes);
                    ((TextView) layout.findViewById(R.id.name)).setText(lesson.name);
                    ((TextView) layout.findViewById(R.id.level)).setText(lesson.level);
                    ((TextView) layout.findViewById(R.id.size)).setText(lesson.students.length);
                    layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (viewStudents != null) {
                                newList.removeView(viewStudents);
                            }
                            viewStudents = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                    .inflate(R.layout.linear_scrollable, null);
//                            Set<Map.Entry<String, Student>> set = finalLesson.entrySet();
                            LinearLayout nestedList = (LinearLayout) viewStudents.findViewById(R.id.list);
                            RelativeLayout newLayout;
                            for (Student student : finalLesson.students) {
                                newLayout = (RelativeLayout)((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                        .inflate(R.layout.view_student_checkin, null);
//                                final Student student = entry.getValue();
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
                                nestedList.addView(newLayout);
                            }
                            newList.addView(viewStudents, index);
                        }
                    });
                    newList.addView(layout);
                }

                list.addView(extraPanel, 1);
            }
        });
        ((Button) lessons.findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MinuteUpdater.minuteQueue.clear(context);
            }
        });
        list.addView(lessons);
    }
}
