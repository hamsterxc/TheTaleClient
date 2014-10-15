package com.lonebytesoft.thetaleclient.fragment;

import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.lonebytesoft.thetaleclient.DataViewMode;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.CommonResponseCallback;
import com.lonebytesoft.thetaleclient.api.dictionary.MapStyle;
import com.lonebytesoft.thetaleclient.api.model.PlaceInfo;
import com.lonebytesoft.thetaleclient.api.model.PositionInfo;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.request.MapCellRequest;
import com.lonebytesoft.thetaleclient.api.request.MapRequest;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.api.response.MapCellResponse;
import com.lonebytesoft.thetaleclient.api.response.MapResponse;
import com.lonebytesoft.thetaleclient.fragment.dialog.ChoiceDialog;
import com.lonebytesoft.thetaleclient.fragment.dialog.TabbedDialog;
import com.lonebytesoft.thetaleclient.util.DialogUtils;
import com.lonebytesoft.thetaleclient.util.MapManager;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.UiUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @author Hamster
 * @since 12.10.2014
 */
public class MapFragment extends WrapperFragment {

    private static final float ZOOM_MAX = 3f;

    private LayoutInflater layoutInflater;

    private View rootView;

    private ImageView mapView;
    private PhotoViewAttacher mapViewHelper;

    private View actionMapStyle;

    private float mapZoom;
    private float mapShiftX;
    private float mapShiftY;
    private boolean isMapInitialPosition = true;

    private PositionInfo heroPosition;
    private List<PlaceInfo> places;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (ImageView) rootView.findViewById(R.id.map_content);
        mapViewHelper = new PhotoViewAttacher(mapView);
        mapViewHelper.setScaleType(ImageView.ScaleType.CENTER);
        mapViewHelper.setMaximumScale(ZOOM_MAX);

        actionMapStyle = rootView.findViewById(R.id.map_style);
        actionMapStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int count = MapStyle.values().length;
                final String[] choices = new String[count];
                for(int i = 0; i < count; i++) {
                    choices[i] = MapStyle.values()[i].getName();
                }

