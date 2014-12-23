package com.lonebytesoft.thetaleclient.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.activity.MainActivity;
import com.lonebytesoft.thetaleclient.fragment.GameFragment;
import com.lonebytesoft.thetaleclient.fragment.onscreen.OnscreenStateListener;

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

    public static void removeGlobalLayoutListener(final View view, final ViewTreeObserver.OnGlobalLayoutListener listener) {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        } else {
            view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
        }
    }

    public static void hideKeyboard(final Activity activity) {
        final View view = activity.getCurrentFocus();
        if(view != null) {
            final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void setHeight(final View view, final int height) {
        if(view == null) {
            return;
        }

        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    public static Spanned getInfoItem(final CharSequence caption, final CharSequence info) {
        final Spannable captionSpanned = new SpannableString(caption);
        captionSpanned.setSpan(new StyleSpan(Typeface.BOLD), 0, captionSpanned.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return (Spanned) TextUtils.concat(captionSpanned, ": ", info);
    }

    public static void callOnscreenStateChange(final Fragment fragment, final boolean isOnscreen) {
        if(fragment instanceof OnscreenStateListener) {
            if(isOnscreen) {
                ((OnscreenStateListener) fragment).onOnscreen();
            } else {
                ((OnscreenStateListener) fragment).onOffscreen();
            }
        }
    }

    public static PendingIntent getMainActivityIntent(final Context context, final GameFragment.GamePage gamePage) {
        final Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(MainActivity.KEY_GAME_TAB_INDEX, gamePage.ordinal());
        return PendingIntent.getActivity(
                context,
                (int) System.currentTimeMillis(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
