package com.lonebytesoft.thetaleclient.fragment.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.R;

/**
 * @author Hamster
 * @since 28.10.2014
 */
public class MessageDialog extends BaseDialog {

    private static final String PARAM_TITLE = "PARAM_TITLE";
    private static final String PARAM_MESSAGE = "PARAM_MESSAGE";

    private Runnable onOkListener = null;
    private Runnable onDismissListener = null;

    private boolean isOkClicked;

    public static MessageDialog newInstance(final String title, final CharSequence message) {
        final MessageDialog dialog = new MessageDialog();

        Bundle args = new Bundle();
        args.putString(PARAM_TITLE, title);
        args.putCharSequence(PARAM_MESSAGE, message);

        dialog.setArguments(args);
        return dialog;
    }

    public void setOnOkClickListener(final Runnable listener) {
        this.onOkListener = listener;
    }

    public void setOnDismissListener(final Runnable listener) {
        this.onDismissListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_content_message, container, false);

        isOkClicked = false;

        ((TextView) view.findViewById(R.id.dialog_message_message)).setText(getArguments().getCharSequence(PARAM_MESSAGE));
        view.findViewById(R.id.dialog_message_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOkClicked = true;
                if(onOkListener != null) {
                    onOkListener.run();
                }
                dismiss();
            }
        });

        return wrapView(inflater, view, getArguments().getString(PARAM_TITLE));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(!isOkClicked && (onDismissListener != null)) {
            onDismissListener.run();
        }
    }

}
