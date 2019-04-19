package bonzai.com.biometrix;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class TimmerPicket extends DialogFragment  {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar=Calendar.getInstance();
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);


        return new TimePickerDialog(getActivity(),(TimePickerDialog.OnTimeSetListener) getParentFragment()
                ,hour,minute,android.text.format.DateFormat.is24HourFormat(getActivity()));
    }



}

