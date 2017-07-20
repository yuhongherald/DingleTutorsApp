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
    private NotificationFragment parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.summary_report_fragment, container, false);
        final EditText editText = (EditText) v.findViewById(R.id.edtInputSummary);
        String oldReport = lesson.getSummaryReport();
        if (oldReport != null) {
            editText.setText(oldReport);
        }
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
                String report = editText.getText().toString().trim();
                if (report.length() == 0){
                    editText.setError("Cannot be empty");
                    return;
                }

                lesson.setSummaryReport(report);

                boolean SMSSuccessful = true;
                for (Student student : lesson.students) {
                    String number = student.clientNo;
                    try {
                        // TODO // Do some magic here and send report as sms then show the toast
                    } catch (Exception e) {
                        SMSSuccessful = false;
                        break;
                    }
                }
                if (SMSSuccessful) {
                    if (MinuteUpdater.lessonWithoutReports == null) {
                        MinuteUpdater.loadMap(getContext());
                    }
                    MinuteUpdater.lessonWithoutReports.remove(lesson);
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Summary report has been saved and sent to the contacts of parents via SMS",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Error while sending SMS. Summary report has been saved.",
                            Toast.LENGTH_SHORT).show();
                }


                getActivity().onBackPressed();
            }
        });


        return v;
    }

    public static SummaryReportFragment newInstance(@NonNull Lesson lesson, @NonNull NotificationFragment parent) {

        SummaryReportFragment f = new SummaryReportFragment();
        f.lesson = lesson;
        f.parent = parent;
        return f;
    }
}
