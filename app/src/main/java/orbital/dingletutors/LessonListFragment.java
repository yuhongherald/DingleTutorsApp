package orbital.dingletutors;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Created by Muruges on 30/5/2017.
 */

public class LessonListFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.popup, container, false);
        // static reference
        updateList((ListView) v);

//        ArrayList<TextView> list = new ArrayList<TextView>();
//        list.add( (TextView) v.findViewById(R.id.lesson1) );
//        list.add( (TextView) v.findViewById(R.id.lesson2) );
//        list.add( (TextView) v.findViewById(R.id.lesson3) );
//        list.add( (TextView) v.findViewById(R.id.lesson4) );
//        list.add( (TextView) v.findViewById(R.id.lesson5) );

//         instead of of 5 can use size of daymap here
//        for (int i=1; i<6; i++){
//            list.get(i-1).setText(getArguments().getStringArray("Lesson names")[i-1]);
//        }

        return v;
    }

    public void updateList(ListView v) {
        if (CalendarFragment.selectedDay == null) {
            Log.v("LessonFragment", "selectedDay not initialized");
            return;
        }
        v.removeAllViewsInLayout(); // the most inefficient way to sort the views
        // if an entry is edited it will be deleted and placed back in
        Set<Map.Entry<Integer, Lesson>> set = CalendarFragment.selectedDay.entrySet();
        RelativeLayout layout;
        Lesson lesson;
        for (Map.Entry<Integer, Lesson> entry : set) {
            layout = (RelativeLayout) getActivity().getLayoutInflater()
                    .inflate(R.layout.view_lesson, null);
            lesson = entry.getValue();
            ((TextView) layout.findViewById(R.id.time)).setText(lesson.displayTime);
            ((TextView) layout.findViewById(R.id.name)).setText(lesson.name);
            ((TextView) layout.findViewById(R.id.level)).setText(lesson.levels[lesson.level]);
            ((TextView) layout.findViewById(R.id.size)).setText(lesson.size());
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            v.addView(layout);
        }
    }

    public static LessonListFragment newInstance(DayMap dayMap){
        LessonListFragment f = new LessonListFragment();
        // do for loop here and extract all the data necessary from daymap here

        // just going to refer to static reference

//        b.putStringArray("Lesson names", new String[]{"Lesson 1", "Lesson 2", "Lesson 3", "Lesson 4", "Lesson 5"});
//        f.setArguments(b);
        return f;
    }
}
