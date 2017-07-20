package orbital.dingletutors;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Muruges on 13/7/2017.
 */

public class LessonListRV extends RecyclerView.Adapter<LessonListRV.LessonHolder> {
    List<Lesson> lessons;
    OnItemClickListener itemCLickListener;
    OnItemClickListener onCloseListener;
    int resource;

    public interface OnItemClickListener {
        void onItemClick(Lesson lesson);
    }

    @Override
    public LessonHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        LessonHolder lh = new LessonHolder(v);
        return lh;
    }

    @Override
    public void onBindViewHolder(LessonHolder holder, int position) {
        holder.bind(lessons.get(position), itemCLickListener, onCloseListener);
    }

    public void swap(List<Lesson> lessons){
        this.lessons = lessons;
//        this.lessons.addAll(lessons);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    public LessonListRV(int resource, List<Lesson> lessons, OnItemClickListener itemCLickListener, OnItemClickListener onCloseListener){
        this.resource = resource;
        this.lessons = lessons;
        this.itemCLickListener = itemCLickListener;
        this.onCloseListener = onCloseListener;
    }

    public static class LessonHolder extends RecyclerView.ViewHolder{
        TextView className;
        TextView classLevel;
        TextView startTime;
        Button deleteLesson;

        public LessonHolder(View itemView) {
            super(itemView);
            className = (TextView) itemView.findViewById(R.id.lesson_list_className);
            classLevel = (TextView) itemView.findViewById(R.id.lesson_list_classLevel);
            startTime = (TextView) itemView.findViewById(R.id.lesson_list_startTime);
            deleteLesson = (Button) itemView.findViewById(R.id.delete_lesson);
        }
        public void bind(final Lesson lesson, final OnItemClickListener itemClickListener, final OnItemClickListener onCloseListener){
            if (lesson.recurringLesson != null) {
                className.setText("[R]" + lesson.name); // Just a primitive indication to save time
            } else {
                className.setText(lesson.name);
            }
            classLevel.setText(lesson.level);
            startTime.setText(lesson.displayTime);
            deleteLesson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCloseListener.onItemClick(lesson);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(lesson);
                }
            });
        }
    }
}
