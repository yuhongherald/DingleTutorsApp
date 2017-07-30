package orbital.dingletutors;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Muruges on 30/7/2017.
 */

public class LessonRateRV extends RecyclerView.Adapter<LessonRateRV.LessonRateHolder>{
    List<LessonRateMap.LessonRate> lessonRates;
    private OnItemClickListener itemClickListener;
    private OnItemClickListener onCloseListener;

    public LessonRateRV(List<LessonRateMap.LessonRate> lessonRates, OnItemClickListener itemClickListener, OnItemClickListener onCloseListener){
        this.lessonRates = lessonRates;
        this.onCloseListener = onCloseListener;
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(LessonRateMap.LessonRate lessonRate);
    }

    @Override
    public LessonRateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_lesson_rate_rv, parent, false);
        LessonRateHolder holder = new LessonRateHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(LessonRateRV.LessonRateHolder holder, int position) {
        holder.bind(lessonRates.get(position), itemClickListener, onCloseListener);
    }

    @Override
    public int getItemCount() {
        return lessonRates.size();
    }

    public static class LessonRateHolder extends RecyclerView.ViewHolder {
        TextView classLevel;
        TextView subject;
        TextView ratePerHour;
        Button deleteRate;

        public LessonRateHolder(View itemView) {
            super(itemView);
            classLevel = (TextView) itemView.findViewById(R.id.classlevel);
            subject = (TextView) itemView.findViewById(R.id.subject);
            ratePerHour = (TextView) itemView.findViewById(R.id.rate);
            deleteRate = (Button) itemView.findViewById(R.id.close);

        }
        public void bind (final LessonRateMap.LessonRate lessonRate, final OnItemClickListener itemClickListener, final OnItemClickListener onCloseListener){
            classLevel.setText(lessonRate.classLevel);
            subject.setText(lessonRate.subjectName);
            ratePerHour.setText(lessonRate.displayFees);
            deleteRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCloseListener.onItemClick(lessonRate);
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onItemClick(lessonRate);
                }
            });
        }
    }
}
