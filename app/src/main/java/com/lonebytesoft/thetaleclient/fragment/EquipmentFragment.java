package com.lonebytesoft.thetaleclient.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.DataViewMode;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.dictionary.Action;
import com.lonebytesoft.thetaleclient.api.dictionary.ArtifactType;
import com.lonebytesoft.thetaleclient.api.dictionary.EquipmentType;
import com.lonebytesoft.thetaleclient.api.model.ArtifactInfo;
import com.lonebytesoft.thetaleclient.api.request.AbilityUseRequest;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.request.InfoRequest;
import com.lonebytesoft.thetaleclient.api.response.CommonResponse;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.api.response.InfoResponse;
import com.lonebytesoft.thetaleclient.util.DialogUtils;
import com.lonebytesoft.thetaleclient.util.GameInfoUtils;
import com.lonebytesoft.thetaleclient.util.RequestUtils;
import com.lonebytesoft.thetaleclient.widget.RequestActionView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Hamster
 * @since 06.10.2014
 */
public class EquipmentFragment extends WrapperFragment {

    private LayoutInflater layoutInflater;

    private View rootView;

    private ViewGroup equipmentContainer;
    private TextView bagCaption;
    private ViewGroup bagContainer;

    public EquipmentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        rootView = layoutInflater.inflate(R.layout.fragment_equipment, container, false);

        equipmentContainer = (ViewGroup) rootView.findViewById(R.id.equipment_container);
        bagCaption = (TextView) rootView.findViewById(R.id.bag_caption);
        bagContainer = (ViewGroup) rootView.findViewById(R.id.bag_container);

