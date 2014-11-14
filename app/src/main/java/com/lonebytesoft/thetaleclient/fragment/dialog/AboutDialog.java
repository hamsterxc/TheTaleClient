package com.lonebytesoft.thetaleclient.fragment.dialog;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.BuildConfig;
import com.lonebytesoft.thetaleclient.R;

/**
 * @author Hamster
 * @since 13.11.2014
 */
public class AboutDialog extends BaseDialog {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_content_about, container, false);

        final TextView text = (TextView) view.findViewById(R.id.dialog_about_text);
        text.setText(Html.fromHtml(getString(R.string.app_about, BuildConfig.VERSION_NAME)));
        text.setMovementMethod(LinkMovementMethod.getInstance());

        view.findViewById(R.id.dialog_about_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return wrapView(inflater, view, getString(R.string.app_name));
    }

}
