package com.lonebytesoft.thetaleclient.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.R;

/**
 * @author Hamster
 * @since 09.10.2014
 */
public abstract class BaseDialog extends DialogFragment {

    protected View wrapView(final LayoutInflater layoutInflater, final View view, final String caption) {
        final View wrapped = layoutInflater.inflate(R.layout.dialog_base, null);

        ((TextView) wrapped.findViewById(R.id.dialog_caption)).setText(caption);
        wrapped.findViewById(R.id.dialog_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ((FrameLayout) wrapped.findViewById(R.id.dialog_content)).addView(view);

        return wrapped;
    }

    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(true);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onPause() {
        dismiss();
        super.onPause();
    }

}
