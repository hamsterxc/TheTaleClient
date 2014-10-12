package com.lonebytesoft.thetaleclient.fragment.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.api.model.MightInfo;

/**
 * @author Hamster
 * @since 09.10.2014
 */
public class MightDialog extends BaseDialog {

    private static final String PARAM_HELP_CRITICAL_CHANCE = "PARAM_HELP_CRITICAL_CHANCE";
    private static final String PARAM_PVP_EFFECTIVENESS_BONUS = "PARAM_PVP_EFFECTIVENESS_BONUS";

    public static MightDialog newInstance(final MightInfo mightInfo) {
        MightDialog dialog = new MightDialog();

        Bundle args = new Bundle();
        args.putDouble(PARAM_HELP_CRITICAL_CHANCE, mightInfo.helpCriticalChance);
        args.putDouble(PARAM_PVP_EFFECTIVENESS_BONUS, mightInfo.pvpEffectivenessBonus);

        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_content_might, container, false);

        ((TextView) view.findViewById(R.id.dialog_might_critical_chance)).setText(getString(
                R.string.game_might_help_critical_chance,
                getArguments().getDouble(PARAM_HELP_CRITICAL_CHANCE, 0) * 100.0));
        ((TextView) view.findViewById(R.id.dialog_might_pvp_effectiveness_bonus)).setText(getString(
                R.string.game_might_pvp_effectiveness_bonus,
                getArguments().getDouble(PARAM_PVP_EFFECTIVENESS_BONUS, 0) * 100.0));

        return wrapView(inflater, view, getString(R.string.game_title_might));
    }

}
