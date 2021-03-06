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
import android.widget.Toast;

/**
 * Created by Muruges on 2/7/2017.
 */

public class ViewStudentFragment extends Fragment{
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.view_student_fragment, container, false);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        final RVAdapter adapter = new RVAdapter(MinuteUpdater.studentPresetMap.studentList, new RVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Student student) {
                // view and edit student data (like number and type of lessons taught, age, education level etc
                Toast.makeText(getContext(), "Student pressed", Toast.LENGTH_SHORT);

            }
        });
        rv.setAdapter(adapter);
        FloatingActionButton quickAddStudent = (FloatingActionButton) v.findViewById(R.id.quick_add_student);
        quickAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // we could open up some additional fields here like the stuff mentioned above which are not compulsory
                final View dialogLayout = inflater.inflate(R.layout.new_student, null);
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Add student");
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
                        adapter.notifyItemInserted(MinuteUpdater.studentPresetMap.studentMap.headMap(addedStudent).size());
                    }
                });

            }
        });
        return v;
    }

    public static ViewStudentFragment newInstance(){
        // do anything
        ViewStudentFragment f = new ViewStudentFragment();
        return f;
    }
}
