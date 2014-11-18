package com.lonebytesoft.thetaleclient.fragment.dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * @author Hamster
 * @since 16.11.2014
 */
public class TimePickDialog extends BaseDialog implements TimePickerDialog.OnTimeSetListener {

    private TimeSelectionListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(listener != null) {
            listener.onTimeSelected(hourOfDay, minute);
        }
        dismiss();
    }

    public void setTimeSelectionListener(TimeSelectionListener listener) {
        this.listener = listener;
    }

    public interface TimeSelectionListener {
        void onTimeSelected(int hourOfDay, int minute);
    }

}
