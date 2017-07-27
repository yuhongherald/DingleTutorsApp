package orbital.dingletutors;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Created by Muruges on 21/6/2017.
 */

public class Choose_Student_Fragment extends Fragment{

    static NewLessonFragment newLessonInstance;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // recyclerview is successor of listview with apparently better performance
        // it will update the list of students avaialble if we choose to quick add a students
        // the actual list of students are stored in a static treemap and arraylist of the student class
        // the list of students selected is stored in the  Newlessonfragment newlessoninstance.selectedstudents
        //
        View v = inflater.inflate(R.layout.recycler_view, container, false);
        RecyclerView rvAvailable = (RecyclerView) v.findViewById(R.id.available_students);
        RecyclerView rvChosen = (RecyclerView) v.findViewById(R.id.chosen_students);
        final ArrayList<Student> availableStudents = new ArrayList<>(MinuteUpdater.studentPresetMap.studentList);
        final ArrayList<Student> chosenStudents = new ArrayList<>();

        if (newLessonInstance.selectedStudents.size() != 0) {
            for (Student student: newLessonInstance.selectedStudents){
                chosenStudents.add(student);
                availableStudents.remove(student);
            }
        }
        final TreeSet<Student> availableStudentsSorted = new TreeSet<>(availableStudents);
        final TreeSet<Student> chosenStudentsSorted = new TreeSet<>(chosenStudents);


        LinearLayoutManager llm1 = new LinearLayoutManager(getContext());
        rvAvailable.setLayoutManager(llm1);
        LinearLayoutManager llm2 = new LinearLayoutManager(getContext());
        rvChosen.setLayoutManager(llm2);
        final RVAdapter adapterChosen = new RVAdapter(chosenStudents, null); // we declare first so that can be used in adapteravilable
        final RVAdapter adapterAvailable = new RVAdapter(availableStudents, null);

        // Setting of onclick listeners
        adapterAvailable.setListener(new RVAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(Student student) {
                // add to chosen students in alphebatical order of students
                chosenStudentsSorted.add(student);
                int posAdded = chosenStudentsSorted.headSet(student).size();
                chosenStudents.add(posAdded,student);
                adapterChosen.notifyItemInserted(posAdded);
                // remove from avilable students
                int posRemoved = availableStudentsSorted.headSet(student).size();
                availableStudentsSorted.remove(student);
                availableStudents.remove(posRemoved);
                adapterAvailable.notifyItemRemoved(posRemoved);

            }
        });
        rvAvailable.setAdapter(adapterAvailable);

        adapterChosen.setListener(new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Student student) {
                availableStudentsSorted.add(student);
                int posAdded = availableStudentsSorted.headSet(student).size();
                availableStudents.add(posAdded,student);
                adapterAvailable.notifyItemInserted(posAdded);
                // remove from avilable students
                int posRemoved = chosenStudentsSorted.headSet(student).size();
                chosenStudentsSorted.remove(student);
                chosenStudents.remove(posRemoved);
                adapterChosen.notifyItemRemoved(posRemoved);

            }
        });
        rvChosen.setAdapter(adapterChosen);

        v.findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        v.findViewById(R.id.save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectNewStudents(chosenStudents);
                newLessonInstance.updateStudents();
                getActivity().onBackPressed();
            }
        });

        FloatingActionButton quickAddStudent = (FloatingActionButton) v.findViewById(R.id.quick_add_student);
        quickAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogLayout = inflater.inflate(R.layout.new_student, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Quick add student");
                builder.setView(dialogLayout);
                final TextInputEditText name = (TextInputEditText)dialogLayout.findViewById(R.id.name);
                final TextInputEditText client = (TextInputEditText)dialogLayout.findViewById(R.id.client);
                final TextInputEditText number = (TextInputEditText)dialogLayout.findViewById(R.id.number);

                final TextInputLayout nameWrapper = ((TextInputLayout)dialogLayout.findViewById(R.id.nameWrapper));
                final TextInputLayout clientWrapper = ((TextInputLayout)dialogLayout.findViewById(R.id.clientWrapper));
                final TextInputLayout numberWrapper = ((TextInputLayout)dialogLayout.findViewById(R.id.numberWrapper));

                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing here is work around to highlight empty fields
                    }
                });
//                builder.show();
                final AlertDialog alert = builder.create();
                alert.show();
                alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // need to add checks to ensure fields not empty
                        boolean errorExists = false;

                        if (name.getText().toString().trim().length() == 0){
                            nameWrapper.setError("This field cannot be empty");
                            errorExists = true;
                        } else {
                            nameWrapper.setError(null);
                        }
                        if (client.getText().toString().trim().length() == 0){
                            clientWrapper.setError("This field cannot be empty");
                            errorExists = true;
                        } else {
                            clientWrapper.setError(null);
                        }
                        if (number.getText().toString().trim().length() == 0){
                            numberWrapper.setError("This field cannot be empty");
                            errorExists = true;
                        } else {
                            numberWrapper.setError(null);
                        }
                        if (errorExists) return;

                        Student addedStudent = new Student(
                                name.getText().toString(),
                                client.getText().toString(),
                                number.getText().toString()
                        );
                        alert.dismiss();

                        availableStudentsSorted.add(addedStudent);
                        int pos = availableStudentsSorted.headSet(addedStudent).size();
                        availableStudents.add(pos, addedStudent);
                        adapterAvailable.notifyItemInserted(pos);
//                        adapterAvailable.notifyItemInserted(MinuteUpdater.studentPresetMap.studentMap.head(addedStudent).size());
                    }
                });
            }
        });

        return v;
    }


    private void selectNewStudents(ArrayList<Student> students){
        newLessonInstance.selectedStudents = students;
    }
    public static Choose_Student_Fragment newInstance(NewLessonFragment newLessonInstance) {

        Choose_Student_Fragment fragment = new Choose_Student_Fragment();
        Choose_Student_Fragment.newLessonInstance = newLessonInstance;
        return fragment;
    }
}
