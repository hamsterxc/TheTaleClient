package com.lonebytesoft.thetaleclient.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.preference.Preference;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.fragment.dialog.TimePickDialog;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * @author Hamster
 * @since 16.11.2014
 */
public class TimeIntervalPreference extends Preference {

    private final Context context;
    private FragmentActivity activity;

    private CheckBox enabled;
    private boolean isEnabled;

    private TextView textTitle;
    private CharSequence title;

    private View container;
    private TextView textFrom;
    private TextView textTo;

    private final String keyEnabled;
    private final String keyFromHour;
    private final String keyFromMinute;
    private final String keyToHour;
    private final String keyToMinute;

    public TimeIntervalPreference(Context context) {
        this(context, null);
    }

    public TimeIntervalPreference(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeIntervalPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PreferenceTimeInterval, defStyleAttr, 0);
        keyEnabled = typedArray.getString(R.styleable.PreferenceTimeInterval_key_enabled);
        keyFromHour = typedArray.getString(R.styleable.PreferenceTimeInterval_key_from_hour);
        keyFromMinute = typedArray.getString(R.styleable.PreferenceTimeInterval_key_from_minute);
        keyToHour = typedArray.getString(R.styleable.PreferenceTimeInterval_key_to_hour);
        keyToMinute = typedArray.getString(R.styleable.PreferenceTimeInterval_key_to_minute);
        typedArray.recycle();
    }

    public void setActivity(final FragmentActivity activity) {
        this.activity = activity;
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        this.title = title;
        if(textTitle != null) {
            textTitle.setText(title);
        }
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return layoutInflater.inflate(R.layout.widget_preference_time_interval, parent, false);
    }

    @Override
    protected void onBindView(View view) {
        textTitle = (TextView) view.findViewById(R.id.preference_time_interval_title);
        textTitle.setText(title);

        container = view.findViewById(R.id.preference_time_interval_container);
        enabled = (CheckBox) view.findViewById(R.id.preference_time_interval_enabled);
        enabled.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setKey(keyEnabled);
                persistBoolean(isChecked);
                container.setEnabled(isChecked);
                isEnabled = isChecked;
            }
        });
        enabled.setChecked(PreferencesManager.isNotificationNighttimeEnabled());
        isEnabled = enabled.isChecked();
        container.setEnabled(isEnabled);

        view.findViewById(R.id.preference_time_interval_container_main).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enabled.performClick();
            }
        });

        textFrom = (TextView) view.findViewById(R.id.preference_time_interval_from);
        textFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEnabled) {
                    enabled.setChecked(true);
                }

                final TimePickDialog dialog = new TimePickDialog();
                dialog.setTimeSelectionListener(new TimePickDialog.TimeSelectionListener() {
                    @Override
                    public void onTimeSelected(int hourOfDay, int minute) {
                        setKey(keyFromHour);
                        persistInt(hourOfDay);
                        setKey(keyFromMinute);
                        persistInt(minute);
                        textFrom.setText(getTimeText(hourOfDay, minute));
                    }
                });
                dialog.show(activity.getSupportFragmentManager(), "TAG_TIME_PICKER");
            }
        });
        textFrom.setText(getTimeText(
                PreferencesManager.getNotificationNighttimeFromHour(),
                PreferencesManager.getNotificationNighttimeFromMinute()));

        textTo = (TextView) view.findViewById(R.id.preference_time_interval_to);
        textTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEnabled) {
                    enabled.setChecked(true);
                }

                final TimePickDialog dialog = new TimePickDialog();
                dialog.setTimeSelectionListener(new TimePickDialog.TimeSelectionListener() {
                    @Override
                    public void onTimeSelected(int hourOfDay, int minute) {
                        setKey(keyToHour);
                        persistInt(hourOfDay);
                        setKey(keyToMinute);
                        persistInt(minute);
                        textTo.setText(getTimeText(hourOfDay, minute));
                    }
                });
                dialog.show(activity.getSupportFragmentManager(), "TAG_TIME_PICKER");
            }
        });
        textTo.setText(getTimeText(
                PreferencesManager.getNotificationNighttimeToHour(),
                PreferencesManager.getNotificationNighttimeToMinute()));
    }

    private static String getTimeText(final int hourOfDay, final int minute) {
        final DateFormat timeFormat = android.text.format.DateFormat.getTimeFormat(TheTaleClientApplication.getContext());
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        return timeFormat.format(calendar.getTime());
    }

}
