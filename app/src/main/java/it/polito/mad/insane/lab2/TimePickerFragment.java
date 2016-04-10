package it.polito.mad.insane.lab2;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Renato on 10/04/2016.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


        String picker = this.getTag();
        switch (picker){
            case "openingPicker":
                Button button = (Button) getActivity().findViewById(R.id.openingHour);
                button.setText(String.format("%d:%d", hourOfDay, minute));
                break;
            case "closingPicker":
                button = (Button) getActivity().findViewById(R.id.closingHour);
                button.setText(String.format("%d:%d", hourOfDay, minute));
                break;
            default:
                break;
        }
    }
}
