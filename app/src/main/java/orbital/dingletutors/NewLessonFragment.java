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
    RelativeLayout newStudent;
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

        final LinearLayout list = (LinearLayout) v.findViewById(R.id.list);
        updateList(list);

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
                String minutes = editMinutes.getText().toString();
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
                // we close this popup
                close();
            }
        });
        ((Button) v.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        ((Button) v.findViewById(R.id.add_student)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent(new Student(null, null, null, CalendarFragment.selectedLesson), 0, list);
            }
        });
        return v;
    }

    @Override
    public void onDestroyView() {
        if (CalendarFragment.thisFragment != null) {
            // recolor here
            Log.v("Calendar", "recoloring");
            CalendarFragment.thisFragment.deleteDay();
        }
        super.onDestroyView();
    }

    public void close() {
        // not sure why the tag check for backstack is not working properly
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        trans.remove(this);
        trans.commit();
        manager.popBackStack();
        // and we updatelist if lesson_list is on top
        LessonListFragment currentFragment = (LessonListFragment) manager.findFragmentByTag("viewLesson");
        if (currentFragment != null) {
            Log.v("ViewLesson", "updating");
            currentFragment.updateList((LinearLayout) currentFragment.getView().findViewById(R.id.list));
        }
    }

    public void updateList(final LinearLayout list) {
        RelativeLayout layout;
        Set<Map.Entry<String, Student>> set = CalendarFragment.selectedLesson.entrySet();
        list.removeAllViewsInLayout();
        int count = 0;
        for (Map.Entry<String, Student> entry : set) {
            count++;
            layout = (RelativeLayout) getActivity().getLayoutInflater()
                    .inflate(R.layout.view_student, null);
            final Student student = entry.getValue();
            TextView editName = (TextView) layout.findViewById(R.id.name);
            TextView editClient = (TextView) layout.findViewById(R.id.client);
            TextView editNumber = (TextView) layout.findViewById(R.id.number);
            if (student.studentName != null) {
                editName.setText(student.studentName);
            }
            if (student.clientName != null) {
                editClient.setText(student.clientName);
            }
            if (student.clientNo != null) {
                editNumber.setText(student.clientNo);
            }
            // Need to set button events later
            final int finalCount = count;
            ((Button) layout.findViewById(R.id.edit)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addStudent(student, finalCount, list);
                }
            });
            ((Button) layout.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    student.delete();
                    updateList(list);
                }
            });
            list.addView(layout);
        }
    }

    public void addStudent(final Student student, int index, final LinearLayout list) {
        if (newStudent != null) {
            list.removeView(newStudent);
        }
        newStudent = (RelativeLayout) getActivity().getLayoutInflater()
                .inflate(R.layout.new_student, null);
        final EditText editName = (EditText) newStudent.findViewById(R.id.name);
        final EditText editClient = (EditText) newStudent.findViewById(R.id.client);
        final EditText editNumber = (EditText) newStudent.findViewById(R.id.number);
        if (student.studentName != null) {
            editName.setText(student.studentName);
        }
        if (student.clientName != null) {
            editClient.setText(student.clientName);
        }
        if (student.clientNo != null) {
            editNumber.setText(student.clientNo);
        }

        ((Button) newStudent.findViewById(R.id.save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String client = editClient.getText().toString();
                String number = editNumber.getText().toString();
                if (name.length() == 0) {
                    Toast.makeText(getActivity(), "Please input student's name.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (number.length() == 0) {
                    Toast.makeText(getActivity(), "Please input a mobile number.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!student.remap(name, client, number)) {
                    Toast.makeText(getActivity(), "Exact student already exists.", Toast.LENGTH_SHORT).show();
                    return;
                }
                list.removeView(newStudent);
                updateList(list);
            }
        });

        ((Button) newStudent.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.removeView(newStudent);
            }
        });

        list.addView(newStudent, index);
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
