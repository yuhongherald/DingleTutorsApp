package orbital.dingletutors;

/**
 * Created by Muruges on 17/5/2017.
 */

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Map;
import java.util.Set;


public class CalendarFragment extends Fragment {
    public static CalendarFragment thisFragment;
    final CaldroidFragment caldroidFragment = new CaldroidFragment();
    public final static SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
    public final static ColorDrawable green = new ColorDrawable(Color.GREEN);
    public final static ColorDrawable white = new ColorDrawable(Color.WHITE);
    public static MonthMap selectedMonth;
    public static DayMap selectedDay;
    public static Lesson selectedLesson;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        thisFragment = this;
        final View v = inflater.inflate(R.layout.calendar, container, false);
        Bundle args = new Bundle();
        Calendar calendar = Calendar.getInstance();
        args.putInt("month", calendar.get(Calendar.MONTH) + 1);
        args.putInt("year", calendar.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
        caldroidFragment.setArguments(args);
        caldroidFragment.setMinDate(calendar.getTime());

        // setting the initial date in text view
        final TextView tv = (TextView) v.findViewById(R.id.title);
        Date date = calendar.getTime();
        tv.setText(formatter.format(date));
        caldroidFragment.refreshView();

        final FragmentTransaction t = getFragmentManager().beginTransaction();
        t.replace(R.id.calendar, caldroidFragment);
        t.commit();

        // time to get hashmap from file


        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
//                Toast.makeText(getActivity().getApplicationContext(), formatter.format(date),
//                        Toast.LENGTH_SHORT).show();
                deleteDay();
                selectedDay = selectedMonth.get(formatter.format(date));
                if (selectedDay == null) {
                    Log.v("selectDate", "no existing date");
                    selectedDay = new DayMap(formatter.format(date), selectedMonth);
                }

                tv.setText(formatter.format(date));
            }

            @Override
            public void onChangeMonth(int month, int year) {
                boundDates(month, year);
                String text = "month: " + month + " year: " + year;
//                Toast.makeText(getActivity().getApplicationContext(), text,
//                        Toast.LENGTH_SHORT).show();
                deleteMonth();
                selectedMonth = MinuteUpdater.calendarMap.get(month + "-" + year);
                if (selectedMonth != null) {
                    // time to mark each days on the calendar
                    Set<Map.Entry<String, DayMap>> set = selectedMonth.entrySet();
                    // might want to hard recolor everything to white

                    for (Map.Entry<String, DayMap> day : set) {
                        try {
                            if (!day.getValue().isEmpty()) {
                                caldroidFragment.setBackgroundDrawableForDate(green, formatter.parse(day.getKey()));
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    selectedMonth = new MonthMap(month + "-" + year, MinuteUpdater.calendarMap);
                }
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                // Same as normal select date
                // We can change this to add event directly in the future when we have the functionality later on if you want
                onSelectDate(date, view);

            }

            @Override
            public void onCaldroidViewCreated() {
//                Toast.makeText(getActivity().getApplicationContext(),
//                        "Caldroid view is created",
//                        Toast.LENGTH_SHORT).show();

                // now we have to get the current month and load up all the notifications
            }

        };
        caldroidFragment.setCaldroidListener(listener);
        // initialize selected month and day
        Calendar cal = Calendar.getInstance();
        listener.onChangeMonth(cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR));
        listener.onSelectDate(cal.getTime(), v);

        // defining what happens when we click the new lesson button
        Button newLessonBtn = (Button) v.findViewById(R.id.add_button);
        newLessonBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (getFragmentManager().findFragmentByTag("newLesson") != null) {
                    Log.v("newLesson", "exists");
                    return;
                }
                NewLessonFragment newLesson = NewLessonFragment.newInstance(null);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                // putting animation for fragment transaction
                transaction.setCustomAnimations(R.anim.slide_in_up, android.R.anim.fade_out,
                        android.R.anim.fade_in, R.anim.slide_out_down);
                transaction.replace(R.id.calendar_container,newLesson) // carry out the transaction
                        .addToBackStack("newLesson") // add to backstack
                        .commit();
            }
        });

        ImageButton viewLessonBtn = (ImageButton) v.findViewById(R.id.list_lessons);
        viewLessonBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (getFragmentManager().findFragmentByTag("viewLesson") != null) {
                    Log.v("viewLesson", "exists");
                    return;
                }
                LessonListFragment viewLesson = LessonListFragment.newInstance(selectedDay);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                // putting animation for fragment transaction
                transaction.setCustomAnimations(R.anim.slide_in_up, android.R.anim.fade_out,
                        android.R.anim.fade_in, R.anim.slide_out_down);
                transaction.replace(R.id.calendar,viewLesson,"viewLesson") // carry out the transaction
                        .addToBackStack("viewLesson") // add to backstack
                        .commit();
            }
        });


        return v;
    }

    @Override
    public void onDestroyView() {
        thisFragment = null;
        super.onDestroyView();
    }

    // mor like update day now
    public void deleteDay() {
        if (selectedDay == null) {
            Log.v("deleteDay", "no day selected");
            return;
        }
        if (selectedDay.isEmpty()) {
            // uncolor the date on the calendar
            try {
                caldroidFragment.setBackgroundDrawableForDate(white, formatter.parse(selectedDay.key));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            selectedDay.delete();
        } else {
            try {
                caldroidFragment.setBackgroundDrawableForDate(green, formatter.parse(selectedDay.key));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        caldroidFragment.refreshView();
    }

    public void deleteMonth() {
        deleteDay();
        if (selectedMonth != null && selectedMonth.isEmpty()) {
            selectedMonth.delete();
        }
    }

    public void boundDates(int month, int year) {
        caldroidFragment.clearDisableDates();
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();
        cal.clear();
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date monthStart = cal.getTime();
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date monthEnd = cal.getTime();

        // always take the later date
        caldroidFragment.setMinDate(currentDate.before(monthStart) ? monthStart : currentDate);
        caldroidFragment.setMaxDate(currentDate.before(monthEnd) ? monthEnd : currentDate);
        caldroidFragment.refreshView();
    }

    public static CalendarFragment newInstance() {

        CalendarFragment f = new CalendarFragment();
//        Bundle b = new Bundle();
//        b.putString("msg", text);
//
//        f.setArguments(b);

        return f;
    }


}