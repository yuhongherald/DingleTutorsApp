package orbital.dingletutors;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Muruges on 20/5/2017.
 */

public class NewLessonFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.new_lesson, container, false);

        // Put whatever we want for creating a lesson reminder
        // such as fields for name of student, time etc.

        return v;
    }
}
