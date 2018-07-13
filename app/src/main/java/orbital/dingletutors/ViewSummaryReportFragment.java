package orbital.dingletutors;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Muruges on 20/7/2017.
 */

public class ViewSummaryReportFragment extends Fragment {
    private Lesson lesson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        if (lesson.students.size() > 1) {
            v = inflater.inflate(R.layout.view_summary_multiple, container, false);
        } else {
            v = inflater.inflate(R.layout.view_summary_report, container, false);
        }
        final TextView studentname = (TextView) v.findViewById(R.id.studentName);
        final TextView summary = (TextView) v.findViewById(R.id.summary_text);
        studentname.setText("Student: " + lesson.students.get(0).studentName);
        summary.setText(lesson.getSummaryReport(0));
        v.findViewById(R.id.go_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        if (lesson.students.size() > 1) {
            Button nextStudentBtn = (Button) v.findViewById(R.id.next_student_btn);
            final int[] i = {1}; // this is just to modify the counter since its final
                                 // we cant use an int must use one element array and then
                                 // can change the value of the element inside
            nextStudentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (i[0] == lesson.students.size()) {
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Last student reached",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    studentname.setText("Student: " + lesson.students.get(i[0]).studentName);
                    summary.setText(lesson.getSummaryReport(i[0]));
                    i[0]++;
                }
            });
        }

        return v;
    }

    public static ViewSummaryReportFragment newInstance(Lesson lesson) {
        ViewSummaryReportFragment f = new ViewSummaryReportFragment();
        f.lesson = lesson;

        return f;
    }
}
