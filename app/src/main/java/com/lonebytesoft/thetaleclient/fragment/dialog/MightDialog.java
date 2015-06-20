package com.lonebytesoft.thetaleclient.fragment.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.api.model.MightInfo;
import com.lonebytesoft.thetaleclient.util.UiUtils;

/**
 * @author Hamster
 * @since 09.10.2014
 */
public class MightDialog extends BaseDialog {

    private static final String PARAM_MIGHT_INFO = "PARAM_MIGHT_INFO";

    public static MightDialog newInstance(final MightInfo mightInfo) {
        final MightDialog dialog = new MightDialog();

        final Bundle args = new Bundle();
        args.putParcelable(PARAM_MIGHT_INFO, mightInfo);

        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_content_might, container, false);

        final MightInfo mightInfo = getArguments().getParcelable(PARAM_MIGHT_INFO);
        if(mightInfo == null) {
            dismiss();
            return null;
        }

        UiUtils.setText(view.findViewById(R.id.dialog_might_critical_chance),
                getString(R.string.game_might_help_critical_chance, mightInfo.helpCriticalChance * 100.0));
        UiUtils.setText(view.findViewById(R.id.dialog_might_pvp_effectiveness_bonus),
                getString(R.string.game_might_pvp_effectiveness_bonus, mightInfo.pvpEffectivenessBonus * 100.0));
        UiUtils.setText(view.findViewById(R.id.dialog_might_politics_power),
                getString(R.string.game_might_politics_power, mightInfo.politicsPower * 100.0));

        return wrapView(inflater, view, getString(R.string.game_title_might));
    }

}
