package orbital.dingletutors;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Set;

/**
 * Created by Muruges on 20/5/2017.
 */

public class NewLessonFragment extends Fragment {
    RelativeLayout newStudent;
    SimpleDateFormat timeformat = new SimpleDateFormat("h:mm a");
    Date currentDate;
    int currentHour;
    int currentMinute;

    TextView startTime;
    TextView classDate;
    TextView className;
    TextView duration;
    TextView studentName;
    TextView educationLevel;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Fragment fragment = this;
        View v = inflater.inflate(R.layout.new_lesson, container, false);

        currentDate = CalendarFragment.selectedDay.date;
        Calendar cal = Calendar.getInstance();

        className = (TextView) v.findViewById(R.id.classNameVal);
        classDate = (TextView) v.findViewById(R.id.dateVal);
        duration = (TextView) v.findViewById(R.id.durationVal);
        startTime = (TextView) v.findViewById(R.id.startTimeVal);
        studentName = (TextView) v.findViewById(R.id.studentNameVal);
        educationLevel = (TextView) v.findViewById(R.id.levelVal);



//        final EditText editHours = (EditText) v.findViewById(R.id.hours);
//        final EditText editMinutes = (EditText) v.findViewById(R.id.minutes);
//        final Spinner spinnerLevel = (Spinner) v.findViewById(R.id.level);

        classDate.setText(CalendarFragment.selectedDay.key);
        startTime.setText(timeformat.format(new Date()));
        currentHour = cal.get(Calendar.HOUR_OF_DAY);
        currentMinute = cal.get(Calendar.MINUTE);

//        if (CalendarFragment.selectedLesson.name != null) {
//            className.setText(CalendarFragment.selectedLesson.name);
//        }
//        if (CalendarFragment.selectedLesson.hours != null) {
//            editHours.setText(CalendarFragment.selectedLesson.hours);
//        }
//        if (CalendarFragment.selectedLesson.minutes != null) {
//            editMinutes.setText(CalendarFragment.selectedLesson.minutes);
//        }
        // have to redo this
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, Lesson.levels);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerLevel.setAdapter(adapter);

//        final LinearLayout list = (LinearLayout) v.findViewById(R.id.list);
//        updateList(list);

        // Setting buttons


        v.findViewById(R.id.date).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showDatePicker();
            }
        });

        v.findViewById(R.id.startTime).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                showTimePicker();
            }
        });
        v.findViewById(R.id.duration).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceDialog("durations");
            }
        });
        v.findViewById(R.id.educationLevel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceDialog("educationLevels");
            }
        });
        v.findViewById(R.id.classname).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SingleChoiceDialog("subjectNames");
            }
        });

        ((Button) v.findViewById(R.id.saveLesson)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check for invalid input
//                String name = editTitle.getText().toString();
//                if (name.length()==0) {
//                    Toast.makeText(getActivity(), "Please input a class name.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                String hours = editHours.getText().toString();
//                String minutes = editMinutes.getText().toString();
//                if (hours.length() == 0 || minutes.length() == 0) {
//                    Toast.makeText(getActivity(), "Please input a time.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                int intHours = Integer.parseInt(hours);
//                int intMinutes = Integer.parseInt(minutes);
//                if (intHours > 23 || intMinutes > 59) {
//                    Toast.makeText(getActivity(), "Please input a valid time.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                // we save
//                String result = CalendarFragment.selectedLesson.remap(
//                        String.format("%02d", intHours),
//                        String.format("%02d", intMinutes),
//                        name,
//                        spinnerLevel.getSelectedItemPosition());
//                if(result != null) {
//                    Toast.makeText(getActivity(), "Lesson in conflict with: " + result, Toast.LENGTH_SHORT).show();
//                    return;
//                }
                // we close this popup
                getActivity().onBackPressed();
//                close();
            }
        });
        ((Button) v.findViewById(R.id.cancelLesson)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
//                close();

            }
        });
