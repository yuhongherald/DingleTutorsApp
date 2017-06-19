package orbital.dingletutors;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by Muruges on 19/6/2017.
 */

public class TimeDialogFragment extends DialogFragment{
    TimePickerDialog.OnTimeSetListener ontime;

    private int hours, minutes;
    public TimeDialogFragment(){

    }

    public void setCallBack(TimePickerDialog.OnTimeSetListener ontime) {
        this.ontime = ontime;
    }
    @Override
    public void setArguments(Bundle args){
        super.setArguments(args);
        hours = args.getInt("hours");
        minutes = args.getInt("minutes");
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        return new TimePickerDialog(getActivity(), ontime, hours, minutes, false);
    }
}
