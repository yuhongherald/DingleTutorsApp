package orbital.dingletutors;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Muruges on 20/7/2017.
 */

public class ViewSummaryReportFragment extends Fragment {
    private Lesson lesson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.view_summary_report, container, false);
        ((TextView)v.findViewById(R.id.summary_text)).setText(lesson.getSummaryReport());
        v.findViewById(R.id.go_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        return v;
    }

    public static ViewSummaryReportFragment newInstance(Lesson lesson) {
        ViewSummaryReportFragment f = new ViewSummaryReportFragment();
        f.lesson = lesson;

        return f;
    }
}
