package com.lonebytesoft.thetaleclient.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.lonebytesoft.thetaleclient.DataViewMode;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.activity.MainActivity;
import com.lonebytesoft.thetaleclient.apisdk.RequestExecutor;
import com.lonebytesoft.thetaleclient.fragment.dialog.ChoiceDialog;
import com.lonebytesoft.thetaleclient.fragment.dialog.TabbedDialog;
import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.dictionary.MapStyle;
import com.lonebytesoft.thetaleclient.sdk.model.HeroInfo;
import com.lonebytesoft.thetaleclient.sdk.model.MapPlaceInfo;
import com.lonebytesoft.thetaleclient.sdk.model.PlaceInfo;
import com.lonebytesoft.thetaleclient.sdk.model.PositionInfo;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.sdk.response.MapCellResponse;
import com.lonebytesoft.thetaleclient.sdk.response.MapResponse;
import com.lonebytesoft.thetaleclient.sdk.response.PlaceResponse;
import com.lonebytesoft.thetaleclient.sdk.response.PlacesResponse;
import com.lonebytesoft.thetaleclient.sdkandroid.ApiCallback;
import com.lonebytesoft.thetaleclient.sdkandroid.helper.MapHelper;
import com.lonebytesoft.thetaleclient.sdkandroid.request.MapCellRequestBuilder;
import com.lonebytesoft.thetaleclient.sdkandroid.request.PlaceRequestBuilder;
import com.lonebytesoft.thetaleclient.sdkandroid.request.PlacesRequestBuilder;
import com.lonebytesoft.thetaleclient.util.DialogUtils;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.RequestUtils;
import com.lonebytesoft.thetaleclient.util.UiUtils;
import com.lonebytesoft.thetaleclient.util.map.MapTileBuildingTabsAdapter;
import com.lonebytesoft.thetaleclient.util.map.MapTilePlaceTabsAdapter;
import com.lonebytesoft.thetaleclient.util.map.MapTileTerrainTabsAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * @author Hamster
 * @since 12.10.2014
 */
public class MapFragment extends WrapperFragment {

    private static final String KEY_MAP_ZOOM = "KEY_MAP_ZOOM";
    private static final String KEY_MAP_SHIFT_X = "KEY_MAP_SHIFT_X";
    private static final String KEY_MAP_SHIFT_Y = "KEY_MAP_SHIFT_Y";

    private static final float ZOOM_MAX = 3f;

    private LayoutInflater layoutInflater;
    private Handler handler = new Handler(Looper.getMainLooper());

    private View rootView;

    private ImageView mapView;
    private PhotoViewAttacher mapViewHelper;
    private MenuItem menuOptions;
    private View findPlayerContainer;

    private float mapZoom;
    private float mapShiftX;
    private float mapShiftY;
    private Point mapTileSize;
    private Point mapSize;
    private MapHelper.MapMemorySettings mapMemorySettings;
    private boolean isMapInitialPosition = true;
    private boolean shouldMoveToHero = false;
    private boolean shouldShowMenuOptions = true;

    private PositionInfo heroPosition;
    private List<MapPlaceInfo> places;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = (ImageView) rootView.findViewById(R.id.map_content);
        mapViewHelper = new PhotoViewAttacher(mapView);
        mapViewHelper.setScaleType(ImageView.ScaleType.CENTER);

        if(savedInstanceState != null) {
            mapZoom = savedInstanceState.getFloat(KEY_MAP_ZOOM, 1.0f);
            mapShiftX = savedInstanceState.getFloat(KEY_MAP_SHIFT_X, 0.0f);
            mapShiftY = savedInstanceState.getFloat(KEY_MAP_SHIFT_Y, 0.0f);
        } else {
            mapZoom = 1.0f;
            mapShiftX = 0.0f;
            mapShiftY = 0.0f;
            shouldMoveToHero = true;
        }

        findPlayerContainer = rootView.findViewById(R.id.map_find_player);
        UiUtils.setupFindPlayerContainer(findPlayerContainer, this, this, (MainActivity) getActivity());

        return wrapView(layoutInflater, rootView);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putFloat(KEY_MAP_ZOOM, getMapZoom());

