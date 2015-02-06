package com.lonebytesoft.thetaleclient.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PointF;
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
import com.lonebytesoft.thetaleclient.activity.MainActivity;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.CommonResponseCallback;
import com.lonebytesoft.thetaleclient.api.cache.prerequisite.InfoPrerequisiteRequest;
import com.lonebytesoft.thetaleclient.api.cache.prerequisite.PrerequisiteRequest;
import com.lonebytesoft.thetaleclient.api.dictionary.MapCellType;
import com.lonebytesoft.thetaleclient.api.dictionary.MapStyle;
import com.lonebytesoft.thetaleclient.api.model.PlaceInfo;
import com.lonebytesoft.thetaleclient.api.model.PositionInfo;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.request.MapCellRequest;
import com.lonebytesoft.thetaleclient.api.request.MapRequest;
import com.lonebytesoft.thetaleclient.api.request.MapTerrainRequest;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.api.response.InfoResponse;
import com.lonebytesoft.thetaleclient.api.response.MapCellResponse;
import com.lonebytesoft.thetaleclient.api.response.MapResponse;
import com.lonebytesoft.thetaleclient.api.response.MapTerrainResponse;
import com.lonebytesoft.thetaleclient.fragment.dialog.ChoiceDialog;
import com.lonebytesoft.thetaleclient.fragment.dialog.TabbedDialog;
import com.lonebytesoft.thetaleclient.util.DialogUtils;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.RequestUtils;
import com.lonebytesoft.thetaleclient.util.UiUtils;
import com.lonebytesoft.thetaleclient.util.map.MapModification;
import com.lonebytesoft.thetaleclient.util.map.MapUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @author Hamster
 * @since 12.10.2014
 */
public class MapFragment extends WrapperFragment {

    private static final String KEY_MAP_ZOOM = "KEY_MAP_ZOOM";
    private static final String KEY_MAP_SHIFT_X = "KEY_MAP_SHIFT_X";
    private static final String KEY_MAP_SHIFT_Y = "KEY_MAP_SHIFT_Y";
    private static final String KEY_MAP_MODIFICATION = "KEY_MAP_MODIFICATION";

    private static final float ZOOM_MAX = 3f;

    private LayoutInflater layoutInflater;

    private View rootView;

    private ImageView mapView;
    private PhotoViewAttacher mapViewHelper;

    private View actionMapStyle;
    private View actionMapModification;
    private View actionMapModificationContainer;

    private View findPlayerContainer;

    private float mapZoom;
    private float mapShiftX;
    private float mapShiftY;
    private boolean isMapInitialPosition = true;

    private PositionInfo heroPosition;
    private List<PlaceInfo> places;
    private MapModification mapModification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (ImageView) rootView.findViewById(R.id.map_content);
        mapViewHelper = new PhotoViewAttacher(mapView);
        mapViewHelper.setScaleType(ImageView.ScaleType.CENTER);

