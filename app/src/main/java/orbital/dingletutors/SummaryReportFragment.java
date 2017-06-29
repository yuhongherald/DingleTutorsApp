package orbital.dingletutors;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Muruges on 29/6/2017.
 */

public class SummaryReportFragment extends Fragment{
    private Lesson lesson;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.summary_report_fragment, container, false);
        final EditText editText = (EditText) v.findViewById(R.id.edtInputSummary);
        v.findViewById(R.id.cancelLessonSummary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
//                close();

            }
        });
        v.findViewById(R.id.saveLessonSummary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().length() == 0){
                    editText.setError("Cannot be empty");
                    return;
                }

                lesson.setSummaryReport(editText.getText().toString());

                // Do some magic here and send report as sms then show the toast

                Toast.makeText(getActivity().getApplicationContext(),
                        "Summary report has been saved and sent to the contacts of parents via SMS",
                        Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });


        return v;
    }

    public static SummaryReportFragment newInstance(@NonNull Lesson lesson) {

        SummaryReportFragment f = new SummaryReportFragment();
        f.lesson = lesson;

        return f;
    }
}
