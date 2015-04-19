package com.lonebytesoft.thetaleclient.fragment;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.apisdk.model.CompanionInfo;
import com.lonebytesoft.thetaleclient.util.UiUtils;

/**
 * @author Hamster
 * @since 18.02.2015
 */
public class CompanionParamsFragment extends TabbedDialogTabFragment {

    private static final String PARAM_COMPANION_INFO = "PARAM_COMPANION_INFO";

    public static CompanionParamsFragment newInstance(final CompanionInfo companion) {
        final CompanionParamsFragment dialog = new CompanionParamsFragment();

        final Bundle args = new Bundle();
        args.putParcelable(PARAM_COMPANION_INFO, companion);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    protected void setupContent(LayoutInflater layoutInflater, ViewGroup container) {
        final View content = layoutInflater.inflate(R.layout.tabbed_dialog_tab_content_text, container, true);
        final TextView text = (TextView) content.findViewById(R.id.tabbed_dialog_tab_content_text);

        final CompanionInfo companion = getArguments().getParcelable(PARAM_COMPANION_INFO);
        final SpannableStringBuilder info = new SpannableStringBuilder();
        info.append(UiUtils.getInfoItem(
                getString(R.string.game_companion_param_rarity),
                companion.species.rarity.name))
                .append("\n");
        info.append(UiUtils.getInfoItem(
                getString(R.string.game_companion_param_type),
                companion.species.type.name))
                .append("\n");
        info.append(UiUtils.getInfoItem(
                getString(R.string.game_companion_param_archetype),
                companion.species.archetype.name))
                .append("\n");
        info.append(UiUtils.getInfoItem(
                getString(R.string.game_companion_param_dedication),
                companion.species.dedication.name))
                .append("\n");
        info.append(UiUtils.getInfoItem(
                getString(R.string.game_companion_param_health),
                String.valueOf(companion.species.health)));
        UiUtils.setText(text, info);
    }

}
