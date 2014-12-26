package com.lonebytesoft.thetaleclient.fragment;

import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.api.response.MapCellResponse;
import com.lonebytesoft.thetaleclient.util.UiUtils;

import java.util.Map;

/**
 * @author Hamster
 * @since 15.10.2014
 */
public class MapTileParamsFragment extends MapTileFragment {

    public static MapTileParamsFragment newInstance(final MapCellResponse cellInfo) {
        final MapTileParamsFragment dialog = new MapTileParamsFragment();
        setupParams(dialog, cellInfo);
        return dialog;
    }

    @Override
    protected void setupContent(final LayoutInflater layoutInflater, final ViewGroup container) {
        final View content = layoutInflater.inflate(R.layout.fragment_map_tile_tab_content_text, container, true);
        final TextView text = (TextView) content.findViewById(R.id.map_tile_tab_content_text);

        final MapCellResponse cellInfo = getArguments().getParcelable(PARAM_CELL_INFO);
        final SpannableStringBuilder info = new SpannableStringBuilder();
        boolean first = true;
        for(final Map.Entry<String, String> entry : cellInfo.parameters.entrySet()) {
            if(first) {
                first = false;
            } else {
                info.append("\n");
            }

            info.append(UiUtils.getInfoItem(entry.getKey(), entry.getValue()));
        }
        UiUtils.setText(text, info);
    }

}
