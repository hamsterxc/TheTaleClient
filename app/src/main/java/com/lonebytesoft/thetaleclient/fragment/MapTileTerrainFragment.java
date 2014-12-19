package com.lonebytesoft.thetaleclient.fragment;

import android.widget.TextView;

import com.lonebytesoft.thetaleclient.api.response.MapCellResponse;
import com.lonebytesoft.thetaleclient.util.UiUtils;

/**
 * @author Hamster
 * @since 15.10.2014
 */
public class MapTileTerrainFragment extends MapTileFragment {

    public static MapTileTerrainFragment newInstance(final MapCellResponse cellInfo) {
        final MapTileTerrainFragment dialog = new MapTileTerrainFragment();
        setupParams(dialog, cellInfo);
        return dialog;
    }

    @Override
    protected void setupText(TextView text) {
        final MapCellResponse cellInfo = getArguments().getParcelable(PARAM_CELL_INFO);

        final StringBuilder stringBuilder = new StringBuilder();
        boolean first = true;
        for(final String line : cellInfo.terrain) {
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
