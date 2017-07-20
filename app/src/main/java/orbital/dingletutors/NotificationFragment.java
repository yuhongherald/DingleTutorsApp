package orbital.dingletutors;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

/**
 * Created by Muruges on 2/7/2017.
 */

public class NotificationFragment extends Fragment {
    public static ImageView imgNotification;
    public static Drawable img;
    private NotificationFragment thisFragment;

    public Lesson upcomingLesson;
    private TextView upcomingTime;
    private TextView upcomingLevel;
    private TextView upcomingName;
    private TextView upcomingSize;
    private ExpandableLayout upcomingExpandable;
    private LayoutInflater inflater;
    private RelativeLayout upcomingContainer;
    private View upcomingHeader;
    private ExpandableLayout summaryExpandable;
    private RecyclerView rv;
    private TextView notificationCount;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.notifications_recycler_view, container, false);
        upcomingExpandable = (ExpandableLayout) v.findViewById(R.id.upcoming_expandable);
        upcomingHeader = v.findViewById(R.id.upcoming_header);
        upcomingContainer = (RelativeLayout) v.findViewById(R.id.upcoming_container);

        this.inflater = inflater;
        upcomingTime = (TextView) v.findViewById(R.id.upcoming_time);
        upcomingLevel = (TextView) v.findViewById(R.id.upcoming_level);
        upcomingName = (TextView) v.findViewById(R.id.upcoming_name);
        upcomingSize = (TextView) v.findViewById(R.id.upcoming_size);
        imgNotification = (ImageView) v.findViewById(R.id.upcoming_img);

        // Just for testing purpose
        // can comment and uncomment if want to see how it looks like

//        upcomingLesson = Lesson.init(Calendar.getInstance().getTime());
//        upcomingLesson.students.add(new Student("Alice", "Mother", "12345678"));
//        upcomingLesson.students.add(new Student("Bob", "Father", "87654321"));
//        upcomingLesson.lessonDate = Calendar.getInstance().getTime();
        // everything above was for testing

        upcomingHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Lesson> lessons = MinuteUpdater.getLessons();
                if (!lessons.isEmpty()) {
                    openUpcomingLesson(lessons);
                    upcomingExpandable.toggle();
                } else {
                    upcomingExpandable.collapse();
                }
                // if (upcomingLesson != null) upcomingExpandable.toggle();
            }
        });

        View summaryHeader = v.findViewById(R.id.summary_header);
        summaryExpandable = (ExpandableLayout) v.findViewById(R.id.summary_expandable);
        summaryHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Lesson> lessons = MinuteUpdater.lessonWithoutReports;
                if (!lessons.isEmpty()) {
                    openLessonSummary(lessons);
                    summaryExpandable.toggle();
                } else {
                    summaryExpandable.collapse();
                }
            }
        });

        rv = (RecyclerView) v.findViewById(R.id.notifications_rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        notificationCount = (TextView) v.findViewById(R.id.summary_count);
//        ArrayList<Lesson> lessonsWithoutSummary = new ArrayList<>();
        // populate the arraylist for now we use test lessons
//        Lesson testLesson1 = Lesson.init(Calendar.getInstance().getTime());
//        testLesson1.students.add(new Student("Cindy", "Mother", "12345678"));
//        testLesson1.students.add(new Student("Damien", "Father", "91827364"));
//        lessonsWithoutSummary.add(testLesson1);
//        lessonsWithoutSummary.add(testLesson1);

        thisFragment = this;
        updateSummaryCount();
        upcomingHeader.callOnClick();

        return v;
    }

    private void updateSummaryCount() {
        if (notificationCount == null) {
            return;
        }
        if (MinuteUpdater.lessonWithoutReports == null) {
            MinuteUpdater.loadMap(getContext());
        }
        notificationCount.setText(Integer.toString(MinuteUpdater.lessonWithoutReports.size()));
    }

    private void openLessonSummary(ArrayList<Lesson> lessons) {
        LessonListRV lessonsWithoutSummaryAdapter = new LessonListRV(true, R.layout.view_lesson_rv, lessons,
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
                        // this seems to work with drawer rather than notifications container
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                                summaryExpandable.toggle();
                                SummaryReportFragment summaryReportFragment = SummaryReportFragment.newInstance(lesson, thisFragment);
                                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                // putting animation for fragment transaction
                                transaction.setCustomAnimations(R.anim.slide_in_up, android.R.anim.fade_out,
                                        android.R.anim.fade_in, R.anim.slide_out_down);
                                transaction.replace(R.id.drawer_container, summaryReportFragment) // carry out the transaction
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
    }

    private void openUpcomingLesson(ArrayList<Lesson> lessons) {
        upcomingLesson = lessons.get(0);
        upcomingTime.setText(upcomingLesson.displayTime);
        upcomingLevel.setText(upcomingLesson.level);
        upcomingName.setText(upcomingLesson.name);
        upcomingSize.setText("Size: " + upcomingLesson.students.size());

        final StudentAdapter adapter = new StudentAdapter(getContext(),
                R.layout.view_student_checkin, upcomingLesson.students);

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
                            upcomingLesson.checkIn(getContext());
                            // send sms to parent here
                            // we assume only one student first
                            // this is untested havent try yet
                            //                        String phoneNo = upcomingLesson.students.get(0).clientNo;
//                            String phoneNo = "91112188";
//                            String studentName = upcomingLesson.students.get(0).studentName;
//                            String clientName = upcomingLesson.students.get(0).clientName;
//                            String message = "Hi Mr/Ms " + clientName + ". This is confirmation of the lesson with "
//                                    + studentName + " at " + upcomingLesson.displayTime;
//                            try {
//                                SmsManager smsManager = SmsManager.getDefault();
//                                smsManager.sendTextMessage(phoneNo, null, message, null, null);
//                                Toast.makeText(getContext(), "SMS sent!",
//                                        Toast.LENGTH_LONG).show();
//
//                            } catch (Exception e) {
//                                Toast.makeText(getContext(), "SMS failed, try again later!",
//                                        Toast.LENGTH_LONG).show();
//                                e.printStackTrace();
//                            }
                            // update the box to show new upcoming lesson if any
                            updateSummaryCount();
                        } else {
                            Toast.makeText(getContext(), "Lesson already checked in.",
                                    Toast.LENGTH_LONG).show();
                        }
                        upcomingHeader.callOnClick();
//                        DrawerMenuItem.mCallBack.onNotificationsSelected();
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
