package com.lonebytesoft.thetaleclient.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.api.response.MapCellResponse;
import com.lonebytesoft.thetaleclient.util.UiUtils;

/**
 * @author Hamster
 * @since 19.12.2014
 */
public abstract class MapTileFragment extends Fragment {

    private static final double DIALOG_MAX_HEIGHT_SCREEN_PART = 0.8;

    protected static final String PARAM_CELL_INFO = "PARAM_CELL_INFO";

    protected static void setupParams(final Fragment dialog, final MapCellResponse cellInfo) {
        Bundle args = new Bundle();
        args.putParcelable(PARAM_CELL_INFO, cellInfo);
        dialog.setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_map_tile_tab, container, false);

        final TextView text = (TextView) view.findViewById(R.id.map_tile_tab_text);
        setupText(text);

        final View contentContainer = view.findViewById(R.id.map_tile_tab_container);
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                final int textHeight = text.getHeight();
                if(textHeight > 0) {
                    UiUtils.setHeight(contentContainer, getContentHeight(textHeight));
                    UiUtils.removeGlobalLayoutListener(view, this);
                }
            }
        });

        return view;
    }

    protected abstract void setupText(final TextView text);

    private int getContentHeight(final int contentHeight) {
        final DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (int) Math.min(contentHeight, displayMetrics.heightPixels * DIALOG_MAX_HEIGHT_SCREEN_PART
                - getResources().getDimension(R.dimen.tab_strip_height)
                - getResources().getDimension(R.dimen.dialog_caption_height));
    }

}
