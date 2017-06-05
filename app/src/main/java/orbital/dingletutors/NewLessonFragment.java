package orbital.dingletutors;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Muruges on 20/5/2017.
 */

public class NewLessonFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_lesson, container, false);
        if (CalendarFragment.selectedLesson.name != null) {
            ((EditText) v.findViewById(R.id.title)).setText(CalendarFragment.selectedLesson.name);
        }
        if (CalendarFragment.selectedLesson.displayTime != null) {
            ((EditText) v.findViewById(R.id.time)).setText(CalendarFragment.selectedLesson.displayTime);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, Lesson.levels);
        adapter.setDropDownViewResource(R.layout.textview);
        ((Spinner) v.findViewById(R.id.level)).setAdapter(adapter);

        ListView list = (ListView) v.findViewById(R.id.list);
        RelativeLayout layout;
        Student student;
        Set<Map.Entry<String, Student>> set = CalendarFragment.selectedLesson.entrySet();
        list.removeAllViewsInLayout();
        for (Map.Entry<String, Student> entry : set) {
            layout = (RelativeLayout) inflater.inflate(R.layout.view_student, container);
            student = entry.getValue();
            ((TextView) layout.findViewById(R.id.name)).setText(student.studentName);
            ((TextView) layout.findViewById(R.id.client)).setText(student.clientName);
            ((TextView) layout.findViewById(R.id.number)).setText(student.clientNo);
            // Need to set button events later
            ((Button) layout.findViewById(R.id.edit)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            list.addView(layout);
        }

        // Setting buttons


        return v;
    }

    public static NewLessonFragment newInstance(Lesson lesson) {
        NewLessonFragment f = new NewLessonFragment();
        if (lesson == null) {
            CalendarFragment.selectedLesson = new Lesson(0, 0, null, 0, null);
        } else {
            CalendarFragment.selectedLesson = lesson;
        }
        return f;
    }
}
