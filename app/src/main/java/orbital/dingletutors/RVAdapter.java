package orbital.dingletutors;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Muruges on 21/6/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.StudentHolder> {

    List<Student> students;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Student student);
    }

    @Override
    public StudentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.choose_students, parent, false);
        StudentHolder sh = new StudentHolder(v);
        return sh;
    }

    @Override
    public void onBindViewHolder(StudentHolder holder, int position) {
        holder.bind(students.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public static class StudentHolder extends RecyclerView.ViewHolder {
        TextView studentName;
        TextView clientName;
        TextView phoneNum;
        public StudentHolder(View itemView) {
            super(itemView);
            studentName = (TextView) itemView.findViewById(R.id.choose_student_studentName);
            clientName = (TextView) itemView.findViewById(R.id.choose_student_clientName);
            phoneNum = (TextView) itemView.findViewById(R.id.choose_student_phoneNumber);
        }
        public void bind(final Student student, final OnItemClickListener listener){
            studentName.setText(student.studentName);
            clientName.setText(student.clientName);
            phoneNum.setText(student.clientNo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(student);
                }
            });
        }
    }

    public RVAdapter(List<Student> students, OnItemClickListener listener){
        this.students = students;
        this.listener = listener;
    }
}
