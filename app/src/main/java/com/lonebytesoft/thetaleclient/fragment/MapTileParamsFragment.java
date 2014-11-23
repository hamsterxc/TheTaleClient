package com.lonebytesoft.thetaleclient.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.api.model.PlaceInfo;
import com.lonebytesoft.thetaleclient.util.UiUtils;

/**
 * @author Hamster
 * @since 15.10.2014
 */
public class MapTileParamsFragment extends Fragment {

    private static final String PARAM_PLACE_INFO = "PARAM_PLACE_INFO";

    public static MapTileParamsFragment newInstance(final PlaceInfo placeInfo) {
        final MapTileParamsFragment dialog = new MapTileParamsFragment();

        Bundle args = new Bundle();
        args.putParcelable(PARAM_PLACE_INFO, placeInfo);

        dialog.setArguments(args);
        return dialog;
    }

    public MapTileParamsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_map_tile_tab_params, container, false);

        final PlaceInfo placeInfo = getArguments().getParcelable(PARAM_PLACE_INFO);
        UiUtils.setText(view.findViewById(R.id.map_tile_tab_params_size),
                UiUtils.getInfoItem(getString(R.string.map_place_size), String.valueOf(placeInfo.size)));
        UiUtils.setText(view.findViewById(R.id.map_tile_tab_params_race),
                UiUtils.getInfoItem(getString(R.string.map_place_race), placeInfo.race.getNamePlural()));

        return view;
    }

}
