package com.lonebytesoft.thetaleclient.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.R;

/**
 * @author Hamster
 * @since 11.10.2014
 */
public class RequestActionView extends FrameLayout {

    private final TextView textAction;
    private final ProgressBar progress;
    private final TextView textError;

    private final boolean isAutoSwitchMode;

    public RequestActionView(Context context) {
        this(context, null);
    }

    public RequestActionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RequestActionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.widget_request_action_view, this, true);

        textAction = (TextView) findViewById(R.id.widget_request_action_view_text);
        progress = (ProgressBar) findViewById(R.id.widget_request_action_view_progress);
        textError = (TextView) findViewById(R.id.widget_request_action_view_error);

        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RequestActionView, defStyle, 0);
        setActionText(typedArray.getString(R.styleable.RequestActionView_action_text));
        setErrorText(typedArray.getString(R.styleable.RequestActionView_error_text));
        setMode(Mode.values()[typedArray.getInt(R.styleable.RequestActionView_mode, 0)]);
        isAutoSwitchMode = typedArray.getBoolean(R.styleable.RequestActionView_auto_switch_mode, true);
        typedArray.recycle();
    }

    public void setActionText(final CharSequence text) {
        textAction.setText(text);
        if(isAutoSwitchMode) {
            setMode(Mode.ACTION);
        }
    }

    public void setErrorText(final CharSequence text) {
        textError.setText(text);
        if(isAutoSwitchMode) {
            setMode(Mode.ERROR);
        }
    }

    public void setMode(final Mode mode) {
        textAction.setVisibility(mode == Mode.ACTION ? VISIBLE : GONE);
        progress.setVisibility(mode == Mode.LOADING ? VISIBLE : GONE);
        textError.setVisibility(mode == Mode.ERROR ? VISIBLE : GONE);
    }

    @Override
    public void setEnabled(boolean enabled) {
        textAction.setEnabled(enabled);
    }

    public void setActionClickListener(final Runnable listener) {
        textAction.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAutoSwitchMode) {
                    setMode(Mode.LOADING);
                }
                listener.run();
            }
        });
    }

    public enum Mode {
        ACTION,
        LOADING,
        ERROR,
    }

}
