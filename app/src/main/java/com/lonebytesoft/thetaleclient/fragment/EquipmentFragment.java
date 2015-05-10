package com.lonebytesoft.thetaleclient.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
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
import com.lonebytesoft.thetaleclient.apisdk.ApiCallback;
import com.lonebytesoft.thetaleclient.apisdk.RequestExecutor;
import com.lonebytesoft.thetaleclient.apisdk.model.ArtifactInfoParcelable;
import com.lonebytesoft.thetaleclient.apisdk.prerequisite.InfoPrerequisiteRequest;
import com.lonebytesoft.thetaleclient.apisdk.request.GameInfoRequestBuilder;
import com.lonebytesoft.thetaleclient.apisdk.request.PerformActionRequestBuilder;
import com.lonebytesoft.thetaleclient.apisdk.util.DictionaryData;
import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.dictionary.Action;
import com.lonebytesoft.thetaleclient.sdk.dictionary.ArtifactEffect;
import com.lonebytesoft.thetaleclient.sdk.dictionary.ArtifactRarity;
import com.lonebytesoft.thetaleclient.sdk.dictionary.ArtifactType;
import com.lonebytesoft.thetaleclient.sdk.dictionary.EquipmentType;
import com.lonebytesoft.thetaleclient.sdk.model.ArtifactInfo;
import com.lonebytesoft.thetaleclient.sdk.response.CommonResponse;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.sdk.response.InfoResponse;
import com.lonebytesoft.thetaleclient.util.DialogUtils;
import com.lonebytesoft.thetaleclient.util.GameInfoUtils;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.RequestUtils;
import com.lonebytesoft.thetaleclient.util.UiUtils;
import com.lonebytesoft.thetaleclient.widget.RequestActionView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author Hamster
 * @since 06.10.2014
 */
public class EquipmentFragment extends WrapperFragment {

    private LayoutInflater layoutInflater;

    private View rootView;

    private View equipmentEffects;
    private ViewGroup equipmentContainer;
    private TextView bagCaption;
    private ViewGroup bagContainer;

    public EquipmentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        rootView = layoutInflater.inflate(R.layout.fragment_equipment, container, false);

        equipmentEffects = rootView.findViewById(R.id.equipment_effects);
        equipmentContainer = (ViewGroup) rootView.findViewById(R.id.equipment_container);
        bagCaption = (TextView) rootView.findViewById(R.id.bag_caption);
        bagContainer = (ViewGroup) rootView.findViewById(R.id.bag_container);

