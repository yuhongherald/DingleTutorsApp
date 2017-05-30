package orbital.dingletutors;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Muruges on 30/5/2017.
 */

public class LessonListFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lesson_list, container, false);

        ArrayList<TextView> list = new ArrayList<TextView>();
        list.add( (TextView) v.findViewById(R.id.lesson1) );
        list.add( (TextView) v.findViewById(R.id.lesson2) );
        list.add( (TextView) v.findViewById(R.id.lesson3) );
        list.add( (TextView) v.findViewById(R.id.lesson4) );
        list.add( (TextView) v.findViewById(R.id.lesson5) );

//         instead of of 5 can use size of daymap here
        for (int i=1; i<6; i++){
            list.get(i-1).setText(getArguments().getStringArray("Lesson names")[i-1]);
        }

        return v;
    }

    public static LessonListFragment newInstance(DayMap dayMap){
        LessonListFragment f = new LessonListFragment();
        // do for loop here and extract all the data necessary from daymap here
        Bundle b = new Bundle();
        b.putStringArray("Lesson names", new String[]{"Lesson 1", "Lesson 2", "Lesson 3", "Lesson 4", "Lesson 5"});

        f.setArguments(b);
        return f;
    }
}
