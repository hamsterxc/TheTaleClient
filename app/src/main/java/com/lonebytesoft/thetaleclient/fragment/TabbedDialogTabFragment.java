package com.lonebytesoft.thetaleclient.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.util.UiUtils;

/**
 * @author Hamster
 * @since 19.12.2014
 */
public abstract class TabbedDialogTabFragment extends Fragment {

    private static final double DIALOG_MAX_HEIGHT_SCREEN_PART = 0.8;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tabbed_dialog_tab, container, false);

        final ViewGroup content = (ViewGroup) view.findViewById(R.id.tabbed_dialog_tab_content);
        setupContent(inflater, content);

        final View contentContainer = view.findViewById(R.id.tabbed_dialog_tab_container);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int contentHeight = content.getHeight();
                if(contentHeight > 0) {
                    UiUtils.setHeight(contentContainer, getContentHeight(contentHeight));
                    UiUtils.removeGlobalLayoutListener(view, this);
                }
            }
        });

        return view;
    }

    protected abstract void setupContent(final LayoutInflater layoutInflater, final ViewGroup container);

    private int getContentHeight(final int contentHeight) {
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (int) Math.min(contentHeight, displayMetrics.heightPixels * DIALOG_MAX_HEIGHT_SCREEN_PART
                - getResources().getDimension(R.dimen.tab_strip_height)
                - getResources().getDimension(R.dimen.dialog_caption_height));
    }

}