        return wrapView(layoutInflater, rootView);
    }

    @Override
    public void refresh(final boolean showLoading) {
        super.refresh(showLoading);

        GameInfoRequestBuilder.executeWatching(getActivity(), RequestUtils.wrapCallback(new ApiCallback<GameInfoResponse>() {
            @Override
            public void onSuccess(final GameInfoResponse response) {
                UiUtils.updateGlobalInfo(EquipmentFragment.this, response);

                equipmentContainer.removeAllViews();
                final List<ArtifactEffect> equipmentEffectsList = new ArrayList<>();
                for(final EquipmentType equipmentType : EquipmentType.values()) {
                    final View equipmentEntryView = layoutInflater.inflate(R.layout.item_equipment, equipmentContainer, false);
                    final ArtifactInfo artifactInfo = response.account.hero.equipment.get(equipmentType);

                    final ImageView imageIcon = (ImageView) equipmentEntryView.findViewById(R.id.equipment_icon);
                    final TextView textName = (TextView) equipmentEntryView.findViewById(R.id.equipment_name);
                    final TextView textPower = (TextView) equipmentEntryView.findViewById(R.id.equipment_power);

                    imageIcon.setImageResource(DictionaryData.getEquipmentTypeDrawableId(equipmentType));

                    if(artifactInfo == null) {
                        textName.setVisibility(View.GONE);
                        textPower.setVisibility(View.GONE);
                    } else {
                        final Spanned[] artifactStrings = getArtifactString(artifactInfo, true, 1);
                        textName.setText(artifactStrings[0]);
                        textPower.setText(artifactStrings[1]);

                        if(artifactInfo.effect != ArtifactEffect.NO_EFFECT) {
                            equipmentEffectsList.add(artifactInfo.effect);
                        }
                        if(artifactInfo.effectSpecial != ArtifactEffect.NO_EFFECT) {
                            equipmentEffectsList.add(artifactInfo.effectSpecial);
                        }

                        try {
                            final ArtifactInfoParcelable artifactInfoParcelable = new ArtifactInfoParcelable(artifactInfo);
                            equipmentEntryView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DialogUtils.showArtifactDialog(getFragmentManager(), artifactInfoParcelable);
                                }
                            });
                        } catch (JSONException ignored) {
                        }
                    }

                    equipmentContainer.addView(equipmentEntryView);
                }

                if(equipmentEffectsList.size() == 0) {
                    equipmentEffects.setVisibility(View.GONE);
                } else {
                    final Map<ArtifactEffect, Integer> effects = ObjectUtils.getItemsCountList(
                            equipmentEffectsList,
                            new Comparator<ArtifactEffect>() {
                                @Override
                                public int compare(ArtifactEffect lhs, ArtifactEffect rhs) {
                                    return lhs.name.compareTo(rhs.name);
                                }
                            });
                    final SpannableStringBuilder effectsStringBuilder = new SpannableStringBuilder();
                    boolean first = true;
                    for(final Map.Entry<ArtifactEffect, Integer> entry : effects.entrySet()) {
                        if(first) {
                            first = false;
                        } else {
                            effectsStringBuilder.append("\n");
                        }
                        final ArtifactEffect effect = entry.getKey();
                        effectsStringBuilder.append(UiUtils.getInfoItem(effect.name, effect.description));
                        if(entry.getValue() != 1) {
                            effectsStringBuilder.append(getString(R.string.common_item_count, entry.getValue()));
                        }
                    }
                    equipmentEffects.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogUtils.showMessageDialog(getFragmentManager(),
                                    getString(R.string.game_bag_effects_title), effectsStringBuilder);
                        }
                    });
                    equipmentEffects.setVisibility(View.VISIBLE);
                }

                bagCaption.setText(getString(R.string.game_title_bag,
                        response.account.hero.basicInfo.bagItemsCount,
                        response.account.hero.basicInfo.bagCapacity));

                bagContainer.removeAllViews();

                if(response.account.isOwnInfo) {
                    final View dropView = layoutInflater.inflate(R.layout.item_bag_drop, bagContainer, false);
                    final RequestActionView dropActionView = (RequestActionView) dropView.findViewById(R.id.bag_drop);
                    if(response.account.hero.basicInfo.bagItemsCount > 0) {
                        RequestExecutor.executeOptional(getActivity(), new InfoPrerequisiteRequest(), RequestUtils.wrapCallback(new ApiCallback<InfoResponse>() {
                            @Override
                            public void onSuccess(InfoResponse infoResponse) {
                                if (GameInfoUtils.isEnoughEnergy(response.account.hero.energy, PreferencesManager.getAbilityCost(Action.DROP_ITEM))) {
                                    dropActionView.setEnabled(true);
                                    dropActionView.setActionClickListener(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (PreferencesManager.isConfirmationBagDropEnabled()) {
                                                DialogUtils.showConfirmationDialog(
                                                        getChildFragmentManager(),
                                                        getString(R.string.game_bag_drop_item),
                                                        getString(R.string.game_bag_drop_item_confirmation),
                                                        new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                dropItem(dropActionView);
                                                            }
                                                        });
                                            } else {
                                                dropItem(dropActionView);
                                            }
                                        }
                                    });
                                } else {
                                    dropActionView.setEnabled(false);
                                }
                            }

                            @Override
                            public void onError(AbstractApiResponse response) {
                                dropActionView.setErrorText(response.errorMessage);
                            }
                        }, EquipmentFragment.this));
                    } else {
                        dropActionView.setEnabled(false);
                    }
                    bagContainer.addView(dropView);
                }

                final Map<ArtifactInfo, Integer> bagItemsList = ObjectUtils.getItemsCountList(
                        response.account.hero.bag.values(),
                        new Comparator<ArtifactInfo>() {
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
                        });
                for(final Map.Entry<ArtifactInfo, Integer> bagEntry : bagItemsList.entrySet()) {
                    final View bagEntryView = layoutInflater.inflate(R.layout.item_bag, bagContainer, false);
                    final ArtifactInfo artifactInfo = bagEntry.getKey();
                    ((TextView) bagEntryView.findViewById(R.id.bag_item_name)).setText(getArtifactString(artifactInfo, false, bagEntry.getValue())[0]);

                    try {
                        final ArtifactInfoParcelable artifactInfoParcelable = new ArtifactInfoParcelable(artifactInfo);
                        bagEntryView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DialogUtils.showArtifactDialog(getFragmentManager(), artifactInfoParcelable);
                            }
                        });
                    } catch (JSONException ignored) {
                    }

                    bagContainer.addView(bagEntryView);
                }

                setMode(DataViewMode.DATA);
            }

            @Override
            public void onError(AbstractApiResponse response) {
                UiUtils.updateGlobalInfo(EquipmentFragment.this, null);
                setError(response.errorMessage);
            }
        }, this));
    }

    private Spanned[] getArtifactString(final ArtifactInfo artifactInfo, final boolean isEquipped, final int count) {
        final String countString = count == 1 ? "" : getString(R.string.common_item_count, count);

        final Spannable name = new SpannableString(artifactInfo.name);
        name.setSpan(new ForegroundColorSpan(getResources().getColor(DictionaryData.getArtifactRarityColorId(artifactInfo.rarity))),
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

            if(artifactInfo.rarity != ArtifactRarity.COMMON) {
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

    private void dropItem(final RequestActionView dropActionView) {
        dropActionView.setMode(RequestActionView.Mode.LOADING);
        RequestExecutor.execute(
                getActivity(),
                new PerformActionRequestBuilder().setAction(Action.DROP_ITEM),
                RequestUtils.wrapCallback(new ApiCallback<CommonResponse>() {
                    @Override
                    public void onSuccess(CommonResponse response) {
                        refresh(false);
                    }

                    @Override
                    public void onError(AbstractApiResponse response) {
                        dropActionView.setErrorText(response.errorMessage);
                    }
                }, this));
    }

}
