package orbital.dingletutors;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.lesson_history, container, false);
        rv = (RecyclerView) v.findViewById(R.id.lesson_history_rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        // populate the arraylist for now we use test lessons
        Lesson testLesson1 = Lesson.init(Calendar.getInstance().getTime());
        testLesson1.students.add(new Student("Cindy", "Mother", "12345678"));
        testLesson1.students.add(new Student("Damien", "Father", "91827364"));
        lessons = new ArrayList<>();
        lessons.add(testLesson1);
        lessons.add(testLesson1);
        lessons.add(testLesson1);
        lessons.add(testLesson1);
        lessons.add(testLesson1);
        lessons.add(testLesson1);
        lessons.add(testLesson1);
        lessons.add(testLesson1);
        lessons.add(testLesson1);

        LessonListRV lessonListAdapter = new LessonListRV(true, R.layout.view_lesson_rv, lessons,
                new LessonListRV.OnItemClickListener() {
                    @Override
                    public void onItemClick(Lesson lesson) {
                    }
                },
                new LessonListRV.OnItemClickListener() {
                    @Override
                    public void onItemClick(Lesson lesson) {
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