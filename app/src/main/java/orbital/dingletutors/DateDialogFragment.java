package orbital.dingletutors;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Muruges on 19/6/2017.
 */

public class DateDialogFragment extends DialogFragment {
    DatePickerDialog.OnDateSetListener ondate;
    private int year, month, day;

    public DateDialogFragment(){
    }
    public void setCallBack(DatePickerDialog.OnDateSetListener ondate){
        this.ondate = ondate;
    }

    @Override
    public void setArguments(Bundle args){
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return new DatePickerDialog(getActivity(), ondate, year, month, day);
    }
}
