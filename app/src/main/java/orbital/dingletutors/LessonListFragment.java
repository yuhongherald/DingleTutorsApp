package orbital.dingletutors;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.Set;

/**
 * Created by Muruges on 30/5/2017.
 */

public class LessonListFragment extends Fragment{
    MonthMap monthMap;
    DayMap dayMap;
    RecyclerView rv;
    ArrayList<Lesson> lessons;
    LessonListRV adapter;
    ImageButton returnBtn;
    View.OnClickListener originalListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recycler_view_list_lessons, container, false);
        rv = (RecyclerView) v.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        String stringDate = new SimpleDateFormat("mmHHddMMyyyy").format(CalendarFragment.currentDate);
        monthMap = MonthMap.init(Integer.parseInt(stringDate.substring(6, 8)) + "-" +
                Integer.parseInt(stringDate.substring(8, 12)), MinuteUpdater.calendarMap);
        dayMap = DayMap.init(CalendarFragment.formatter.format(CalendarFragment.currentDate), monthMap);
        Set<Map.Entry<Integer, Lesson>> set = dayMap.entrySet();
        lessons = new ArrayList<>();
        for (Map.Entry<Integer, Lesson> entry : set) {
            lessons.add(entry.getValue());
        }

        adapter = new LessonListRV(false, R.layout.view_lesson_rv, lessons, new LessonListRV.OnItemClickListener() {
            @Override
            public void onItemClick(Lesson lesson) {
                NewLessonFragment newLesson = NewLessonFragment.newInstance(lesson);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                // putting animation for fragment transaction
                transaction.setCustomAnimations(R.anim.slide_in_up, android.R.anim.fade_out,
                        android.R.anim.fade_in, R.anim.slide_out_down);
                transaction.replace(R.id.calendar_fragment_container, newLesson) // carry out the transaction
                        .addToBackStack("newLesson") // add to backstack
                        .commit();
            }
        }, new LessonListRV.OnItemClickListener() {
            @Override
            public void onItemClick(final Lesson lesson) {
                // over here i delete all other recurring lessons and update current month coloring in destroyview
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Delete lesson?");
                builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Don't do anything here
                    }
                });
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Don't do anything here
                    }
                });
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
                        if (lesson.recurringLesson != null) {
                            lesson.deleteAll();
                        } else {
                            lesson.delete();
                        }
                        Toast.makeText(getContext(), "Lesson deleted",
                                Toast.LENGTH_SHORT).show();
                        alert.dismiss();
                        updateList();
                    }
                });
            }
        });
        rv.setAdapter(adapter);
        returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });


        return v;
    }

    public void updateList() {
//        list.removeAllViewsInLayout(); // the most inefficient way to sort the views
        // if an entry is edited it will be deleted and placed back in

        String stringDate = new SimpleDateFormat("mmHHddMMyyyy").format(CalendarFragment.currentDate);
        monthMap = MonthMap.init(Integer.parseInt(stringDate.substring(6, 8)) + "-" +
                Integer.parseInt(stringDate.substring(8, 12)), MinuteUpdater.calendarMap);
        dayMap = DayMap.init(CalendarFragment.formatter.format(CalendarFragment.currentDate), monthMap);
        Set<Map.Entry<Integer, Lesson>> set = dayMap.entrySet();
//        RelativeLayout layout;
        lessons.clear();
        for (Map.Entry<Integer, Lesson> entry : set) {
            lessons.add(entry.getValue());
        }
        adapter.swap(lessons);
    }

    @Override
    public void onDestroyView() {
        if (dayMap.isEmpty()) {
            dayMap.delete();
        }
        if (CalendarFragment.thisFragment != null) {
            // recolor here
            Log.v("Calendar", "recoloring");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(CalendarFragment.currentDate);
            CalendarFragment.thisFragment.caldroidFragment.getCaldroidListener().onChangeMonth(
                    CalendarFragment.thisFragment.caldroidFragment.getMonth()
                    , CalendarFragment.thisFragment.caldroidFragment.getYear()
            );
//            CalendarFragment.thisFragment.recolorDay(CalendarFragment.currentDate);
        }
        returnBtn.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.view));
        returnBtn.setOnClickListener(originalListener);
        super.onDestroyView();
    }

    public static LessonListFragment newInstance(ImageButton imgBtn, View.OnClickListener listener){
        LessonListFragment f = new LessonListFragment();
        f.returnBtn = imgBtn;
        f.originalListener = listener;
        // do for loop here and extract all the data necessary from daymap here

        // just going to refer to static reference

//        b.putStringArray("Lesson names", new String[]{"Lesson 1", "Lesson 2", "Lesson 3", "Lesson 4", "Lesson 5"});
//        f.setArguments(b);
        return f;
    }
}
