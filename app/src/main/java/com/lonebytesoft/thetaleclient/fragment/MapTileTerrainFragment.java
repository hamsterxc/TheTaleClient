package com.lonebytesoft.thetaleclient.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.api.response.MapCellResponse;
import com.lonebytesoft.thetaleclient.util.UiUtils;

/**
 * @author Hamster
 * @since 15.10.2014
 */
public class MapTileTerrainFragment extends Fragment {

    private static final String PARAM_TERRAIN_DESCRIPTION = "PARAM_TERRAIN_DESCRIPTION";

    public static MapTileTerrainFragment newInstance(final MapCellResponse cellInfo) {
        final MapTileTerrainFragment dialog = new MapTileTerrainFragment();

        Bundle args = new Bundle();
        args.putStringArray(PARAM_TERRAIN_DESCRIPTION, cellInfo.terrainText.toArray(new String[cellInfo.terrainText.size()]));

        dialog.setArguments(args);
        return dialog;
    }

    public MapTileTerrainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_map_tile_tab_terrain, container, false);

        final String[] lines = getArguments().getStringArray(PARAM_TERRAIN_DESCRIPTION);
        final int count = lines.length;
        final StringBuilder stringBuilder = new StringBuilder();
        if(count > 0) {
            stringBuilder.append(lines[0]);
            for(int i = 1; i < count; i++) {
                stringBuilder.append("\n").append(lines[i]);
            }
        }
        UiUtils.setText(view.findViewById(R.id.map_tile_tab_terrain_info), stringBuilder.toString());

        return view;
    }

}
