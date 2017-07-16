package orbital.dingletutors;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
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
                final View dialogLayout = createDialog(inflater, upcomingLesson, adapter);

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
                        if (!upcomingLesson.checkedIn) {
                            upcomingLesson.checkIn();
                            // send sms to parent here
                            // we assume only one student first
                            // this is untested havent try yet
                            //                        String phoneNo = upcomingLesson.students.get(0).clientNo;
                            String phoneNo = "91112188";
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
                        } else {
                            Toast.makeText(getContext(), "Lesson already checked in.",
                                    Toast.LENGTH_LONG).show();
                        }
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

        TextView summaryTV = (TextView) v.findViewById(R.id.summary_tv);
        final ExpandableLayout summaryExpandable = (ExpandableLayout) v.findViewById(R.id.summary_expandable);
        summaryTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                summaryExpandable.toggle();
            }
        });

        RecyclerView rv = (RecyclerView) v.findViewById(R.id.notifications_rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        ArrayList<Lesson> lessonsWithoutSummary = new ArrayList<>();
        // populate the arraylist for now we use test lessons
        Lesson testLesson1 = Lesson.init(Calendar.getInstance().getTime());
        testLesson1.students.add(new Student("Cindy", "Mother", "12345678"));
        testLesson1.students.add(new Student("Damien", "Father", "91827364"));
        lessonsWithoutSummary.add(testLesson1);
        lessonsWithoutSummary.add(testLesson1);
        LessonListRV lessonsWithoutSummaryAdapter = new LessonListRV(R.layout.view_lesson_rv, lessonsWithoutSummary,
                new LessonListRV.OnItemClickListener() {
                    @Override
                    public void onItemClick(final Lesson lesson) {
                        StudentAdapter adapter = new StudentAdapter(getContext(),
                                R.layout.view_student_checkin, lesson.students);
                        View dialogLayout = createDialog(inflater, lesson, adapter);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Lesson details");
                        builder.setView(dialogLayout);
                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                              // dont do anything here
                            }
                        });
                        builder.setPositiveButton("write summary", new DialogInterface.OnClickListener() {
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
                                alert.dismiss();
                                summaryExpandable.toggle();
                                SummaryReportFragment summaryReportFragment = SummaryReportFragment.newInstance(lesson);
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                // putting animation for fragment transaction
                                transaction.setCustomAnimations(R.anim.slide_in_up, android.R.anim.fade_out,
                                        android.R.anim.fade_in, R.anim.slide_out_down);
                                transaction.replace(R.id.notifications_container, summaryReportFragment) // carry out the transaction
                                        .addToBackStack("Writing summary") // add to backstack
                                        .commit();

                            }
                        });
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                            }
                        });
                    }
                },
                new LessonListRV.OnItemClickListener() {
                    @Override
                    public void onItemClick(Lesson lesson) {
                        return;
                    }
                });
        rv.setAdapter(lessonsWithoutSummaryAdapter);

        return v;
    }

    private View createDialog(LayoutInflater inflater, Lesson lesson, StudentAdapter adapter){
        View dialogLayout = inflater.inflate(R.layout.upcoming_lesson, null);
        TextView displayUpcomingTime = (TextView) dialogLayout.findViewById(R.id.display_upcoming_time);
        TextView displayUpcomingLevel = (TextView) dialogLayout.findViewById(R.id.display_upcoming_name);
        TextView displayUpcomingName = (TextView) dialogLayout.findViewById(R.id.display_upcoming_level);
        TextView displayUpcomingDate = (TextView) dialogLayout.findViewById(R.id.display_upcoming_date);
        ListView displayListStudents = (ListView) dialogLayout.findViewById(R.id.display_list_students);
        displayUpcomingTime.setText(lesson.displayTime);
        displayUpcomingLevel.setText(lesson.level);
        displayUpcomingName.setText(lesson.name);
        displayUpcomingDate.setText(CalendarFragment.formatter.format(lesson.lessonDate));
        displayListStudents.setAdapter(adapter);
        return dialogLayout;
    }

    public static NotificationFragment newInstance(){
        return new NotificationFragment();
    }
}
