package com.lonebytesoft.thetaleclient.fragment;

import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.api.dictionary.CompanionFeature;
import com.lonebytesoft.thetaleclient.api.model.CompanionInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author Hamster
 * @since 18.02.2015
 */
public class CompanionFeaturesFragment extends TabbedDialogTabFragment {

    private static final String PARAM_COMPANION_INFO = "PARAM_COMPANION_INFO";

    public static CompanionFeaturesFragment newInstance(final CompanionInfo companion) {
        final CompanionFeaturesFragment dialog = new CompanionFeaturesFragment();

        final Bundle args = new Bundle();
        args.putParcelable(PARAM_COMPANION_INFO, companion);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    protected void setupContent(LayoutInflater layoutInflater, ViewGroup container) {
        final View content = layoutInflater.inflate(R.layout.fragment_companion_feature_content, container, true);
        final ViewGroup featuresContainer = (ViewGroup) content.findViewById(R.id.companion_feature_content);

        final CompanionInfo companion = getArguments().getParcelable(PARAM_COMPANION_INFO);
        final List<Pair<Integer, CompanionFeature>> features = new ArrayList<>(companion.species.features.size());
        for(final Map.Entry<CompanionFeature, Integer> feature : companion.species.features.entrySet()) {
            features.add(Pair.create(feature.getValue(), feature.getKey()));
        }
        Collections.sort(features, new Comparator<Pair<Integer, CompanionFeature>>() {
            @Override
            public int compare(Pair<Integer, CompanionFeature> lhs, Pair<Integer, CompanionFeature> rhs) {
                if(lhs.first.equals(rhs.first)) {
                    return lhs.second.getName().compareTo(rhs.second.getName());
                } else {
                    return lhs.first - rhs.first;
                }
            }
        });

        int previous = -1;
        for(final Pair<Integer, CompanionFeature> feature : features) {
            if(previous != feature.first) {
                previous = feature.first;
                final View block = layoutInflater.inflate(R.layout.item_companion_feature_block, featuresContainer, false);
                ((TextView) block.findViewById(R.id.companion_feature_block_text)).setText(
                        previous == 0
                                ? getString(R.string.game_companion_feature_initial)
                                : getString(R.string.game_companion_feature_coherence, previous));
                featuresContainer.addView(block);
            }

            final View item = layoutInflater.inflate(R.layout.item_companion_feature_item, featuresContainer, false);
            ((TextView) item.findViewById(R.id.companion_feature_item_name)).setText(feature.second.getName());
            ((TextView) item.findViewById(R.id.companion_feature_item_description)).setText(feature.second.getDescription());
            featuresContainer.addView(item);
        }
    }

}
