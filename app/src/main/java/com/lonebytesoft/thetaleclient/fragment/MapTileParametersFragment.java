package com.lonebytesoft.thetaleclient.fragment;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.util.UiUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Hamster
 * @since 15.10.2014
 */
public class MapTileParametersFragment extends TabbedDialogTabFragment {

    private static final String PARAM_PARAMETER_KEYS = "PARAM_PARAMETER_KEYS";
    private static final String PARAM_PARAMETER_VALUES = "PARAM_PARAMETER_VALUES";

    public static MapTileParametersFragment newInstance(final Map<String, String> parameters) {
        final MapTileParametersFragment dialog = new MapTileParametersFragment();

        final Bundle args = new Bundle();
        args.putStringArrayList(PARAM_PARAMETER_KEYS, new ArrayList<>(parameters.keySet()));
        args.putStringArrayList(PARAM_PARAMETER_VALUES, new ArrayList<>(parameters.values()));
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    protected void setupContent(final LayoutInflater layoutInflater, final ViewGroup container) {
        final View content = layoutInflater.inflate(R.layout.tabbed_dialog_tab_content_text, container, true);
        final TextView text = (TextView) content.findViewById(R.id.tabbed_dialog_tab_content_text);

        final List<String> parameterKeys = getArguments().getStringArrayList(PARAM_PARAMETER_KEYS);
        final List<String> parameterValues = getArguments().getStringArrayList(PARAM_PARAMETER_VALUES);
        final int count = Math.min(parameterKeys.size(), parameterValues.size());
        final SpannableStringBuilder info = new SpannableStringBuilder();
        for(int i = 0; i < count; i++) {
            if(i != 0) {
                info.append("\n");
            }
            info.append(UiUtils.getInfoItem(parameterKeys.get(i), parameterValues.get(i)));
        }
        UiUtils.setText(text, info);
    }

}
