package com.lonebytesoft.thetaleclient.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.util.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hamster
 * @since 15.10.2014
 */
public class MapTileTerrainFragment extends TabbedDialogTabFragment {

    private static final String PARAM_TERRAIN = "PARAM_TERRAIN";

    public static MapTileTerrainFragment newInstance(final List<String> terrain) {
        final MapTileTerrainFragment dialog = new MapTileTerrainFragment();

        final Bundle args = new Bundle();
        args.putStringArrayList(PARAM_TERRAIN, new ArrayList<>(terrain));
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    protected void setupContent(final LayoutInflater layoutInflater, final ViewGroup container) {
        final View content = layoutInflater.inflate(R.layout.tabbed_dialog_tab_content_text, container, true);
        final TextView text = (TextView) content.findViewById(R.id.tabbed_dialog_tab_content_text);

        final List<String> terrain = getArguments().getStringArrayList(PARAM_TERRAIN);
        final StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;
        for(final String line : terrain) {
            if(first) {
                first = false;
            } else {
                stringBuilder.append("\n");
            }
            stringBuilder.append(line);
        }
        UiUtils.setText(text, stringBuilder.toString());
    }

}
