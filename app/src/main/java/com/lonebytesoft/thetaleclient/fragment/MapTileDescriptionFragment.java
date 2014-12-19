package com.lonebytesoft.thetaleclient.fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.api.response.MapCellResponse;
import com.lonebytesoft.thetaleclient.util.UiUtils;

/**
 * @author Hamster
 * @since 18.12.2014
 */
public class MapTileDescriptionFragment extends MapTileFragment {

    public static MapTileDescriptionFragment newInstance(final MapCellResponse cellInfo) {
        final MapTileDescriptionFragment dialog = new MapTileDescriptionFragment();
        setupParams(dialog, cellInfo);
        return dialog;
    }

    @Override
    protected void setupText(TextView text) {
        final MapCellResponse cellInfo = getArguments().getParcelable(PARAM_CELL_INFO);
        UiUtils.setText(text, Html.fromHtml(String.valueOf(cellInfo.description)));
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
