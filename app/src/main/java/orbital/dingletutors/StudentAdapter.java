package orbital.dingletutors;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Muruges on 2/7/2017.
 */

public class StudentAdapter extends ArrayAdapter<Student> {

    Context context;
    int layoutResourceId;
    ArrayList<Student> students;

    public StudentAdapter(Context context, int layoutResourceId, ArrayList<Student> data){
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.students = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        StudentHolder holder;
        if (row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new StudentHolder();
            holder.studentName = (TextView) row.findViewById(R.id.name);
            holder.clientName = (TextView) row.findViewById(R.id.client);
            holder.phoneNum = (TextView) row.findViewById(R.id.number);
            row.setTag(holder);

        } else {
            holder = (StudentHolder) row.getTag();
        }
        Student student = students.get(position);
        holder.studentName.setText(student.studentName);
        holder.clientName.setText(student.clientName);
        holder.phoneNum.setText(student.clientNo);
        return row;
    }

    static class StudentHolder {
        TextView studentName;
        TextView clientName;
        TextView phoneNum;
    }

}
