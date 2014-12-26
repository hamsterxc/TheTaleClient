package com.lonebytesoft.thetaleclient.fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.R;
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
    protected void setupContent(final LayoutInflater layoutInflater, final ViewGroup container) {
        final View content = layoutInflater.inflate(R.layout.fragment_map_tile_tab_content_text, container, true);
        final TextView text = (TextView) content.findViewById(R.id.map_tile_tab_content_text);

        final MapCellResponse cellInfo = getArguments().getParcelable(PARAM_CELL_INFO);
        UiUtils.setText(text, Html.fromHtml(String.valueOf(cellInfo.description)));
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
