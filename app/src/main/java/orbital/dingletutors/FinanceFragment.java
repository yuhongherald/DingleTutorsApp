package orbital.dingletutors;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

/**
 * Created by Muruges on 28/7/2017.
 */

public class FinanceFragment extends Fragment {
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.finance_fragment, container, false);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        // Testing purpose
        ArrayList<LessonRateMap.LessonRate> lessonRates = LessonRateMap.lessonRates;
        lessonRates.add(new LessonRateMap.LessonRate("Primary 1", " English", 20));
        lessonRates.add(new LessonRateMap.LessonRate("Primary 2", " English", 80));
        lessonRates.add(new LessonRateMap.LessonRate("Primary 3", " English", 20));

        final ArrayAdapter<CharSequence> classLevelAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item,
                NewLessonFragment.educationLevels);
        final ArrayAdapter<CharSequence> subjectNameAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item,
                NewLessonFragment.subjectNames);
        classLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        LessonRateRV lessonRateAdapter = new LessonRateRV(lessonRates,
                new LessonRateRV.OnItemClickListener() {
                    @Override
                    public void onItemClick(LessonRateMap.LessonRate lessonRate) {
                        View dialogLayout = inflater.inflate(R.layout.new_fees, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Add fees");
                        builder.setView(dialogLayout);

                        Spinner classLevel = (Spinner) dialogLayout.findViewById(R.id.class_level_spinner);
                        Spinner subjectName = (Spinner) dialogLayout.findViewById(R.id.subject_name_spinner);

                        classLevel.setAdapter(classLevelAdapter);
                        subjectName.setAdapter(subjectNameAdapter);

                        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setPositiveButton("save", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        });
                        final AlertDialog alert = builder.create();
                        alert.show();
                        alert.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });

                    }
                },
                new LessonRateRV.OnItemClickListener() {
                    @Override
                    public void onItemClick(LessonRateMap.LessonRate lessonRate) {

                    }
                });
        rv.setAdapter(lessonRateAdapter);
        return v;

    }

    public static FinanceFragment newInstance() {
        FinanceFragment f = new FinanceFragment();
        return f;
    }
}
