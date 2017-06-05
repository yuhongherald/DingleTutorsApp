package orbital.dingletutors;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.Set;

/**
 * Created by Muruges on 20/5/2017.
 */

public class NewLessonFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Fragment fragment = this;
        View v = inflater.inflate(R.layout.new_lesson, container, false);

        final EditText editTitle = (EditText) v.findViewById(R.id.title);
        final EditText editHours = (EditText) v.findViewById(R.id.hours);
        final EditText editMinutes = (EditText) v.findViewById(R.id.minutes);
        final Spinner spinnerLevel = (Spinner) v.findViewById(R.id.level);

        if (CalendarFragment.selectedLesson.name != null) {
            editTitle.setText(CalendarFragment.selectedLesson.name);
        }
        if (CalendarFragment.selectedLesson.hours != null) {
            editHours.setText(CalendarFragment.selectedLesson.hours);
        }
        if (CalendarFragment.selectedLesson.minutes != null) {
            editMinutes.setText(CalendarFragment.selectedLesson.minutes);
        }
        // have to redo this
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Lesson.levels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLevel.setAdapter(adapter);

        LinearLayout list = (LinearLayout) v.findViewById(R.id.list);
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

        ((Button) v.findViewById(R.id.save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check for invalid input
                String name = editTitle.getText().toString();
                if (name.length()==0) {
                    Toast.makeText(getActivity(), "Please input a class name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String hours = editHours.getText().toString();
                String minutes = editMinutes.getEditableText().toString();
                if (hours.length() == 0 || minutes.length() == 0) {
                    Toast.makeText(getActivity(), "Please input a time.", Toast.LENGTH_SHORT).show();
                    return;
                }
                int intHours = Integer.parseInt(hours);
                int intMinutes = Integer.parseInt(minutes);
                if (intHours > 23 || intMinutes > 59) {
                    Toast.makeText(getActivity(), "Please input a valid time.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // we save
                String result = CalendarFragment.selectedLesson.remap(
                        String.format("%02d", intHours),
                        String.format("%02d", intMinutes),
                        name,
                        spinnerLevel.getSelectedItemPosition());
                if(result != null) {
                    Toast.makeText(getActivity(), "Lesson in conflict with: " + result, Toast.LENGTH_SHORT).show();
                    return;
                }

                //
                // we close this popup
                FragmentManager manager = getActivity().getSupportFragmentManager();
                if (manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName() != "newLesson") {
                    Log.v("NewLesson", "not n top of stack");
                }
                FragmentTransaction trans = manager.beginTransaction();
                trans.remove(fragment);
                trans.commit();
                manager.popBackStack();
                // and we updatelist if lesson_list is on top
                if (manager.getBackStackEntryAt(manager.getBackStackEntryCount() - 1).getName() != "viewLesson") {
                    LessonListFragment currentFragment = (LessonListFragment) manager.findFragmentByTag("viewLesson");
                    currentFragment.updateList((LinearLayout) currentFragment.getView().findViewById(R.id.list));
                }
            }
        });

        return v;
    }

    public static NewLessonFragment newInstance(Lesson lesson) {
        NewLessonFragment f = new NewLessonFragment();
        if (lesson == null) {
            CalendarFragment.selectedLesson = new Lesson(null, null, null, 0, CalendarFragment.selectedDay);
        } else {
            CalendarFragment.selectedLesson = lesson;
        }
        return f;
    }
}
