package orbital.dingletutors;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Muruges on 28/7/2017.
 */

public class FinanceFragment extends Fragment {
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.finance_fragment, container, false);
        RecyclerView rv = (RecyclerView) v.findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        final ArrayList<LessonRateMap.LessonRate> lessonRates = LessonRateMap.lessonRates;
        final TreeMap lessonRateMap = LessonRateMap.lessonRatesMap;
        final ArrayAdapter<CharSequence> classLevelAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item,
                NewLessonFragment.educationLevels);
        final ArrayAdapter<CharSequence> subjectNameAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item,
                NewLessonFragment.subjectNames);
        classLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subjectNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final LessonRateRV lessonRateAdapter = new LessonRateRV(lessonRates,null, null);
        v.findViewById(R.id.add_fee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogLayout = inflater.inflate(R.layout.new_fees, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("New fees");
                builder.setView(dialogLayout);

                final Spinner classLevel = (Spinner) dialogLayout.findViewById(R.id.class_level_spinner);
                final Spinner subjectName = (Spinner) dialogLayout.findViewById(R.id.subject_name_spinner);

                classLevel.setAdapter(classLevelAdapter);
                subjectName.setAdapter(subjectNameAdapter);

                final TextInputLayout feeWrapper = (TextInputLayout) dialogLayout.findViewById(R.id.feeWrapper);
                final TextInputEditText fee = (TextInputEditText) dialogLayout.findViewById(R.id.fee);

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
                        String classLevelText = classLevel.getSelectedItem().toString();
                        String subjectNameText = subjectName.getSelectedItem().toString();
                        if (fee.getText().toString().trim().length() == 0) {
                            feeWrapper.setError("This field cannot be empty");
                        } else if (LessonRateMap.isPresent(classLevelText, subjectNameText)) {
                            feeWrapper.setError(null);
                            Toast.makeText(getContext(), "Another fee for the same subject name and class level has already been created.",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            LessonRateMap.LessonRate newRate = new LessonRateMap.LessonRate(classLevelText,
                                    subjectNameText, Double.parseDouble(fee.getText().toString()));
                            int newPos = LessonRateMap.getPosInMap(newRate);
                            lessonRates.add(newPos, newRate);
                            lessonRateAdapter.notifyItemInserted(newPos);
                            alert.dismiss();
                        }
                    }
                });
            }
        });

        lessonRateAdapter.setOnClickListeners(
                new LessonRateRV.OnItemClickListener() {
                    @Override
                    public void onItemClick(final LessonRateMap.LessonRate lessonRate) {
                        View dialogLayout = inflater.inflate(R.layout.new_fees, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Change fees");
                        builder.setView(dialogLayout);

                        final Spinner classLevel = (Spinner) dialogLayout.findViewById(R.id.class_level_spinner);
                        final Spinner subjectName = (Spinner) dialogLayout.findViewById(R.id.subject_name_spinner);

                        classLevel.setAdapter(classLevelAdapter);
                        subjectName.setAdapter(subjectNameAdapter);

                        final TextInputLayout feeWrapper = (TextInputLayout) dialogLayout.findViewById(R.id.feeWrapper);
                        final TextInputEditText fee = (TextInputEditText) dialogLayout.findViewById(R.id.fee);

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
                                String classLevelText = classLevel.getSelectedItem().toString();
                                String subjectNameText = subjectName.getSelectedItem().toString();
                                if (fee.getText().toString().trim().length() == 0) {
                                    feeWrapper.setError("This field cannot be empty");
                                } else if (LessonRateMap.isPresent(classLevelText, subjectNameText) &&
                                        !LessonRateMap.isSameRate(lessonRate, classLevelText, subjectNameText)){
                                    feeWrapper.setError(null);
                                    Toast.makeText(getContext(), "Another fee for the same subject name and class level has already been created.",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    int currPos = LessonRateMap.getPosInMap(lessonRate);
                                    lessonRates.remove(currPos);
                                    lessonRateMap.remove(LessonRateMap.getKey(lessonRate));
                                    lessonRateAdapter.notifyItemRemoved(currPos);
                                    LessonRateMap.LessonRate newRate = new LessonRateMap.LessonRate(classLevelText,
                                            subjectNameText, Double.parseDouble(fee.getText().toString()));
                                    int newPos = LessonRateMap.getPosInMap(newRate);
                                    lessonRates.add(newPos, newRate);
                                    lessonRateAdapter.notifyItemInserted(newPos);
                                    alert.dismiss();
                                }
                            }
                        });

                    }
                },
                new LessonRateRV.OnItemClickListener() {
                    @Override
                    public void onItemClick(final LessonRateMap.LessonRate lessonRate) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setMessage("Delete fees for this lesson type?");
                        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Don't do anything here
                            }
                        });
                        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Don't do anything here
                            }
                        });
                        final AlertDialog alert = builder.create();
                        alert.show();
                        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                            }
                        });
                        alert.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                int currPos = LessonRateMap.getPosInMap(lessonRate);
                                lessonRates.remove(currPos);
                                lessonRateMap.remove(LessonRateMap.getKey(lessonRate));
                                lessonRateAdapter.notifyItemRemoved(currPos);
                                alert.dismiss();
                            }
                        });
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