                DialogUtils.showChoiceDialog(getFragmentManager(), getString(R.string.map_style_caption), choices, new ChoiceDialog.ItemChooseListener() {
                    @Override
                    public void onItemSelected(int position) {
                        PreferencesManager.setMapStyle(MapStyle.values()[position]);
                        refresh(true);
                    }
                });
            }
        });

        rootView.findViewById(R.id.map_find_place).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int count = places.size();
                final String[] choices = new String[count];
                for(int i = 0; i < count; i++) {
                    choices[i] = places.get(i).name;
                }

                DialogUtils.showChoiceDialog(getFragmentManager(), getString(R.string.map_find_place), choices, new ChoiceDialog.ItemChooseListener() {
                    @Override
                    public void onItemSelected(int position) {
                        final PlaceInfo placeInfo = places.get(position);
                        moveToTile(placeInfo.x, placeInfo.y);
                    }
                });
            }
        });

        rootView.findViewById(R.id.map_find_hero).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveToTile((int) Math.round(heroPosition.x), (int) Math.round(heroPosition.y));
            }
        });

        return wrapView(layoutInflater, rootView);
    }

    @Override
    public void refresh(final boolean isGlobal) {
        super.refresh(isGlobal);

        if(!isMapInitialPosition) {
            mapZoom = mapViewHelper.getScale();
            if (mapZoom > ZOOM_MAX) {
                mapZoom = ZOOM_MAX;
            }

            final RectF currentRect = mapViewHelper.getDisplayRect();
            final float currentDrawableWidth = currentRect.right - currentRect.left;
            final float currentDrawableHeight = currentRect.bottom - currentRect.top;
            final float viewWidth = mapView.getWidth();
            final float viewHeight = mapView.getHeight();

            final float centeredRectLeft = (viewWidth - currentDrawableWidth) / 2.0f;
            final float centeredRectTop = (viewHeight - currentDrawableHeight) / 2.0f;
            mapShiftX = currentRect.left - centeredRectLeft;
            mapShiftY = currentRect.top - centeredRectTop;
        }

        mapView.setImageBitmap(null);
        System.gc();

        final MapStyle mapStyle = PreferencesManager.getMapStyle();
        UiUtils.setText(actionMapStyle, getString(R.string.map_style, mapStyle.getName()));

        MapManager.buildMapBitmap(new MapManager.MapBitmapCallback() {
            @Override
            public void onBitmapBuilt(final Bitmap map) {
                new GameInfoRequest().execute(new ApiResponseCallback<GameInfoResponse>() {
                    @Override
                    public void processResponse(final GameInfoResponse response) {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                heroPosition = response.account.hero.position;
                                MapManager.drawHero(map, heroPosition,
                                        MapManager.getSpriteTile(response.account.hero.spriteId), mapStyle);
                                mapView.setImageBitmap(map);
                                mapViewHelper.update();
                                if(isMapInitialPosition) {
                                    isMapInitialPosition = false;
                                } else {
                                    mapViewHelper.setScale(mapZoom);
                                    mapViewHelper.onDrag(mapShiftX, mapShiftY);
                                }

                                final int width = mapView.getDrawable().getIntrinsicWidth();
                                final int height = mapView.getDrawable().getIntrinsicHeight();

                                mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @Override
                                    public void onGlobalLayout() {
                                        final int viewWidth = mapView.getWidth();
                                        final int viewHeight = mapView.getHeight();
                                        if((viewWidth != 0) && (viewHeight != 0)) {
                                            if(viewWidth < viewHeight) {
                                                mapViewHelper.setMinimumScale((float) viewWidth / width);
                                            } else {
                                                mapViewHelper.setMinimumScale((float) viewHeight / height);
                                            }
                                            UiUtils.removeGlobalLayoutListener(mapView, this);
                                        }
                                    }
                                });

                                mapViewHelper.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                                    @Override
                                    public void onPhotoTap(View view, float x, float y) {
                                        final int tileX = (int) Math.floor(x * width / MapManager.MAP_TILE_SIZE);
                                        final int tileY = (int) Math.floor(y * height / MapManager.MAP_TILE_SIZE);

                                        DialogUtils.showTabbedDialog(getChildFragmentManager(), getString(R.string.drawer_title_map), null);

                                        new MapRequest(response.mapVersion).execute(new CommonResponseCallback<MapResponse, String>() {
                                            @Override
                                            public void processResponse(MapResponse response) {
                                                PlaceInfo placeInfo = null;
                                                for(final Map.Entry<Integer, PlaceInfo> entry : response.places.entrySet()) {
                                                    final PlaceInfo entryPlaceInfo = entry.getValue();
                                                    if((entryPlaceInfo.x == tileX) && (entryPlaceInfo.y == tileY)) {
                                                        placeInfo = entryPlaceInfo;
                                                        break;
                                                    }
                                                }

                                                final PlaceInfo placeInfoFinal = placeInfo;
                                                new MapCellRequest().execute(tileX, tileY, new CommonResponseCallback<MapCellResponse, String>() {
                                                    @Override
                                                    public void processResponse(final MapCellResponse response) {
                                                        // request may be completed before fragment is instantiated, we'll wait for it
                                                        final Handler handler = new Handler();
                                                        handler.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                final TabbedDialog dialog = (TabbedDialog) getChildFragmentManager().findFragmentByTag(DialogUtils.DIALOG_TABBED_TAG);
                                                                if (dialog == null) {
                                                                    handler.post(this);
                                                                } else {
                                                                    dialog.setCaption(placeInfoFinal == null ? getString(R.string.map_tile_caption, tileX, tileY) : placeInfoFinal.name);
                                                                    dialog.setTabsAdapter(new TileTabsAdapter(response, placeInfoFinal));
                                                                    dialog.setMode(DataViewMode.DATA);
                                                                }
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void processError(String error) {
                                                        setError(getString(R.string.map_error));
                                                    }
                                                });
                                            }

                                            @Override
                                            public void processError(String error) {
                                                setError(getString(R.string.map_error));
                                            }
                                        });
                                    }
                                });

                                new MapRequest(response.mapVersion).execute(new CommonResponseCallback<MapResponse, String>() {
                                    @Override
                                    public void processResponse(MapResponse response) {
                                        places = new ArrayList<>(response.places.size());
                                        for(final Map.Entry<Integer, PlaceInfo> entry : response.places.entrySet()) {
                                            places.add(entry.getValue());
                                        }
                                        Collections.sort(places, new Comparator<PlaceInfo>() {
                                            @Override
                                            public int compare(PlaceInfo lhs, PlaceInfo rhs) {
                                                return lhs.name.compareTo(rhs.name);
                                            }
                                        });
                                        setMode(DataViewMode.DATA);
                                    }

                                    @Override
                                    public void processError(String error) {
                                        setError(getString(R.string.map_error));
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void processError(GameInfoResponse response) {
                        setError(getString(R.string.map_error));
                    }
                });
            }

            @Override
            public void onError() {
                setError(getString(R.string.map_error));
            }
        }, mapStyle);
    }

    private void moveToTile(final int tileX, final int tileY) {
        mapViewHelper.setScale(ZOOM_MAX);
        final float scale = mapViewHelper.getScale();
        final float newCenterX = (tileX + 0.5f) * MapManager.MAP_TILE_SIZE * scale;
        final float newCenterY = (tileY + 0.5f) * MapManager.MAP_TILE_SIZE * scale;
        final float newRectLeft = mapView.getWidth() / 2.0f - newCenterX;
        final float newRectTop = mapView.getHeight() / 2.0f - newCenterY;
        final RectF currentRect = mapViewHelper.getDisplayRect();
        mapViewHelper.onDrag(newRectLeft - currentRect.left, newRectTop - currentRect.top);
    }

    private class TileTabsAdapter extends TabbedDialog.TabbedDialogTabsAdapter {

        private final MapCellResponse cellInfo;
        private final PlaceInfo placeInfo;

        public TileTabsAdapter(final MapCellResponse cellInfo, final PlaceInfo placeInfo) {
            this.cellInfo = cellInfo;
            this.placeInfo = placeInfo;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getTileTab(position).getTitle();
        }

        @Override
        public Fragment getItem(int i) {
            switch(getTileTab(i)) {
                case PARAMETERS:
                    return MapTileParamsFragment.newInstance(placeInfo);

                case TERRAIN:
                    return MapTileTerrainFragment.newInstance(cellInfo);

                default:
                    return new Fragment();
            }
        }

        @Override
        public int getCount() {
            return placeInfo == null ? 1 : 2;
        }

        private TileTab getTileTab(final int position) {
            if(placeInfo == null) {
                return position == 0 ? TileTab.TERRAIN : null;
            } else {
                switch(position) {
                    case 0:
                        return TileTab.PARAMETERS;

                    case 1:
                        return TileTab.TERRAIN;

                    default:
                        return null;
                }
            }
        }

    }

    private enum TileTab {

        PARAMETERS(R.string.map_tile_tab_params),
        TERRAIN(R.string.map_tile_tab_terrain),
        ;

        private final int titleResId;

        private TileTab(final int titleResId) {
            this.titleResId = titleResId;
        }

        public String getTitle() {
            return TheTaleClientApplication.getContext().getString(titleResId);
        }

    }

}
