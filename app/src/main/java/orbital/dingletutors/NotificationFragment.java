package orbital.dingletutors;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Muruges on 2/7/2017.
 */

public class NotificationFragment extends Fragment {

    public Lesson upcomingLesson;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.notifications_recycler_view, container, false);
        final ExpandableLayout upcomingExpandable = (ExpandableLayout) v.findViewById(R.id.upcoming_expandable);
        TextView upcomingTV = (TextView) v.findViewById(R.id.upcoming_tv);
        RelativeLayout upcomingContainer = (RelativeLayout) v.findViewById(R.id.upcoming_container);

        TextView upcomingTime = (TextView) v.findViewById(R.id.upcoming_time);
        TextView upcomingLevel = (TextView) v.findViewById(R.id.upcoming_level);
        TextView upcomingName = (TextView) v.findViewById(R.id.upcoming_name);
        TextView upcomingSize = (TextView) v.findViewById(R.id.upcoming_size);

        // Just for testing purpose
        // can comment and uncomment if want to see how it looks like

        upcomingLesson = Lesson.init(Calendar.getInstance().getTime());
        upcomingLesson.students.add(new Student("Alice", "Mother", "12345678"));
        upcomingLesson.students.add(new Student("Bob", "Father", "87654321"));
        upcomingLesson.lessonDate = Calendar.getInstance().getTime();
        // everything above was for testing

        final StudentAdapter adapter = new StudentAdapter(getContext(),
                R.layout.view_student_checkin, upcomingLesson.students);
        if (upcomingLesson != null) {
            upcomingTime.setText(upcomingLesson.displayTime);
            upcomingLevel.setText(upcomingLesson.level);
            upcomingName.setText(upcomingLesson.name);
            upcomingSize.setText("Size: " + upcomingLesson.students.size());
        }


        upcomingTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (upcomingLesson != null) upcomingExpandable.toggle();
            }
        });
        upcomingContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                upcomingExpandable.toggle();
                final View dialogLayout = inflater.inflate(R.layout.upcoming_lesson, null);
                TextView displayUpcomingTime = (TextView) dialogLayout.findViewById(R.id.display_upcoming_time);
                TextView displayUpcomingLevel = (TextView) dialogLayout.findViewById(R.id.display_upcoming_name);
                TextView displayUpcomingName = (TextView) dialogLayout.findViewById(R.id.display_upcoming_level);
                TextView displayUpcomingDate = (TextView) dialogLayout.findViewById(R.id.display_upcoming_date);
                ListView displayListStudents = (ListView) dialogLayout.findViewById(R.id.display_list_students);
                displayUpcomingTime.setText(upcomingLesson.displayTime);
                displayUpcomingLevel.setText(upcomingLesson.level);
                displayUpcomingName.setText(upcomingLesson.name);
                displayUpcomingDate.setText(CalendarFragment.formatter.format(upcomingLesson.lessonDate));
                displayListStudents.setAdapter(adapter);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Upcoming Lesson details");
                builder.setView(dialogLayout);
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        // dont do anything here
                    }
                });
                builder.setPositiveButton("check in", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // dont do anything here
                    }
                });
                final AlertDialog alert = builder.create();
                alert.show();
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        upcomingLesson.checkIn();
                        // send sms to parent here
                        // we assume only one student first
                        // this is untested havent try yet
                        String phoneNo = upcomingLesson.students.get(0).clientNo;
                        String studentName = upcomingLesson.students.get(0).studentName;
                        String clientName = upcomingLesson.students.get(0).clientName;
                        String message = "Hi Mr/Ms " + clientName + ". This is confirmation of the lesson with "
                                + studentName + " at " + upcomingLesson.displayTime;
                        try {
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phoneNo, null, message, null, null);
                            Toast.makeText(getContext(), "SMS sent!",
                                    Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            Toast.makeText(getContext(), "SMS failed, try again later!",
                                    Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        // update the box to show new upcoming lesson if any
                        alert.dismiss();
                    }
                });
                alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });
            }
        });


        RecyclerView rv = (RecyclerView) v.findViewById(R.id.notifications_rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        return v;
    }

    public static NotificationFragment newInstance(){
        return new NotificationFragment();
    }
}