        actionMapStyle = rootView.findViewById(R.id.map_style);
        actionMapStyle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showChoiceDialog(getFragmentManager(), getString(R.string.map_style_caption),
                        ObjectUtils.getNamesForEnum(MapStyle.class), new ChoiceDialog.ItemChooseListener() {
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

        actionMapModificationContainer = rootView.findViewById(R.id.map_modification_container);
        actionMapModification = rootView.findViewById(R.id.map_modification);
        actionMapModification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUtils.showChoiceDialog(getChildFragmentManager(), getString(R.string.map_modification_caption),
                        ObjectUtils.getNamesForEnum(MapModification.class), new ChoiceDialog.ItemChooseListener() {
                            @Override
                            public void onItemSelected(int position) {
                                mapModification = MapModification.values()[position];
                                refresh(true);
                            }
                        },
                        R.layout.dialog_content_map_modification, R.id.dialog_map_modification_list);
            }
        });

        if(savedInstanceState != null) {
            mapZoom = savedInstanceState.getFloat(KEY_MAP_ZOOM, 1.0f);
            mapShiftX = savedInstanceState.getFloat(KEY_MAP_SHIFT_X, 0.0f);
            mapShiftY = savedInstanceState.getFloat(KEY_MAP_SHIFT_Y, 0.0f);
            mapModification = MapModification.values()[savedInstanceState.getInt(KEY_MAP_MODIFICATION, MapModification.NONE.ordinal())];
        } else {
            mapZoom = 1.0f;
            mapShiftX = 0.0f;
            mapShiftY = 0.0f;
            mapModification = MapModification.NONE;
        }

        findPlayerContainer = rootView.findViewById(R.id.map_find_player);
        UiUtils.setupFindPlayerContainer(findPlayerContainer, this, this, (MainActivity) getActivity());

        return wrapView(layoutInflater, rootView);
    }

    public void onSaveInstanceState(Bundle outState) {
        outState.putFloat(KEY_MAP_ZOOM, getMapZoom());

        final PointF mapShift = getMapShift();
        outState.putFloat(KEY_MAP_SHIFT_X, mapShift.x);
        outState.putFloat(KEY_MAP_SHIFT_Y, mapShift.y);

        outState.putInt(KEY_MAP_MODIFICATION, mapModification.ordinal());
    }

    @Override
    public void refresh(final boolean isGlobal) {
        super.refresh(isGlobal);

        UiUtils.setupFindPlayerContainer(findPlayerContainer, this, this, (MainActivity) getActivity());

        if(!isMapInitialPosition) {
            mapZoom = getMapZoom();

            final PointF mapShift = getMapShift();
            mapShiftX = mapShift.x;
            mapShiftY = mapShift.y;
        }

        mapView.setImageBitmap(null);
        System.gc();

        final MapStyle mapStyle = PreferencesManager.getMapStyle();
        UiUtils.setText(actionMapStyle, getString(R.string.map_style, mapStyle.getName()));

        UiUtils.setText(actionMapModification, getString(R.string.map_modification, mapModification.getName()));

        new InfoPrerequisiteRequest(new Runnable() {
            @Override
            public void run() {
                final ApiResponseCallback<GameInfoResponse> gameInfoCallback = RequestUtils.wrapCallback(new ApiResponseCallback<GameInfoResponse>() {
                    @Override
                    public void processResponse(final GameInfoResponse gameInfoResponse) {
                        new MapRequest(gameInfoResponse.mapVersion).execute(RequestUtils.wrapCallback(new CommonResponseCallback<MapResponse, String>() {
                            @Override
                            public void processResponse(final MapResponse mapResponse) {
                                MapUtils.getMapSprite(mapStyle, new MapUtils.MapBitmapCallback() {
                                    @Override
                                    public void onBitmapBuilt(final Bitmap sprite) {
                                        if(!isAdded()) {
                                            return;
                                        }

                                        heroPosition = gameInfoResponse.account.hero.position;

                                        final Bitmap map = MapUtils.getMapBitmap(mapResponse);
                                        final Canvas canvas = new Canvas(map);

                                        if(MapUtils.getCurrentSizeDenominator() == 1) {
                                            actionMapModificationContainer.setVisibility(View.VISIBLE);
                                        } else {
                                            actionMapModificationContainer.setVisibility(View.GONE);
                                            DialogUtils.showMessageDialog(getChildFragmentManager(),
                                                    getString(R.string.common_dialog_attention_title),
                                                    getString(R.string.map_decreased_quality));
                                        }

                                        if(mapModification == MapModification.NONE) {
                                            MapUtils.drawBaseLayer(canvas, mapResponse, sprite);
                                            MapUtils.drawPlaceNamesLayer(canvas, mapResponse);
                                            MapUtils.drawHeroLayer(canvas, gameInfoResponse.account.hero, sprite);
                                            setMap(map, mapResponse);
                                        } else {
                                            new MapTerrainRequest().execute(RequestUtils.wrapCallback(new CommonResponseCallback<MapTerrainResponse, String>() {
                                                @Override
                                                public void processResponse(final MapTerrainResponse mapTerrainResponse) {
                                                    switch(mapModification) {
                                                        case WIND:
                                                            MapUtils.drawModificationLayer(canvas, mapResponse, mapTerrainResponse, mapModification);
                                                            break;

                                                        case INFLUENCE:
                                                            MapUtils.drawBaseLayer(canvas, mapResponse, sprite);
                                                            MapUtils.drawModificationLayer(canvas, mapResponse, mapTerrainResponse, mapModification);
                                                            MapUtils.drawPlaceNamesLayer(canvas, mapResponse);
                                                            MapUtils.drawHeroLayer(canvas, gameInfoResponse.account.hero, sprite);
                                                            break;
                                                    }
                                                    setMap(map, mapResponse);
                                                }

                                                @Override
                                                public void processError(String error) {
                                                    setError(getString(R.string.map_error));
                                                    mapModification = MapModification.NONE;
                                                }
                                            }, MapFragment.this));
                                        }
                                    }

                                    @Override
                                    public void onError() {
                                        if(!isAdded()) {
                                            return;
                                        }

                                        setError(getString(R.string.map_error));
                                    }
                                });
                            }

                            @Override
                            public void processError(String error) {
                                setError(getString(R.string.map_error));
                            }
                        }, MapFragment.this));
                    }

                    @Override
                    public void processError(GameInfoResponse response) {
                        setError(getString(R.string.map_error));
                    }
                }, MapFragment.this);

                final int watchingAccountId = PreferencesManager.getWatchingAccountId();
                if(watchingAccountId == 0) {
                    new GameInfoRequest(true).execute(gameInfoCallback, true);
                } else {
                    new GameInfoRequest(true).execute(watchingAccountId, gameInfoCallback, true);
                }
            }
        }, new PrerequisiteRequest.ErrorCallback<InfoResponse>() {
            @Override
            public void processError(InfoResponse response) {
                setError(getString(R.string.map_error));
            }
        }, this).execute();
    }

    private void moveToTile(final int tileX, final int tileY) {
        mapViewHelper.setScale(ZOOM_MAX * MapUtils.getCurrentSizeDenominator());
        final float scale = mapViewHelper.getScale();
        final float newCenterX = (tileX + 0.5f) * MapUtils.MAP_TILE_SIZE / MapUtils.getCurrentSizeDenominator() * scale;
        final float newCenterY = (tileY + 0.5f) * MapUtils.MAP_TILE_SIZE / MapUtils.getCurrentSizeDenominator() * scale;
        final float newRectLeft = mapView.getWidth() / 2.0f - newCenterX;
        final float newRectTop = mapView.getHeight() / 2.0f - newCenterY;
        final RectF currentRect = mapViewHelper.getDisplayRect();
        mapViewHelper.onDrag(newRectLeft - currentRect.left, newRectTop - currentRect.top);
    }

    private float getMapZoom() {
        float mapZoom = mapViewHelper.getScale();
        if (mapZoom > ZOOM_MAX) {
            mapZoom = ZOOM_MAX;
        }

        return mapZoom;
    }

    private PointF getMapShift() {
        final RectF currentRect = mapViewHelper.getDisplayRect();
        final float currentDrawableWidth = currentRect.right - currentRect.left;
        final float currentDrawableHeight = currentRect.bottom - currentRect.top;
        final float viewWidth = mapView.getWidth();
        final float viewHeight = mapView.getHeight();

        final float centeredRectLeft = (viewWidth - currentDrawableWidth) / 2.0f;
        final float centeredRectTop = (viewHeight - currentDrawableHeight) / 2.0f;

        return new PointF(currentRect.left - centeredRectLeft, currentRect.top - centeredRectTop);
    }

    private void setMap(final Bitmap map, final MapResponse mapResponse) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mapView.setImageBitmap(map);
                mapViewHelper.update();
                if (!isMapInitialPosition) {
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
                        if ((viewWidth != 0) && (viewHeight != 0)) {
                            final int currentSizeDenominator = MapUtils.getCurrentSizeDenominator();
                            final float minimumScale;
                            if (viewWidth < viewHeight) {
                                minimumScale = (float) viewWidth / width;
                            } else {
                                minimumScale = (float) viewHeight / height;
                            }

                            if (isMapInitialPosition) {
                                isMapInitialPosition = false;
                                mapViewHelper.setMaximumScale(ZOOM_MAX * currentSizeDenominator);
                                mapViewHelper.setMediumScale((ZOOM_MAX * currentSizeDenominator + minimumScale) / 2.0f);
                                mapViewHelper.setMinimumScale(minimumScale);
                                final PlaceInfo placeInfo = mapResponse.places.get(PreferencesManager.getMapCenterPlaceId());
                                if(placeInfo == null) {
                                    mapViewHelper.setScale(mapViewHelper.getMediumScale());
                                    mapViewHelper.onDrag(mapShiftX, mapShiftY);
                                } else {
                                    PreferencesManager.setMapCenterPlaceId(-1);
                                    moveToTile(placeInfo.x, placeInfo.y);
                                }
                            }

                            UiUtils.removeGlobalLayoutListener(mapView, this);
                        }
                    }
                });

                mapViewHelper.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float x, float y) {
                        final int tileX = (int) Math.floor(x * width * MapUtils.getCurrentSizeDenominator() / MapUtils.MAP_TILE_SIZE);
                        final int tileY = (int) Math.floor(y * height * MapUtils.getCurrentSizeDenominator() / MapUtils.MAP_TILE_SIZE);

                        DialogUtils.showTabbedDialog(getChildFragmentManager(), getString(R.string.drawer_title_map), null);

                        new MapCellRequest().execute(tileX, tileY, RequestUtils.wrapCallback(new CommonResponseCallback<MapCellResponse, String>() {
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
                                            dialog.setCaption(response.title == null ? getString(R.string.map_tile_caption, tileX, tileY) : response.title);
                                            dialog.setTabsAdapter(new TileTabsAdapter(response));
                                            dialog.setMode(DataViewMode.DATA);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void processError(String error) {
                                final Handler handler = new Handler();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        final TabbedDialog dialog = (TabbedDialog) getChildFragmentManager().findFragmentByTag(DialogUtils.DIALOG_TABBED_TAG);
                                        if (dialog == null) {
                                            handler.post(this);
                                        } else {
                                            dialog.dismiss();
                                        }
                                    }
                                });
                                setError(getString(R.string.map_error));
                            }
                        }, MapFragment.this));
                    }
                });

                places = new ArrayList<>(mapResponse.places.size());
                for (final PlaceInfo placeInfo : mapResponse.places.values()) {
                    places.add(placeInfo);
                }
                Collections.sort(places, new Comparator<PlaceInfo>() {
                    @Override
                    public int compare(PlaceInfo lhs, PlaceInfo rhs) {
                        return lhs.name.compareTo(rhs.name);
                    }
                });
                setMode(DataViewMode.DATA);
            }
        });
    }

    private class TileTabsAdapter extends TabbedDialog.TabbedDialogTabsAdapter {

        private final MapCellResponse cellInfo;

        public TileTabsAdapter(final MapCellResponse cellInfo) {
            this.cellInfo = cellInfo;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getTileTab(position).getTitle();
        }

        @Override
        public Fragment getItem(int i) {
            return getTileTab(i).getFragment(cellInfo);
        }

        @Override
        public int getCount() {
            return TileTab.getTabs(cellInfo.type).size();
        }

        private TileTab getTileTab(final int position) {
            return TileTab.getTabs(cellInfo.type).get(position);
        }

    }

    private enum TileTab {

        PARAMETERS(R.string.map_tile_tab_params, new MapCellType[]{MapCellType.PLACE, MapCellType.BUILDING}) {
            @Override
            public Fragment getFragment(MapCellResponse cellInfo) {
                return MapTileParamsFragment.newInstance(cellInfo);
            }
        },
        COUNCIL(R.string.map_tile_tab_council, new MapCellType[]{MapCellType.PLACE}) {
            @Override
            public Fragment getFragment(MapCellResponse cellInfo) {
                return MapTileCouncilFragment.newInstance(cellInfo);
            }
        },
        DESCRIPTION(R.string.map_tile_tab_description, new MapCellType[]{MapCellType.PLACE}) {
            @Override
            public Fragment getFragment(MapCellResponse cellInfo) {
                return MapTileDescriptionFragment.newInstance(cellInfo);
            }
        },
        TERRAIN(R.string.map_tile_tab_terrain, new MapCellType[]{MapCellType.PLACE, MapCellType.BUILDING, MapCellType.TERRAIN}) {
            @Override
            public Fragment getFragment(MapCellResponse cellInfo) {
                return MapTileTerrainFragment.newInstance(cellInfo);
            }
        },
        ;

        private final int titleResId;
        private final MapCellType[] cellTypes;

        private TileTab(final int titleResId, final MapCellType[] cellTypes) {
            this.titleResId = titleResId;
            this.cellTypes = cellTypes;
        }

        public String getTitle() {
            return TheTaleClientApplication.getContext().getString(titleResId);
        }

        public abstract Fragment getFragment(final MapCellResponse cellInfo);

        private static final Map<MapCellType, List<TileTab>> tabs;

        static {
            tabs = new HashMap<>(MapCellType.values().length);
            for(final TileTab tileTab : values()) {
                for(final MapCellType cellType : tileTab.cellTypes) {
                    List<TileTab> tileTabs = tabs.get(cellType);
                    if(tileTabs == null) {
                        tileTabs = new ArrayList<>();
                        tileTabs.add(tileTab);
                        tabs.put(cellType, tileTabs);
                    } else {
                        tileTabs.add(tileTab);
                    }
                }
            }
        }

        public static List<TileTab> getTabs(final MapCellType cellType) {
            return tabs.get(cellType);
        }

    }

}
