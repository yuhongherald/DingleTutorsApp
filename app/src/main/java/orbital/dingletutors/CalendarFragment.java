package orbital.dingletutors;

/**
 * Created by Muruges on 17/5/2017.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CalendarFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.calendar, container, false);

        final CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar calendar = Calendar.getInstance();
        args.putInt("month", calendar.get(Calendar.MONTH) + 1);
        args.putInt("year", calendar.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
        caldroidFragment.setArguments(args);

        FragmentTransaction t = getChildFragmentManager().beginTransaction();
        t.replace(R.id.calendar, caldroidFragment);
        t.commit();

        // time to get hashmap from file

        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                Toast.makeText(getActivity().getApplicationContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                Toast.makeText(getActivity().getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                // Same as normal select date
                // We can change this to add event directly in the future when we have the functionality later on if you want
                onSelectDate(date, view);

            }

            @Override
            public void onCaldroidViewCreated() {
                Toast.makeText(getActivity().getApplicationContext(),
                        "Caldroid view is created",
                        Toast.LENGTH_SHORT).show();
            }

        };
        caldroidFragment.setCaldroidListener(listener);

        // defining what happens when we click the new lesson button
        Button newLessonBtn = (Button) v.findViewById(R.id.newLessonBtn);
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