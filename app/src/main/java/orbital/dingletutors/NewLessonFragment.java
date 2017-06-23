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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Muruges on 20/5/2017.
 */

public class NewLessonFragment extends Fragment {
//    RelativeLayout newStudent;
    Lesson lesson;
    Date oldDate;

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

    ArrayList<Student> selectedStudents;
    NewLessonFragment currentInstance;
    public NewLessonFragment(){
        super();
        selectedStudents = new ArrayList<Student>();
    }

    public static HashMap<String, Integer> durationStringToInt;
    static {
        durationStringToInt = new HashMap<>(5, 1);
        durationStringToInt.put("1 Hour", 60);
        durationStringToInt.put("1.5 Hours", 90);
        durationStringToInt.put("2 Hours", 120);
        durationStringToInt.put("2.5 Hours", 150);
        durationStringToInt.put("3 Hours", 180);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Fragment fragment = this;
        View v = inflater.inflate(R.layout.new_lesson, container, false);
        currentInstance = this; // for access when choosing students
        currentDate = CalendarFragment.currentDate;
        if (lesson != null) {
            try {
                oldDate = CalendarFragment.formatter.parse(lesson.parent.key);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        final Calendar cal = Calendar.getInstance();

        className = (TextView) v.findViewById(R.id.classNameVal);
        classDate = (TextView) v.findViewById(R.id.dateVal);
        duration = (TextView) v.findViewById(R.id.durationVal);
        startTime = (TextView) v.findViewById(R.id.startTimeVal);
        studentName = (TextView) v.findViewById(R.id.studentNameVal);
        educationLevel = (TextView) v.findViewById(R.id.levelVal);


//        final EditText editHours = (EditText) v.findViewById(R.id.hours);
//        final EditText editMinutes = (EditText) v.findViewById(R.id.minutes);
//        final Spinner spinnerLevel = (Spinner) v.findViewById(R.id.level);

        classDate.setText(CalendarFragment.formatter.format(currentDate));
        startTime.setText(timeformat.format(new Date()));
        currentHour = cal.get(Calendar.HOUR_OF_DAY);
        currentMinute = cal.get(Calendar.MINUTE);
        updateStudents();

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
        v.findViewById(R.id.studentName).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Choose_Student_Fragment chooseStudent = Choose_Student_Fragment.newInstance(currentInstance);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_up, android.R.anim.fade_out,
                        android.R.anim.fade_in, R.anim.slide_out_down);
                transaction.replace(R.id.new_lesson_container, chooseStudent) // carry out the transaction
                        .addToBackStack("newLesson") // add to backstack
                        .commit();
            }
        });

        ((Button) v.findViewById(R.id.saveLesson)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check for invalid input
//                String name = className.getText().toString();
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
//                 we save
                // for testing purposes use fake students
                if (selectedStudents.isEmpty()){
                    Toast.makeText(getActivity(), "No students selected yet", Toast.LENGTH_SHORT).show();
                    return;
                }
//                String result = lesson.remap(
//                        currentHour,
//                        currentMinute,
//                        durationStringToInt.get(duration.getText().toString()),
//                        className.getText().toString(),
//                        educationLevel.getText().toString(),
//                        selectedStudents
//                );
                cal.setTime(currentDate);
                cal.set(Calendar.HOUR, currentHour);
                cal.set(Calendar.MINUTE, currentMinute);
                currentDate = cal.getTime();
                Lesson newLesson = Lesson.remap(currentDate);

                if(newLesson != null && newLesson != lesson) {
                    Toast.makeText(getActivity(), "Lesson in conflict with: " + newLesson.name, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (lesson != null) {
                        lesson.delete();
                    }
                    newLesson = Lesson.init(currentDate);
                    newLesson.duration = durationStringToInt.get(duration.getText().toString());
                    newLesson.name = className.getText().toString();
                    newLesson.level = educationLevel.getText().toString();
                    newLesson.students = selectedStudents;
                }

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
    public static final String[] subjectNames = { "English", "Mathematics", "Science", "Mother Tongue"};
    public static final String[] educationLevels = { "Primary 1", "Primary 2", "Primary 3", "Primary 4",
            "Primary 5", "Primary 6", "Secondary 1", "Secondary 2", "Secondary 3", "Secondary 4"};
    public static final String[] durations = { "1 Hour", "1.5 Hours", "2 Hours", "2.5 Hours", "3 Hours"};

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

    public void updateStudents(){
        if (!selectedStudents.isEmpty()) {
            this.studentName.setText(selectedStudents.get(0).studentName);
        }
    }

    @Override
    public void onDestroyView() {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        LessonListFragment currentFragment = (LessonListFragment) manager.findFragmentByTag("viewLesson");
        if (currentFragment != null) {
            Log.v("ViewLesson", "updating");
            currentFragment.updateList((LinearLayout) currentFragment.getView().findViewById(R.id.list));
        }
        if (CalendarFragment.thisFragment != null) {
            // recolor here
            Log.v("Calendar", "recoloring");
//            CalendarFragment.thisFragment.deleteDay();
            if (oldDate != null) {
                CalendarFragment.thisFragment.recolorDay(oldDate);
            }
            CalendarFragment.thisFragment.recolorDay(currentDate);
        }
        super.onDestroyView();
    }

//    public void close() {
//        // not sure why the tag check for backstack is not working properly
//        FragmentManager manager = getActivity().getSupportFragmentManager();
////        FragmentTransaction trans = manager.beginTransaction();
////        trans.remove(this);
////        trans.commit();
////        manager.popBackStack();
//        getActivity().onBackPressed();
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

    public void addStudent(Student student){

    }

    public static NewLessonFragment newInstance(Lesson lesson) {
        NewLessonFragment f = new NewLessonFragment();

        if (lesson == null) {
            f.lesson = null;
        } else {
            f.lesson = lesson;
            f.selectedStudents = lesson.students;
        }
        return f;
    }
}
