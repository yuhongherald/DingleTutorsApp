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
import android.widget.DatePicker;
import android.widget.LinearLayout;
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

/**
 * Created by Muruges on 20/5/2017.
 */

public class NewLessonFragment extends Fragment {
//    RelativeLayout newStudent;
    Lesson lesson;
    Date oldDate;

    static SimpleDateFormat timeformat = new SimpleDateFormat("h:mm a");
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
        selectedStudents = new ArrayList<>();
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

        classDate.setText(CalendarFragment.formatter.format(currentDate));
        startTime.setText(timeformat.format(new Date()));
        currentHour = cal.get(Calendar.HOUR_OF_DAY);
        currentMinute = cal.get(Calendar.MINUTE);
        updateStudents();

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

        v.findViewById(R.id.saveLesson).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedStudents.isEmpty()){
                    Toast.makeText(getActivity(), "No students selected yet", Toast.LENGTH_SHORT).show();
                    return;
                }

                cal.setTime(currentDate);
                cal.set(Calendar.HOUR, currentHour);
                cal.set(Calendar.MINUTE, currentMinute);
                currentDate = cal.getTime();
                Lesson newLesson = Lesson.remap(currentDate);

                if(newLesson != null && newLesson != lesson) {
                    Toast.makeText(getActivity(), "Lesson in conflict with: " + newLesson.name, Toast.LENGTH_SHORT).show();
                    return;
                } else if (currentDate.before(Calendar.getInstance().getTime())) {
                    Toast.makeText(getActivity(), "Please set lesson after current time", Toast.LENGTH_SHORT).show();
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
        v.findViewById(R.id.cancelLesson).setOnClickListener(new View.OnClickListener() {
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
            currentFragment.updateList();
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
