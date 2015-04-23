package com.lonebytesoft.thetaleclient.fragment;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.apisdk.model.CompanionInfoParcelable;
import com.lonebytesoft.thetaleclient.util.UiUtils;

/**
 * @author Hamster
 * @since 18.02.2015
 */
public class CompanionDescriptionFragment extends TabbedDialogTabFragment {

    private static final String PARAM_COMPANION_INFO = "PARAM_COMPANION_INFO";

    public static CompanionDescriptionFragment newInstance(final CompanionInfoParcelable companion) {
        final CompanionDescriptionFragment dialog = new CompanionDescriptionFragment();

        final Bundle args = new Bundle();
        args.putParcelable(PARAM_COMPANION_INFO, companion);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    protected void setupContent(LayoutInflater layoutInflater, ViewGroup container) {
        final View content = layoutInflater.inflate(R.layout.tabbed_dialog_tab_content_text, container, true);
        final TextView text = (TextView) content.findViewById(R.id.tabbed_dialog_tab_content_text);

        final CompanionInfoParcelable companion = getArguments().getParcelable(PARAM_COMPANION_INFO);
        UiUtils.setText(text, Html.fromHtml(String.valueOf(companion.species.description)));
    }

}
