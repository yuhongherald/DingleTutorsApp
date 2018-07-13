package orbital.dingletutors;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
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
        final TextView studentName = (TextView) v.findViewById(R.id.studentName);
        studentName.setText("Student: " + lesson.getNextStudent().studentName);


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
                Student currentStudent = lesson.getNextStudent();
                lesson.setSummaryReport(report);

                boolean SMSSuccessful = true;
                try {
                    // TODO // Do some magic here and send report as sms then show the toast
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(currentStudent.clientNo, null, report, null, null);
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Summary report has been saved and successfully sent to the contacts of the parent via SMS",
                            Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    SMSSuccessful = false;
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Error while sending SMS. Summary report has been saved.",
                            Toast.LENGTH_SHORT).show();
                }

                if (lesson.areSummaryReportsComplete()){
                    if (MinuteUpdater.lessonWithoutReports == null) {
                        MinuteUpdater.loadMap(getContext());
                    }
                    lesson.nextReportCheck = null; // Just in case
                    MinuteUpdater.lessonWithoutReports.remove(lesson);
                    getActivity().onBackPressed();
                } else {
                    studentName.setText("Student: " + lesson.getNextStudent().studentName);
                    editText.setText("");
                }
            }
        });


        return v;
    }

    public static SummaryReportFragment newInstance(@NonNull Lesson lesson) {
//        @NonNull NotificationFragment parent
//        I just commented the parent temporarily
        SummaryReportFragment f = new SummaryReportFragment();
        f.lesson = lesson;
//        f.parent = parent;
        return f;
    }
}
