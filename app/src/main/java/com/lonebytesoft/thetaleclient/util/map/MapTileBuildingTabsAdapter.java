package com.lonebytesoft.thetaleclient.util.map;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.fragment.MapTileParametersFragment;
import com.lonebytesoft.thetaleclient.fragment.MapTileTerrainFragment;
import com.lonebytesoft.thetaleclient.fragment.dialog.TabbedDialog;
import com.lonebytesoft.thetaleclient.sdk.response.MapCellResponse;

/**
 * @author Hamster
 * @since 07.05.2015
 */
public class MapTileBuildingTabsAdapter extends TabbedDialog.TabbedDialogTabsAdapter {

    private static final String LOG_TAG = MapTileBuildingTabsAdapter.class.getSimpleName();

    private final Context context;
    private final MapCellResponse mapCellResponse;

    public MapTileBuildingTabsAdapter(final Context context, final MapCellResponse mapCellResponse) {
        this.context = context;
        this.mapCellResponse = mapCellResponse;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Fragment getItem(int i) {
        switch(i) {
            case 0:
                return MapTileParametersFragment.newInstance(mapCellResponse.parameters);

            case 1:
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
                return context.getString(R.string.map_tile_tab_terrain);

            default:
                throw new IllegalArgumentException(
                        String.format("%s requested tab title %d", LOG_TAG, position));
        }
    }

}
