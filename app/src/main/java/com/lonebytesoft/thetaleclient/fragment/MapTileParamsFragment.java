package com.lonebytesoft.thetaleclient.fragment;

import android.text.SpannableStringBuilder;
import android.widget.TextView;

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
    protected void setupText(TextView text) {
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