//        ((Button) v.findViewById(R.id.add_student)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                addStudent(new Student(null, null, null, CalendarFragment.selectedLesson), 0, list);
//            }
//        });
        return v;
    }
    private void showDatePicker(){
        DateDialogFragment datefrag = new DateDialogFragment();
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        Bundle args = new Bundle();
        args.putInt("year", cal.get(Calendar.YEAR));
        args.putInt("month", cal.get(Calendar.MONTH));
        args.putInt("day", cal.get(Calendar.DAY_OF_MONTH));
        datefrag.setArguments(args);

        DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                currentDate = new GregorianCalendar(year, month, dayOfMonth).getTime();
                classDate.setText(CalendarFragment.formatter.format(currentDate));
            }
        };
        datefrag.setCallBack(ondate);
        datefrag.show(getFragmentManager(), "Date Picker");
    }
    private void showTimePicker(){
        TimeDialogFragment timefrag = new TimeDialogFragment();
        Bundle args = new Bundle();
        args.putInt("hours", currentHour);
        args.putInt("minutes", currentMinute);
        timefrag.setArguments(args);

        TimePickerDialog.OnTimeSetListener ontime = new TimePickerDialog.OnTimeSetListener(){

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                currentMinute = minute;
                currentHour = hourOfDay;
                Calendar cal = Calendar.getInstance();
                cal.set(0,0,0,hourOfDay, minute);
                startTime.setText(timeformat.format(cal.getTime()));
            }
        };
        timefrag.setCallBack(ontime);
        timefrag.show(getFragmentManager(), "Time Picker");
    }
    private String[] subjectNames = { "English", "Mathematics", "Science", "Mother Tongue"};
    private String[] educationLevels = { "Primary 1", "Primary 2", "Primary 3", "Primary 4",
            "Primary 5", "Primary 6", "Secondary 1", "Secondary 2", "Secondary 3", "Secondary 4"};
    private String[] durations = { "1 Hour", "1.5 Hours", "2 Hours", "2.5 Hours", "3 Hours"};

    private void SingleChoiceDialog(String field){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Single Choice");
        switch (field) {
            case "subjectNames":
                builder.setItems(subjectNames, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        className.setText(subjectNames[which]);
                        dialog.dismiss();
                    }
                });
                break;
            case "educationLevels":
                builder.setItems(educationLevels, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        educationLevel.setText(educationLevels[which]);
                        dialog.dismiss();
                    }
                });
                break;
            case "durations":
                builder.setItems(durations, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        duration.setText(durations[which]);
                        dialog.dismiss();
                    }
                });
                break;
            default:
                Log.v("TAG","Incorrect input for single choice dialog");
        }

        builder.setNegativeButton("cancel",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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

//    public void close() {
//        // not sure why the tag check for backstack is not working properly
//        FragmentManager manager = getActivity().getSupportFragmentManager();
//        FragmentTransaction trans = manager.beginTransaction();
//        trans.remove(this);
//        trans.commit();
//        manager.popBackStack();
//        // and we updatelist if lesson_list is on top
//        LessonListFragment currentFragment = (LessonListFragment) manager.findFragmentByTag("viewLesson");
//        if (currentFragment != null) {
//            Log.v("ViewLesson", "updating");
//            currentFragment.updateList((LinearLayout) currentFragment.getView().findViewById(R.id.list));
//        }
//    }

//    public void updateList(final LinearLayout list) {
//        RelativeLayout layout;
//        Set<Map.Entry<String, Student>> set = CalendarFragment.selectedLesson.entrySet();
//        list.removeAllViewsInLayout();
//        int count = 0;
//        for (Map.Entry<String, Student> entry : set) {
//            count++;
//            layout = (RelativeLayout) getActivity().getLayoutInflater()
//                    .inflate(R.layout.view_student, null);
//            final Student student = entry.getValue();
//            TextView editName = (TextView) layout.findViewById(R.id.name);
//            TextView editClient = (TextView) layout.findViewById(R.id.client);
//            TextView editNumber = (TextView) layout.findViewById(R.id.number);
//            if (student.studentName != null) {
//                editName.setText(student.studentName);
//            }
//            if (student.clientName != null) {
//                editClient.setText(student.clientName);
//            }
//            if (student.clientNo != null) {
//                editNumber.setText(student.clientNo);
//            }
//            // Need to set button events later
//            final int finalCount = count;
//            ((Button) layout.findViewById(R.id.edit)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    addStudent(student, finalCount, list);
//                }
//            });
//            ((Button) layout.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    student.delete();
//                    updateList(list);
//                }
//            });
//            list.addView(layout);
//        }
//    }

//    public void addStudent(final Student student, int index, final LinearLayout list) {
//        if (newStudent != null) {
//            list.removeView(newStudent);
//        }
//        newStudent = (RelativeLayout) getActivity().getLayoutInflater()
//                .inflate(R.layout.new_student, null);
//        final EditText editName = (EditText) newStudent.findViewById(R.id.name);
//        final EditText editClient = (EditText) newStudent.findViewById(R.id.client);
//        final EditText editNumber = (EditText) newStudent.findViewById(R.id.number);
//        if (student.studentName != null) {
//            editName.setText(student.studentName);
//        }
//        if (student.clientName != null) {
//            editClient.setText(student.clientName);
//        }
//        if (student.clientNo != null) {
//            editNumber.setText(student.clientNo);
//        }
//
//        ((Button) newStudent.findViewById(R.id.save)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String name = editName.getText().toString();
//                String client = editClient.getText().toString();
//                String number = editNumber.getText().toString();
//                if (name.length() == 0) {
//                    Toast.makeText(getActivity(), "Please input student's name.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (number.length() == 0) {
//                    Toast.makeText(getActivity(), "Please input a mobile number.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                if (!student.remap(name, client, number)) {
//                    Toast.makeText(getActivity(), "Exact student already exists.", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                list.removeView(newStudent);
//                updateList(list);
//            }
//        });
//
//        ((Button) newStudent.findViewById(R.id.cancel)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                list.removeView(newStudent);
//            }
//        });
//
//        list.addView(newStudent, index);
//    }

    public static NewLessonFragment newInstance(Lesson lesson) {
        NewLessonFragment f = new NewLessonFragment();
//        if (lesson == null) {
//            CalendarFragment.selectedLesson = new Lesson(null, null, null, 0, CalendarFragment.selectedDay);
//        } else {
//            CalendarFragment.selectedLesson = lesson;
//        }
        return f;
    }
}
