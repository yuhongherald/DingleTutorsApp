package orbital.dingletutors;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Muruges on 17/5/2017.
 */


/**
 * Created by Muruges on 17/5/2017.
 */

public class LessonHistoryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lesson_history, container, false);

//        TextView tv = (TextView) v.findViewById(R.id.tvLessonHistory);
//        tv.setText(getArguments().getString("msg"));

        return v;
    }

    public static LessonHistoryFragment newInstance() {

        LessonHistoryFragment f = new LessonHistoryFragment();
//        Bundle b = new Bundle();
//        b.putString("msg", text);
//        f.setArguments(b);

        return f;
    }
}