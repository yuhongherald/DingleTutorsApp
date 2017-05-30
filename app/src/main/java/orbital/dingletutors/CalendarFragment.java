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
import java.util.Map;
import java.util.Set;


public class CalendarFragment extends Fragment {
    MonthMap selectedMonth;
    DayMap selectedDay;
    Popup<Integer> popup;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.calendar, container, false);

        final CaldroidFragment caldroidFragment = new CaldroidFragment();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy,\nEEEE");
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
                Toast.makeText(getActivity().getApplicationContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();
                if (selectedDay != null && selectedDay.isEmpty()) {
                    if (selectedMonth.remove(selectedDay.key) == null) {
                        Log.v("SelectDate", "Invalid key mapping when tried to remove.");
                    } else {
                        Log.v("SelectDate", "Deleting previous day mapping");

                    }
                }
                selectedDay = selectedMonth.get(formatter.format(date));
                if (selectedDay == null) {
                    selectedDay = new DayMap(formatter.format(date));
                    selectedMonth.put(selectedDay.key, selectedDay);
                    Log.v("SelectDate", "New day created and added to map.");
                }
                // I create a popup window and supply it with DayMap
                // and the list element format
                // and the button on click listener
//                popup = new Popup<Integer>(getActivity(), getView(), selectedDay,
//                        formatter.format(date), R.layout.view_lesson, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        MainActivity.addLesson(v);
//                    }
//                });
//                popup.updateList();
//                popup.showPopup();
                tv.setText(formatter.format(date));
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
//                Toast.makeText(getActivity().getApplicationContext(), text,
//                        Toast.LENGTH_SHORT).show();
                if (selectedMonth != null && selectedMonth.isEmpty()) {
                    CalendarMap.map.remove(selectedMonth.key);
                }
                selectedMonth = CalendarMap.map.get(month + "-" + year);
                if (selectedMonth != null) {
                    ColorDrawable green = new ColorDrawable(Color.GREEN);
                    // time to mark each days on the calendar
                    Set<Map.Entry<String, DayMap>> set = selectedMonth.entrySet();
                    for (Map.Entry<String, DayMap> day : set) {
                        try {
                            caldroidFragment.setBackgroundDrawableForDate(green, formatter.parse(day.getKey()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    selectedMonth = new MonthMap(month + "-" + year);
                    CalendarMap.map.put(selectedMonth.key, selectedMonth);
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

        // defining what happens when we click the new lesson button
        Button newLessonBtn = (Button) v.findViewById(R.id.add_button);
        newLessonBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                NewLessonFragment newLesson = new NewLessonFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                // putting animation for fragment transaction
                transaction.setCustomAnimations(R.anim.slide_in_up, android.R.anim.fade_out,
                        android.R.anim.fade_in, R.anim.slide_out_down);
                transaction.replace(R.id.calendar_container,newLesson) // carry out the transaction
                        .addToBackStack(null) // add to backstack
                        .commit();
            }
        });
        ImageButton viewLessonBtn = (ImageButton) v.findViewById(R.id.list_lessons);
        viewLessonBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                LessonListFragment viewLesson = LessonListFragment.newInstance(selectedDay);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                // putting animation for fragment transaction
                transaction.setCustomAnimations(R.anim.slide_in_up, android.R.anim.fade_out,
                        android.R.anim.fade_in, R.anim.slide_out_down);
                transaction.replace(R.id.calendar,viewLesson) // carry out the transaction
                        .addToBackStack(null) // add to backstack
                        .commit();
            }
        });

        return v;
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