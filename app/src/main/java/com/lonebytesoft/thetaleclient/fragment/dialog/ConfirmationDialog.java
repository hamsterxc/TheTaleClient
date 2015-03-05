package com.lonebytesoft.thetaleclient.fragment.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.R;

/**
 * @author Hamster
 * @since 11.12.2014
 */
public class ConfirmationDialog extends BaseDialog {

    private static final String PARAM_TITLE = "PARAM_TITLE";
    private static final String PARAM_MESSAGE = "PARAM_MESSAGE";

    private Runnable onOkListener;
    private Runnable onCancelListener;
    private Runnable onDismissListener;

    private CharSequence okCaption;
    private CharSequence cancelCaption;

    private boolean isOkClicked;
    private boolean isCancelClicked;

    public static ConfirmationDialog newInstance(final String title, final CharSequence message) {
        final ConfirmationDialog dialog = new ConfirmationDialog();

        Bundle args = new Bundle();
        args.putString(PARAM_TITLE, title);
        args.putCharSequence(PARAM_MESSAGE, message);

        dialog.setArguments(args);
        return dialog;
    }

    public void setupPositiveButton(final CharSequence caption, final Runnable listener) {
        okCaption = caption;
        onOkListener = listener;
    }

    public void setupNegativeButton(final CharSequence caption, final Runnable listener) {
        cancelCaption = caption;
        onCancelListener = listener;
    }

    public void setOnDismissListener(final Runnable listener) {
        this.onDismissListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_content_confirmation, container, false);

        isOkClicked = false;

        ((TextView) view.findViewById(R.id.dialog_confirmation_message)).setText(getArguments().getCharSequence(PARAM_MESSAGE));

        final Button okButton = (Button) view.findViewById(R.id.dialog_confirmation_ok);
        if(okCaption != null) {
            okButton.setText(okCaption);
        }
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOkClicked = true;
                if(onOkListener != null) {
                    onOkListener.run();
                }
                dismiss();
            }
        });

        final Button cancelButton = (Button) view.findViewById(R.id.dialog_confirmation_cancel);
        if(cancelCaption != null){
            cancelButton.setText(cancelCaption);
        }
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCancelClicked = true;
                if(onCancelListener != null) {
                    onCancelListener.run();
                }
                dismiss();
            }
        });

        return wrapView(inflater, view, getArguments().getString(PARAM_TITLE));
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(!isOkClicked && !isCancelClicked && (onDismissListener != null)) {
            onDismissListener.run();
        }
    }

}