        return wrapView(layoutInflater, rootView);
    }

    @Override
    public void refresh(final boolean showLoading) {
        super.refresh(showLoading);

        new GameInfoRequest(true).execute(RequestUtils.wrapCallback(new ApiResponseCallback<GameInfoResponse>() {
            @Override
            public void processResponse(GameInfoResponse response) {
                equipmentContainer.removeAllViews();
                for(final EquipmentType equipmentType : EquipmentType.values()) {
                    final View equipmentEntryView = layoutInflater.inflate(R.layout.item_equipment, equipmentContainer, false);
                    final ArtifactInfo artifactInfo = response.account.hero.equipment.get(equipmentType);

                    final ImageView imageIcon = (ImageView) equipmentEntryView.findViewById(R.id.equipment_icon);
                    final TextView textName = (TextView) equipmentEntryView.findViewById(R.id.equipment_name);
                    final TextView textPower = (TextView) equipmentEntryView.findViewById(R.id.equipment_power);

                    imageIcon.setImageResource(equipmentType.getDrawableResId());

                    if(artifactInfo == null) {
                        textName.setVisibility(View.GONE);
                        textPower.setVisibility(View.GONE);
                    } else {
                        final Spanned[] artifactStrings = getArtifactString(artifactInfo, true, 1);
                        textName.setText(artifactStrings[0]);
                        textPower.setText(artifactStrings[1]);

                        equipmentEntryView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtils.showArtifactDialog(getFragmentManager(), artifactInfo);
                            }
                        });
                    }

                    equipmentContainer.addView(equipmentEntryView);
                }

                bagCaption.setText(getString(R.string.game_title_bag,
                        response.account.hero.basicInfo.bagItemsCount,
                        response.account.hero.basicInfo.bagCapacity));

                bagContainer.removeAllViews();

                final View dropView = layoutInflater.inflate(R.layout.item_bag_drop, bagContainer, false);
                final RequestActionView dropActionView = (RequestActionView) dropView.findViewById(R.id.bag_drop);
                if(response.account.hero.basicInfo.bagItemsCount > 0) {
                    final GameInfoResponse gameInfoResponse = response;
                    new InfoRequest().execute(RequestUtils.wrapCallback(new ApiResponseCallback<InfoResponse>() {
                        @Override
                        public void processResponse(InfoResponse response) {
                            if(GameInfoUtils.isEnoughEnergy(gameInfoResponse.account.hero.energy, response.abilitiesCost.get(Action.DROP_ITEM))) {
                                dropActionView.setEnabled(true);
                                dropActionView.setActionClickListener(new Runnable() {
                                    @Override
                                    public void run() {
                                        new AbilityUseRequest(Action.DROP_ITEM).execute(0, RequestUtils.wrapCallback(new ApiResponseCallback<CommonResponse>() {
                                            @Override
                                            public void processResponse(CommonResponse response) {
                                                refresh(false);
                                            }

                                            @Override
                                            public void processError(CommonResponse response) {
                                                dropActionView.setErrorText(response.errorMessage);
                                            }
                                        }, EquipmentFragment.this));
                                    }
                                });
                            } else {
                                dropActionView.setEnabled(false);
                            }
                        }

                        @Override
                        public void processError(InfoResponse response) {
                            dropActionView.setErrorText(response.errorMessage);
                        }
                    }, EquipmentFragment.this));
                } else {
                    dropActionView.setEnabled(false);
                }
                bagContainer.addView(dropView);

                final Comparator<ArtifactInfo> bagArtifactComparator = new Comparator<ArtifactInfo>() {
                    @Override
                    public int compare(ArtifactInfo lhs, ArtifactInfo rhs) {
                        if(lhs.name.equals(rhs.name)) {
                            if(lhs.powerPhysical == rhs.powerPhysical) {
                                if(lhs.powerMagical == rhs.powerMagical) {
                                    if(lhs.type == ArtifactType.JUNK) {
                                        return rhs.type == ArtifactType.JUNK ? 0 : 1;
                                    } else {
                                        return rhs.type == ArtifactType.JUNK ? -1 : 0;
                                    }
                                } else {
                                    return lhs.powerMagical - rhs.powerMagical;
                                }
                            } else {
                                return lhs.powerPhysical - rhs.powerPhysical;
                            }
                        } else {
                            return lhs.name.compareTo(rhs.name);
                        }
                    }
                };
                final List<ArtifactInfo> bagItems = new ArrayList<>();
                bagItems.addAll(response.account.hero.bag.values());
                Collections.sort(bagItems, bagArtifactComparator);

                ArtifactInfo bagPrevious = null;
                final Map<ArtifactInfo, Integer> bagItemsList = new LinkedHashMap<>();
                for(final ArtifactInfo artifactInfo : bagItems) {
                    if((bagPrevious == null) || (bagArtifactComparator.compare(bagPrevious, artifactInfo) != 0)) {
                        bagPrevious = artifactInfo;
                        bagItemsList.put(artifactInfo, 1);
                    } else {
                        bagItemsList.put(bagPrevious, bagItemsList.get(bagPrevious) + 1);
                    }
                }

                for(final Map.Entry<ArtifactInfo, Integer> bagEntry : bagItemsList.entrySet()) {
                    final View bagEntryView = layoutInflater.inflate(R.layout.item_bag, bagContainer, false);
                    final ArtifactInfo artifactInfo = bagEntry.getKey();

                    ((TextView) bagEntryView.findViewById(R.id.bag_item_name)).setText(getArtifactString(artifactInfo, false, bagEntry.getValue())[0]);
                    bagEntryView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogUtils.showArtifactDialog(getFragmentManager(), artifactInfo);
                        }
                    });

                    bagContainer.addView(bagEntryView);
                }

                setMode(DataViewMode.DATA);
            }

            @Override
            public void processError(GameInfoResponse response) {
                setError(response.errorMessage);
            }
        }, this), true);
    }

    private Spanned[] getArtifactString(final ArtifactInfo artifactInfo, final boolean isEquipped, final int count) {
        final String countString = count == 1 ? "" : getString(R.string.game_bag_item_count, count);

        final Spannable name = new SpannableString(artifactInfo.name);
        name.setSpan(new ForegroundColorSpan(getResources().getColor(artifactInfo.rarity.getColorResId())),
                0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        if(artifactInfo.type == ArtifactType.JUNK) {
            return new Spanned[]{(Spanned) TextUtils.concat(name, countString)};
        } else {
            final Spannable powerPhysical = new SpannableString(String.valueOf(artifactInfo.powerPhysical));
            powerPhysical.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.artifact_power_physical)),
                    0, powerPhysical.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            final Spannable powerMagical = new SpannableString(String.valueOf(artifactInfo.powerMagical));
            powerMagical.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.artifact_power_magical)),
                    0, powerMagical.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            if(artifactInfo.rarity.isExceptional()) {
                name.setSpan(new StyleSpan(Typeface.BOLD), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                powerPhysical.setSpan(new StyleSpan(Typeface.BOLD), 0, powerPhysical.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                powerMagical.setSpan(new StyleSpan(Typeface.BOLD), 0, powerMagical.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if(isEquipped) {
                return new Spanned[]{name, (Spanned) TextUtils.concat(powerPhysical, " ", powerMagical)};
            } else {
                return new Spanned[]{(Spanned) TextUtils.concat(name, " ", powerPhysical, " ", powerMagical, countString)};
            }
        }

    }

}
