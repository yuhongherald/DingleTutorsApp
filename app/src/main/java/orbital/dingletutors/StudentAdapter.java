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
            holder.clientNameAndContact = (TextView) row.findViewById(R.id.client_and_contact);
            row.setTag(holder);

        } else {
            holder = (StudentHolder) row.getTag();
        }
        Student student = students.get(position);
        holder.studentName.setText("Student: " +student.studentName);
        holder.clientNameAndContact.setText("Contact: " + student.clientName + ", " + student.clientNo);
        return row;
    }

    static class StudentHolder {
        TextView studentName;
        TextView clientNameAndContact;
    }

}