        final PointF mapShift = getMapShift();
        outState.putFloat(KEY_MAP_SHIFT_X, mapShift.x);
        outState.putFloat(KEY_MAP_SHIFT_Y, mapShift.y);
    }

    private void updateMenuItemTitle(final int id, final String title) {
        final MenuItem menuItem = UiUtils.getMenuItem(getActivity(), id);
        if(menuItem != null) {
            menuItem.setTitle(title);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        menuOptions = UiUtils.getMenuItem(getActivity(), R.id.action_map_actions);
        if((menuOptions != null) && !shouldShowMenuOptions) {
            menuOptions.setVisible(false);
        }

        updateMenuItemTitle(R.id.action_map_style,
                getString(R.string.map_style, PreferencesManager.getMapStyle().name));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_map_style:
                DialogUtils.showChoiceDialog(getFragmentManager(), getString(R.string.map_style_caption),
                        ObjectUtils.getNamesForEnum(MapStyle.class), new ChoiceDialog.ItemChooseListener() {
                            @Override
                            public void onItemSelected(int position) {
                                PreferencesManager.setMapStyle(MapStyle.values()[position]);
                                refresh(true);
                            }
                        });
                return true;

            case R.id.action_map_find_place:
                final int count = places.size();
                final String[] choices = new String[count];
                for(int i = 0; i < count; i++) {
                    choices[i] = places.get(i).name;
                }

                DialogUtils.showChoiceDialog(getFragmentManager(), getString(R.string.map_find_place), choices, new ChoiceDialog.ItemChooseListener() {
                    @Override
                    public void onItemSelected(int position) {
                        final MapPlaceInfo placeInfo = places.get(position);
                        moveToTile(placeInfo.x, placeInfo.y, mapViewHelper.getMediumScale());
                    }
                });
                return true;

            case R.id.action_map_find_hero:
                moveToTile((int) Math.round(heroPosition.x), (int) Math.round(heroPosition.y),
                        mapViewHelper.getMediumScale());
                return true;

            case R.id.action_map_save:
                final ProgressDialog progressDialog = ProgressDialog.show(getActivity(),
                        getString(R.string.map_save), getString(R.string.map_save_progress), true, false);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                        path.mkdirs();

                        final String filenameBase = String.format("the-tale_map_%s",
                                new SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(new Date()));
                        File file;
                        int counter = 0;
                        do {
                            final String filename = counter == 0 ?
                                    filenameBase + ".png" :
                                    String.format("%s_%d.png", filenameBase, counter);
                            file = new File(path, filename);
                            counter++;
                        } while (file.exists());
                        final File fileMap = file;

                        OutputStream output = null;
                        try {
                            output = new FileOutputStream(fileMap);
                            ((BitmapDrawable) mapView.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 90, output);
                        } catch(FileNotFoundException e) {
                            showMapSaveError(e.getLocalizedMessage());
                        }

                        if(output != null) {
                            boolean success = false;
                            try {
                                output.close();
                                success = true;
                            } catch (IOException e) {
                                showMapSaveError(e.getLocalizedMessage());
                            }

                            if(success) {
                                UiUtils.runChecked(MapFragment.this, new Runnable() {
                                    @Override
                                    public void run() {
                                        DialogUtils.showConfirmationDialog(getChildFragmentManager(),
                                                getString(R.string.map_save), getString(R.string.map_save_message, fileMap.getPath()),
                                                null, null,
                                                getString(R.string.map_save_open), new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        final Intent intent = new Intent();
                                                        intent.setAction(Intent.ACTION_VIEW);
                                                        intent.setDataAndType(Uri.fromFile(fileMap), "image/png");
                                                        startActivity(intent);
                                                    }
                                                }, null);
                                    }
                                });
                            }
                        }

                        UiUtils.runChecked(MapFragment.this, new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                            }
                        });
                    }
                }).start();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showMapSaveError(final String error) {
        UiUtils.runChecked(this, new Runnable() {
            @Override
            public void run() {
                DialogUtils.showMessageDialog(getChildFragmentManager(),
                        getString(R.string.common_dialog_attention_title),
                        TextUtils.isEmpty(error) ? getString(R.string.map_save_error_short) : getString(R.string.map_save_error, error));
            }
        });
    }

    @Override
    public void refresh(final boolean isGlobal) {
        super.refresh(isGlobal);
        shouldShowMenuOptions = false;

        UiUtils.setupFindPlayerContainer(findPlayerContainer, this, this, (MainActivity) getActivity());

        if(menuOptions != null) {
            menuOptions.setVisible(false);
        }

        if(!isMapInitialPosition) {
            mapZoom = getMapZoom();

            final PointF mapShift = getMapShift();
            mapShiftX = mapShift.x;
            mapShiftY = mapShift.y;
        }

        mapView.setImageBitmap(null);
        System.gc();

        final MapStyle mapStyle = PreferencesManager.getMapStyle();
        updateMenuItemTitle(R.id.action_map_style, getString(R.string.map_style, mapStyle.name));

        RequestUtils.executeGameInfoRequestWatching(getActivity(), RequestUtils.wrapCallback(new ApiCallback<GameInfoResponse>() {
            @Override
            public void onSuccess(final GameInfoResponse gameInfoResponse) {
                UiUtils.updateGlobalInfo(MapFragment.this, gameInfoResponse);
                RequestUtils.executeMapRequest(getActivity(), RequestUtils.wrapCallback(new ApiCallback<MapResponse>() {
                    @Override
                    public void onSuccess(final MapResponse mapResponse) {
                        heroPosition = gameInfoResponse.account.hero.position;
                        final List<HeroInfo> heroes = new ArrayList<>();
                        heroes.add(gameInfoResponse.account.hero);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mapTileSize = new Point(mapResponse.width, mapResponse.height);
                                mapSize = MapHelper.getMapSize(mapResponse);
                                mapMemorySettings = MapHelper.getAvailableMapMemorySettings(mapResponse);
                                if (getSizeDenominator() > 1) {
                                    UiUtils.runChecked(MapFragment.this, new Runnable() {
                                        @Override
                                        public void run() {
                                            DialogUtils.showMessageDialog(getChildFragmentManager(),
                                                    getString(R.string.common_dialog_attention_title),
                                                    getString(R.string.map_decreased_quality));
                                        }
                                    });
                                }
                                final Bitmap map = MapHelper.getMapImage(
                                        mapMemorySettings.width, mapMemorySettings.height, PreferencesManager.getMapStyle(),
                                        true, mapResponse, PreferencesManager.getStaticContentUrl(), heroes, false);
                                UiUtils.runChecked(MapFragment.this, new Runnable() {
                                    @Override
                                    public void run() {
                                        if (map == null) {
                                            setError(getString(R.string.map_error));
                                        } else {
                                            setMap(map, mapResponse);
                                        }
                                    }
                                });
                            }
                        }).start();
                    }

                    @Override
                    public void onError(AbstractApiResponse response) {
                        setError(getString(R.string.map_error));
                    }
                }, MapFragment.this));
            }

            @Override
            public void onError(AbstractApiResponse response) {
                UiUtils.updateGlobalInfo(MapFragment.this, null);
                setError(getString(R.string.map_error));
            }
        }, this));
    }

    @Override
    public void onOnscreen() {
        super.onOnscreen();

        if(findPlayerContainer != null) {
            UiUtils.setupFindPlayerContainer(findPlayerContainer, this, this, (MainActivity) getActivity());
        }
    }

    private int getSizeDenominator() {
        return (int) Math.round(Math.max(
                (double) mapSize.x / mapMemorySettings.width,
                (double) mapSize.y / mapMemorySettings.height));
    }

    private void moveToTile(final int tileX, final int tileY, final float scale) {
        mapViewHelper.setScale(scale);
        final float newCenterX = (tileX + 0.5f) / mapTileSize.x * mapMemorySettings.width * scale;
        final float newCenterY = (tileY + 0.5f) / mapTileSize.y * mapMemorySettings.height * scale;
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
        UiUtils.runChecked(this, new Runnable() {
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
                            final float minimumScale;
                            if (viewWidth < viewHeight) {
                                minimumScale = (float) viewWidth / width;
                            } else {
                                minimumScale = (float) viewHeight / height;
                            }

                            if (isMapInitialPosition) {
                                isMapInitialPosition = false;
                                mapViewHelper.setMaximumScale(ZOOM_MAX * getSizeDenominator());
                                mapViewHelper.setMediumScale((ZOOM_MAX * getSizeDenominator() + minimumScale) / 2.0f);
                                mapViewHelper.setMinimumScale(minimumScale);
                                final MapPlaceInfo placeInfo = mapResponse.places.get(PreferencesManager.getMapCenterPlaceId());
                                if (placeInfo == null) {
                                    if (shouldMoveToHero) {
                                        shouldMoveToHero = false;
                                        moveToTile((int) Math.round(heroPosition.x), (int) Math.round(heroPosition.y),
                                                mapViewHelper.getMediumScale());
                                    } else {
                                        mapViewHelper.setScale(mapViewHelper.getMediumScale());
                                        mapViewHelper.onDrag(mapShiftX, mapShiftY);
                                    }
                                } else {
                                    PreferencesManager.setMapCenterPlaceId(-1);
                                    moveToTile(placeInfo.x, placeInfo.y, mapViewHelper.getMediumScale());
                                }
                            }

                            UiUtils.removeGlobalLayoutListener(mapView, this);
                        }
                    }
                });

                mapViewHelper.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                    @Override
                    public void onPhotoTap(View view, float x, float y) {
                        final int tileX = (int) Math.floor(x * mapTileSize.x);
                        final int tileY = (int) Math.floor(y * mapTileSize.y);

                        DialogUtils.showTabbedDialog(getChildFragmentManager(), getString(R.string.drawer_title_map), null);

                        RequestExecutor.execute(
                                getActivity(),
                                new MapCellRequestBuilder().setX(tileX).setY(tileY),
                                RequestUtils.wrapCallback(new ApiCallback<MapCellResponse>() {
                                    @Override
                                    public void onSuccess(final MapCellResponse mapCellResponse) {
                                        final TabbedDialog.TabbedDialogTabsAdapter adapter;
                                        switch(mapCellResponse.type) {
                                            case PLACE:
                                                RequestExecutor.execute(
                                                        getActivity(),
                                                        new PlacesRequestBuilder(),
                                                        RequestUtils.wrapCallback(new ApiCallback<PlacesResponse>() {
                                                            @Override
                                                            public void onSuccess(PlacesResponse placesResponse) {
                                                                for(final PlaceInfo placeInfo : placesResponse.places.values()) {
                                                                    if((placeInfo.x == tileX) && (placeInfo.y == tileY)) {
                                                                        RequestExecutor.execute(
                                                                                getActivity(),
                                                                                new PlaceRequestBuilder().setPlaceId(placeInfo.id),
                                                                                RequestUtils.wrapCallback(new ApiCallback<PlaceResponse>() {
                                                                                    @Override
                                                                                    public void onSuccess(PlaceResponse placeResponse) {
                                                                                        initTabbedDialog(placeResponse.name,
                                                                                                new MapTilePlaceTabsAdapter(getActivity(), placeResponse, mapCellResponse));
                                                                                    }

                                                                                    @Override
                                                                                    public void onError(AbstractApiResponse response) {
                                                                                        dismissTabbedDialog();
                                                                                        setError(getString(R.string.map_error));
                                                                                    }
                                                                                }, MapFragment.this));
                                                                        return;
                                                                    }
                                                                }
                                                                dismissTabbedDialog();
                                                                setError(getString(R.string.map_error));
                                                            }

                                                            @Override
                                                            public void onError(AbstractApiResponse response) {
                                                                dismissTabbedDialog();
                                                                setError(getString(R.string.map_error));
                                                            }
                                                        }, MapFragment.this));
                                                return;

                                            case BUILDING:
                                                adapter = new MapTileBuildingTabsAdapter(getActivity(), mapCellResponse);
                                                break;

                                            case TERRAIN:
                                                adapter = new MapTileTerrainTabsAdapter(getActivity(), mapCellResponse);
                                                break;

                                            default:
                                                dismissTabbedDialog();
                                                setError(getString(R.string.map_error));
                                                return;
                                        }
                                        initTabbedDialog(mapCellResponse.title == null
                                                ? getString(R.string.map_tile_caption, tileX, tileY)
                                                : mapCellResponse.title, adapter);
                                    }

                                    @Override
                                    public void onError(AbstractApiResponse response) {
                                        dismissTabbedDialog();
                                        setError(getString(R.string.map_error));
                                    }
                                }, MapFragment.this));
                    }
                });

                places = new ArrayList<>(mapResponse.places.size());
                for (final MapPlaceInfo placeInfo : mapResponse.places.values()) {
                    places.add(placeInfo);
                }
                Collections.sort(places, new Comparator<MapPlaceInfo>() {
                    @Override
                    public int compare(MapPlaceInfo lhs, MapPlaceInfo rhs) {
                        return lhs.name.compareTo(rhs.name);
                    }
                });

                shouldShowMenuOptions = true;
                if(menuOptions != null) {
                    menuOptions.setVisible(true);
                }

                setMode(DataViewMode.DATA);
            }
        });
    }

    // requests may be completed before dialog is instantiated, we'll wait for it
    private void initTabbedDialog(final CharSequence caption,
                                  final TabbedDialog.TabbedDialogTabsAdapter tabsAdapter) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                final TabbedDialog dialog = (TabbedDialog) getChildFragmentManager().findFragmentByTag(DialogUtils.DIALOG_TABBED_TAG);
                if (dialog == null) {
                    handler.post(this);
                } else {
                    dialog.setCaption(caption);
                    dialog.setTabsAdapter(tabsAdapter);
                    dialog.setMode(DataViewMode.DATA);
                }
            }
        });
    }

    private void dismissTabbedDialog() {
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
    }

}
