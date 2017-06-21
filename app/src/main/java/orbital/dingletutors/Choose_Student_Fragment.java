package orbital.dingletutors;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Muruges on 21/6/2017.
 */

public class Choose_Student_Fragment extends Fragment{

    static NewLessonFragment newLessonInstance;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.recycler_view, container, false);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        RVAdapter adapter = new RVAdapter(createTestStudents(), new RVAdapter.OnItemClickListener(){

            @Override
            public void onItemClick(Student student) {
                selectNewStudent(student);
                newLessonInstance.updateStudents();
                getActivity().onBackPressed();
            }
        });
        rv.setAdapter(adapter);



        return v;
    }

    public ArrayList<Student> createTestStudents(){
        ArrayList<Student> students = new ArrayList<>(2);
        students.add(new Student("Mary", "Mary's mother", "98765432"));
        students.add(new Student("Bob", "Bob's father", "12345678"));
        return students;
    }



    private void selectNewStudent(Student student){
        newLessonInstance.selectedStudents.add(student);
    }
    public static Choose_Student_Fragment newInstance(NewLessonFragment newLessonInstance) {

        Choose_Student_Fragment fragment = new Choose_Student_Fragment();
        Choose_Student_Fragment.newLessonInstance = newLessonInstance;
        return fragment;
    }
}
