package orbital.dingletutors;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import static orbital.dingletutors.NotificationFragment.createDialog;

/**
 * Created by Muruges on 17/5/2017.
 */


/**
 * Created by Muruges on 17/5/2017.
 */

public class LessonHistoryFragment extends Fragment {
    private ArrayList<Lesson> lessons;
    private RecyclerView rv;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.lesson_history, container, false);
        rv = (RecyclerView) v.findViewById(R.id.lesson_history_rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        // populate with all the lessons that have been checked in, with or without summary report
        lessons = new ArrayList<>();
        lessons.addAll(MinuteUpdater.lessonHistoryMap);
//        // populate the arraylist for now we use test lessons
//        Lesson testLesson1 = Lesson.init(Calendar.getInstance().getTime());
//        testLesson1.students.add(new Student("Cindy", "Mother", "12345678"));
//        testLesson1.students.add(new Student("Damien", "Father", "91827364"));
//        testLesson1.setSummaryReport("test");
//        lessons.add(testLesson1);
//        lessons.add(testLesson1);
//        lessons.add(testLesson1);
//        lessons.add(testLesson1);
//        lessons.add(testLesson1);
//        lessons.add(testLesson1);
//        lessons.add(testLesson1);
//        lessons.add(testLesson1);
//        lessons.add(testLesson1);

        LessonListRV lessonListAdapter = new LessonListRV(true, R.layout.view_lesson_rv, lessons,
                new LessonListRV.OnItemClickListener() {
                    @Override
                    public void onItemClick(final Lesson lesson) {
                        StudentAdapter adapter = new StudentAdapter(getContext(),
                                R.layout.view_student, lesson.students);
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
                        if (lesson.getSummaryReport() == null) { // in case summary report not filled up yet
                            builder.setPositiveButton("write summary", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // dont do anything here
                                }
                            });
                        } else {
                            builder.setPositiveButton("view summary", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // dont do anything here
                                }
                            });
                        }
                        final AlertDialog alert = builder.create();
                        alert.show();
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                            }
                        });
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (lesson.getSummaryReport() == null){ // in case summary report not filled up
                                    alert.dismiss();
                                    SummaryReportFragment summaryReportFragment = SummaryReportFragment.newInstance(lesson);
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    // putting animation for fragment transaction
                                    transaction.setCustomAnimations(R.anim.slide_in_up, android.R.anim.fade_out,
                                            android.R.anim.fade_in, R.anim.slide_out_down);
                                    transaction.replace(R.id.drawer_container, summaryReportFragment) // carry out the transaction
                                            .addToBackStack("Writing summary") // add to backstack
                                            .commit();
                                } else {
                                    // just display summary report
                                    alert.dismiss();
                                    ViewSummaryReportFragment viewSummaryReportFragment = ViewSummaryReportFragment.newInstance(lesson);
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.setCustomAnimations(R.anim.slide_in_up, android.R.anim.fade_out,
                                            android.R.anim.fade_in, R.anim.slide_out_down);
                                    transaction.replace(R.id.drawer_container, viewSummaryReportFragment) // carry out the transaction
                                            .addToBackStack("Viewing summary") // add to backstack
                                            .commit();
                                }
                            }
                        });
                    }
                },
                new LessonListRV.OnItemClickListener() {
                    @Override
                    public void onItemClick(Lesson lesson) {
                        // ignore this
                    }
                });

        rv.setAdapter(lessonListAdapter);
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