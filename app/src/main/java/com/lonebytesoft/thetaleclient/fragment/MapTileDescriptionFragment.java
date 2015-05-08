package com.lonebytesoft.thetaleclient.fragment;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.util.UiUtils;

/**
 * @author Hamster
 * @since 18.12.2014
 */
public class MapTileDescriptionFragment extends TabbedDialogTabFragment {

    private static final String PARAM_DESCRIPTION = "PARAM_DESCRIPTION";

    public static MapTileDescriptionFragment newInstance(final String description) {
        final MapTileDescriptionFragment dialog = new MapTileDescriptionFragment();

        final Bundle args = new Bundle();
        args.putString(PARAM_DESCRIPTION, description);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    protected void setupContent(final LayoutInflater layoutInflater, final ViewGroup container) {
        final View content = layoutInflater.inflate(R.layout.tabbed_dialog_tab_content_text, container, true);
        final TextView text = (TextView) content.findViewById(R.id.tabbed_dialog_tab_content_text);

        UiUtils.setText(text, Html.fromHtml(String.valueOf(getArguments().getString(PARAM_DESCRIPTION))));
        text.setMovementMethod(LinkMovementMethod.getInstance());
    }

}
