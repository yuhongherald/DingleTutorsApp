package orbital.dingletutors;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    TextView repeatDuration;

    // for selection of multiple dates for repeat
    boolean[] repeatDaysSelected;
    boolean atLeastOneRepeatDay;
    ExpandableLayout repeatDays;
    ExpandableLayout singleDate;
    TextView monday;
    TextView tuesday;
    TextView wednesday;
    TextView thursday;
    TextView friday;
    TextView saturday;
    TextView sunday;

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

        final Calendar cal = Calendar.getInstance();

        className = (TextView) v.findViewById(R.id.classNameVal);
        classDate = (TextView) v.findViewById(R.id.dateVal);
        duration = (TextView) v.findViewById(R.id.durationVal);
        startTime = (TextView) v.findViewById(R.id.startTimeVal);
        studentName = (TextView) v.findViewById(R.id.studentNameVal);
        educationLevel = (TextView) v.findViewById(R.id.levelVal);
        repeatDuration = (TextView) v.findViewById(R.id.repeatDurationVal);

        classDate.setText(CalendarFragment.formatter.format(currentDate));
        startTime.setText(timeformat.format(new Date()));
        currentHour = cal.get(Calendar.HOUR_OF_DAY);
        currentMinute = cal.get(Calendar.MINUTE);

        singleDate = (ExpandableLayout) v.findViewById(R.id.dateEL);
        repeatDays = (ExpandableLayout) v.findViewById(R.id.repeatDuration);
        monday = (TextView) v.findViewById(R.id.monday);
        tuesday = (TextView) v.findViewById(R.id.tuesday);
        wednesday = (TextView) v.findViewById(R.id.wednesday);
        thursday = (TextView) v.findViewById(R.id.thursday);
        friday = (TextView) v.findViewById(R.id.friday);
        saturday = (TextView) v.findViewById(R.id.saturday);
        sunday = (TextView) v.findViewById(R.id.sunday);

        final TextView[] days = {monday, tuesday, wednesday, thursday, friday, saturday, sunday};

        if (lesson != null && lesson.recurringLesson != null && lesson.recurringLesson.sessionsAWeek > 0) {
            atLeastOneRepeatDay = true;
            repeatDaysSelected = lesson.recurringLesson.daysOfWeek; // no duplication, no modification
        } else {
            atLeastOneRepeatDay = false;
            // Monday to Sunday assume that all are not selected first
            repeatDaysSelected = new boolean[]{false, false, false, false, false, false, false};
        }

        if (lesson != null){
            oldDate = lesson.lessonDate;
            Set<Map.Entry<String, Integer>> set = durationStringToInt.entrySet();
            for (Map.Entry<String, Integer> entry : set) {
                if (entry.getValue() == lesson.duration) {
                    duration.setText(entry.getKey()); // not very convenient
                }
            }
            className.setText(lesson.name);
            startTime.setText(lesson.displayTime);
            educationLevel.setText(lesson.level);
            updateStudents();
        }

        // Setting buttons
        if (lesson != null && lesson.recurringLesson != null) {
            if (lesson.recurringLesson.weeks == 1) {
                repeatDuration.setText("1 Week");
            } else {
                repeatDuration.setText(lesson.recurringLesson.weeks + "Weeks");
            }
            for (int i = 0; i < 7; i++) {
                if (repeatDaysSelected[i]){
                    days[i].setBackgroundResource(R.drawable.customborderselected);
                    days[i].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    days[i].setBackgroundResource(R.drawable.customborder);
                    days[i].setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                }
            }
            v.findViewById(R.id.saveLesson).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().onBackPressed();
                }
            });
            repeatDays.expand();
        } else {
            for (int i = 0; i<7; i++) {
                if (repeatDaysSelected[i]) {
                    days[i].setBackgroundResource(R.drawable.customborderselected);
                    days[i].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                } else {
                    days[i].setBackgroundResource(R.drawable.customborder);
                    days[i].setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                }
                final int finalI = i;
                days[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // toggle the value on click
                        repeatDaysSelected[finalI] = !repeatDaysSelected[finalI];
                        checkRepeatDays();
                        // change the highlighting based on previous state
                        if (!repeatDaysSelected[finalI]) {
                            days[finalI].setBackgroundResource(R.drawable.customborder);
                            days[finalI].setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                        } else {
                            days[finalI].setBackgroundResource(R.drawable.customborderselected);
                            days[finalI].setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                        }
                        if (atLeastOneRepeatDay) {
                            singleDate.collapse();
                            repeatDays.expand();
                        } else {
                            singleDate.expand();
                            repeatDays.collapse();
                        }
                    }
                });
            }
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
            v.findViewById(R.id.repeatDuration).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SingleChoiceDialog("repeatDurations");
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

                    if (atLeastOneRepeatDay) {
                        int weeks = Integer.parseInt(repeatDuration.getText().toString().replaceAll("[^0-9]", ""));
                        RecurringLesson recurringLesson = new RecurringLesson(
                                currentHour, currentMinute, weeks, repeatDaysSelected);
                        recurringLesson.duration = durationStringToInt.get(duration.getText().toString());
                        recurringLesson.name = className.getText().toString();
                        recurringLesson.level = educationLevel.getText().toString();
                        recurringLesson.students = selectedStudents;

                        cal.setTime(currentDate);
                        cal.set(Calendar.HOUR_OF_DAY, currentHour);
                        cal.set(Calendar.MINUTE, currentMinute);
                        currentDate = cal.getTime();
                        Date date = Calendar.getInstance().getTime();
                        if (date.after(currentDate)) {
                            currentDate = date;
                        }
                        Lesson newLesson = recurringLesson.validate(getContext(), currentDate);
                        if (newLesson != null) {
                            Toast.makeText(getActivity(), "Lesson in conflict with: " + newLesson.name + " "
                                    + CalendarFragment.formatter.format(lesson.lessonDate)
                                    + " " + newLesson.displayTime, Toast.LENGTH_SHORT).show();
                            return;
                        } else if (lesson != null) {
                            // remember to delete lesson if not null!
                            lesson.delete();
                        }
                    } else {
                        cal.setTime(currentDate);
                        cal.set(Calendar.HOUR_OF_DAY, currentHour);
                        cal.set(Calendar.MINUTE, currentMinute);
                        currentDate = cal.getTime();
                        Lesson newLesson = Lesson.remap(currentDate);

                        if (newLesson != null && newLesson != lesson) {
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
                    }
                    // we close this popup
                    getActivity().onBackPressed();
                    //                close();
                }
            });
        }

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
    public static final String[] subjectNames = { "Preschool", "English", "Mathematics", "Science", "Mother Tongue",
            "Higher Mother Tongue", "'A' Maths", "'E' Maths", "Literature", "Biology", "Chemistry", "Physics",
            "History", "Geography", "Economics", "Music", "Foreign Language", "Programming"};
    public static final String[] educationLevels = { "Preschool", "Primary", "'O' Level", "'A' Level", "Polytechnic"};
    public static final String[] durations = { "1 Hour", "1.5 Hours", "2 Hours", "2.5 Hours", "3 Hours"};
    public static final String[] repeatDurations = { "1 week", "2 weeks", "3 weeks", "4 weeks", "5 weeks", "6 weeks",
            "7 weeks", "8 weeks", "9 weeks", "10 weeks", "11 weeks", "12 weeks"};

    private void SingleChoiceDialog(String field){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        switch (field) {
            case "subjectNames":
                builder.setTitle("Subjects");
                builder.setItems(subjectNames, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        className.setText(subjectNames[which]);
                        dialog.dismiss();
                    }
                });
                break;
            case "educationLevels":
                builder.setTitle("Education levels");
                builder.setItems(educationLevels, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        educationLevel.setText(educationLevels[which]);
                        dialog.dismiss();
                    }
                });
                break;
            case "durations":
                builder.setTitle("Lesson Duration");
                builder.setItems(durations, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        duration.setText(durations[which]);
                        dialog.dismiss();
                    }
                });
                break;
            case "repeatDurations":
                builder.setTitle("Repeat Duration");
                builder.setItems(repeatDurations, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        repeatDuration.setText(repeatDurations[which]);
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
            if (selectedStudents.size() > 1) {
                this.studentName.setText(selectedStudents.size() + " students");
            } else {
                this.studentName.setText(selectedStudents.get(0).studentName);
            }
        } else {
            this.studentName.setText("None Selected Yet");
        }
    }
    public void checkRepeatDays() {
        for (boolean day:repeatDaysSelected){
            if (day) {
                atLeastOneRepeatDay = true;
                return;
            }
        }
        atLeastOneRepeatDay = false;
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
//            if (oldDate != null) {
//                CalendarFragment.thisFragment.recolorDay(oldDate);
//            }
//            CalendarFragment.thisFragment.recolorDay(currentDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(CalendarFragment.currentDate);
            CalendarFragment.thisFragment.caldroidFragment.getCaldroidListener().onChangeMonth(
                    CalendarFragment.thisFragment.caldroidFragment.getMonth()
                    , CalendarFragment.thisFragment.caldroidFragment.getYear()
            );
        }
        MinuteUpdater.getLessons();
        super.onDestroyView();
    }

    public static NewLessonFragment newInstance(Lesson lesson) {
        NewLessonFragment f = new NewLessonFragment();

        if (lesson == null) {
            f.lesson = null;
        } else {
            f.lesson = lesson;
            f.selectedStudents = (ArrayList<Student>) lesson.students.clone(); // to facilitate cancel
        }
        return f;
    }
}
