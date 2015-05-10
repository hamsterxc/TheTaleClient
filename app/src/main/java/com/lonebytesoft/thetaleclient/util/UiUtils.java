package com.lonebytesoft.thetaleclient.util;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.DrawerItem;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.activity.LoginActivity;
import com.lonebytesoft.thetaleclient.activity.MainActivity;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.cache.prerequisite.InfoPrerequisiteRequest;
import com.lonebytesoft.thetaleclient.api.cache.prerequisite.PrerequisiteRequest;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.api.response.InfoResponse;
import com.lonebytesoft.thetaleclient.fragment.GameFragment;
import com.lonebytesoft.thetaleclient.fragment.Refreshable;
import com.lonebytesoft.thetaleclient.util.onscreen.OnscreenStateListener;

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

    public static void showKeyboard(final Activity activity) {
        final View view = activity.getCurrentFocus();
        if(view != null) {
            final InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
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

    public static PendingIntent getApplicationIntent(final Context context, final GameFragment.GamePage gamePage,
                                                     final boolean shouldResetWatchingAccount) {
        final Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        if(gamePage != null) {
            intent.putExtra(MainActivity.KEY_GAME_TAB_INDEX, gamePage.ordinal());
        }
        intent.putExtra(MainActivity.KEY_SHOULD_RESET_WATCHING_ACCOUNT, shouldResetWatchingAccount);

        return PendingIntent.getActivity(
                context,
                (int) System.currentTimeMillis(),
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT);
    }

    // TODO plurals in "values" folder are treated as English, not Russian
    public static String getQuantityString(final Context context,
                                     final int oneResId, final int fewResId, final int manyResId,
                                     final int quantity) {
        if((quantity % 100 >= 11) && (quantity % 100 <= 14) || (quantity % 10 == 0) || (quantity % 10 >= 5)) {
            return context.getString(manyResId, quantity);
        } else if(quantity % 10 == 1) {
            return context.getString(oneResId, quantity);
        } else {
            return context.getString(fewResId, quantity);
        }
    }

    public static void setupFindPlayerContainer(final View container, final Refreshable refreshable, final Fragment fragment, final MainActivity activity) {
        container.setVisibility(View.GONE);
        new InfoPrerequisiteRequest(
                new Runnable() {
                    @Override
                    public void run() {
                        final int watchingAccountId = PreferencesManager.getWatchingAccountId();
                        final boolean isOwnAccount = (watchingAccountId == 0) || (watchingAccountId == PreferencesManager.getAccountId());
                        if(!isOwnAccount) {
                            container.setVisibility(View.VISIBLE);

                            final TextView findPlayerText = (TextView) container.findViewById(R.id.find_player_widget_text);
                            findPlayerText.setText(Html.fromHtml(activity.getString(R.string.find_player_info_short, PreferencesManager.getWatchingAccountName())));
                            new GameInfoRequest(true).execute(watchingAccountId, new ApiResponseCallback<GameInfoResponse>() {
                                @Override
                                public void processResponse(GameInfoResponse response) {
                                    findPlayerText.setText(Html.fromHtml(activity.getString(R.string.find_player_info,
                                            PreferencesManager.getWatchingAccountName(), response.account.hero.basicInfo.name)));
                                }

                                @Override
                                public void processError(GameInfoResponse response) {
                                    // do nothing
                                }
                            }, true);

                            container.findViewById(R.id.find_player_widget_cancel).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    PreferencesManager.setWatchingAccount(0, null);
                                    refreshable.refresh(true);
                                    activity.refreshGameAdjacentFragments();
                                }
                            });
                            container.findViewById(R.id.find_player_widget_search).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    activity.onNavigationDrawerItemSelected(DrawerItem.FIND_PLAYER);
                                }
                            });
                        }
                    }
                }, new PrerequisiteRequest.ErrorCallback<InfoResponse>() {
            @Override
            public void processError(InfoResponse response) {
                // do nothing
            }
        }, fragment).execute();
    }

    public static MenuItem getMenuItem(final Activity activity, final int menuItemResId) {
        if(activity instanceof MainActivity) {
            final Menu menu = ((MainActivity) activity).getMenu();
            if(menu != null) {
                return menu.findItem(menuItemResId);
            }
        }
        return null;
    }

    public static MainActivity getMainActivity(final Fragment fragment) {
        final Activity activity = fragment.getActivity();
        if(activity instanceof MainActivity) {
            return (MainActivity) activity;
        } else {
            return null;
        }
    }

    public static void setRemoteViewsViewVisibility(final RemoteViews remoteViews, final int viewId, final boolean isVisible) {
        remoteViews.setViewVisibility(viewId, isVisible ? View.VISIBLE : View.GONE);
    }

    public static void runChecked(final Object uiComponent, final Runnable task) {
        final MainActivity mainActivity;
        if(uiComponent instanceof Fragment) {
            mainActivity = getMainActivity((Fragment) uiComponent);
        } else if(uiComponent instanceof MainActivity) {
            mainActivity = (MainActivity) uiComponent;
        } else {
            mainActivity = null;
        }

        if((mainActivity == null) || !mainActivity.isPaused()) {
            com.lonebytesoft.thetaleclient.sdkandroid.util.UiUtils.runChecked(uiComponent, task);
        }
    }

    public static void updateGlobalInfo(final Fragment fragment,
                                        final com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse gameInfoResponse) {
        final MainActivity mainActivity = getMainActivity(fragment);
        if(mainActivity != null) {
            mainActivity.updateGlobalInfo(gameInfoResponse == null ? null : gameInfoResponse.turnInfo);
        }
    }

}
