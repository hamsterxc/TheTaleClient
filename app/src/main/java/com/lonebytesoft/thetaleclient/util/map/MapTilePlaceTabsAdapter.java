package com.lonebytesoft.thetaleclient.util.map;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.fragment.MapTileCouncilFragment;
import com.lonebytesoft.thetaleclient.fragment.MapTileDescriptionFragment;
import com.lonebytesoft.thetaleclient.fragment.MapTileParametersFragment;
import com.lonebytesoft.thetaleclient.fragment.MapTileTerrainFragment;
import com.lonebytesoft.thetaleclient.fragment.dialog.TabbedDialog;
import com.lonebytesoft.thetaleclient.sdk.dictionary.PlaceParameter;
import com.lonebytesoft.thetaleclient.sdk.model.PlaceParameterInfo;
import com.lonebytesoft.thetaleclient.sdk.response.MapCellResponse;
import com.lonebytesoft.thetaleclient.sdk.response.PlaceResponse;
import com.lonebytesoft.thetaleclient.util.GameInfoUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Hamster
 * @since 07.05.2015
 */
public class MapTilePlaceTabsAdapter extends TabbedDialog.TabbedDialogTabsAdapter {

    private static final String LOG_TAG = MapTileTerrainTabsAdapter.class.getSimpleName();

    private final Context context;
    private final PlaceResponse placeResponse;
    private final MapCellResponse mapCellResponse;

    public MapTilePlaceTabsAdapter(final Context context, final PlaceResponse placeResponse,
                                   final MapCellResponse mapCellResponse) {
        this.context = context;
        this.placeResponse = placeResponse;
        this.mapCellResponse = mapCellResponse;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            case 0:
                final Map<String, String> parameters = new LinkedHashMap<>(placeResponse.parameters.size());
                for(final PlaceParameter placeParameter : PlaceParameter.values()) {
                    final PlaceParameterInfo parameterInfo = placeResponse.parameters.get(placeParameter);
                    if(parameterInfo != null) {
                        parameters.put(placeParameter.name,
                                GameInfoUtils.getPlaceParameterValue(placeParameter, parameterInfo));
                    }
                }
                return MapTileParametersFragment.newInstance(parameters);

            case 1:
                return MapTileCouncilFragment.newInstance(placeResponse.council);

            case 2:
                return MapTileDescriptionFragment.newInstance(placeResponse.description);

            case 3:
                return MapTileTerrainFragment.newInstance(mapCellResponse.terrain);

            default:
                throw new IllegalArgumentException(String.format("%s requested tab %d", LOG_TAG, i));
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return context.getString(R.string.map_tile_tab_params);

            case 1:
                return context.getString(R.string.map_tile_tab_council);

            case 2:
                return context.getString(R.string.map_tile_tab_description);

            case 3:
                return context.getString(R.string.map_tile_tab_terrain);

            default:
                throw new IllegalArgumentException(
                        String.format("%s requested tab title %d", LOG_TAG, position));
        }
    }

}
