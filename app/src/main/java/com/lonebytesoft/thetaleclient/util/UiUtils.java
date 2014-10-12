package com.lonebytesoft.thetaleclient.util;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

/**
 * @author Hamster
 * @since 07.10.2014
 */
public class UiUtils {

    public static void setText(final TextView textView, final CharSequence text) {
        if(textView == null) {
            return;
        }

        if(TextUtils.isEmpty(text)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
    }

    public static void setText(final View view, final CharSequence text) {
        if(view instanceof TextView) {
            setText((TextView) view, text);
        }
    }

    public static Intent getOpenLinkIntent(final String url) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        return intent;
    }

}
