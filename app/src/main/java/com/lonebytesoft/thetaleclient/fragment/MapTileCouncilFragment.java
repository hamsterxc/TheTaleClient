package com.lonebytesoft.thetaleclient.fragment;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.api.model.PlaceCouncilMember;
import com.lonebytesoft.thetaleclient.api.response.MapCellResponse;
import com.lonebytesoft.thetaleclient.util.UiUtils;

/**
 * @author Hamster
 * @since 26.12.2014
 */
public class MapTileCouncilFragment extends MapTileFragment {

    public static MapTileCouncilFragment newInstance(final MapCellResponse cellInfo) {
        final MapTileCouncilFragment dialog = new MapTileCouncilFragment();
        setupParams(dialog, cellInfo);
        return dialog;
    }

    @Override
    protected void setupContent(final LayoutInflater layoutInflater, final ViewGroup container) {
        final View content = layoutInflater.inflate(R.layout.fragment_map_tile_tab_content_council, container, true);
        final ViewGroup councilContainer = (ViewGroup) content.findViewById(R.id.map_tile_tab_content_council);

        final MapCellResponse cellInfo = getArguments().getParcelable(PARAM_CELL_INFO);
        boolean first = true;
        for(final PlaceCouncilMember councilMember : cellInfo.council) {
            if(first) {
                first = false;
            } else {
                final View delimiter = layoutInflater.inflate(R.layout.item_council_member_delimiter, councilContainer, false);
                councilContainer.addView(delimiter);
            }

            final View councilMemberView = layoutInflater.inflate(R.layout.item_council_member, councilContainer, false);
            ((ImageView) councilMemberView.findViewById(R.id.item_council_member_icon))
                    .setImageResource(councilMember.profession.getIconResId());

            final SpannableStringBuilder stringBuilder = new SpannableStringBuilder();

            final Spannable name = new SpannableString(councilMember.name);
            name.setSpan(new StyleSpan(Typeface.BOLD), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            stringBuilder
                    .append(name)
                    .append(", ")
                    .append(councilMember.gender.getName())
                    .append("-")
                    .append(councilMember.race.getName())
                    .append(", ")
                    .append(councilMember.profession.getName())
                    .append("-")
                    .append(councilMember.proficiencyLevel.getCode())
                    .append("\n");

            final Spannable power = new SpannableString(getString(R.string.map_council_member_power));
            power.setSpan(new StyleSpan(Typeface.BOLD), 0, power.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            final Spannable powerBonusPositive = new SpannableString(String.format("+%.2f%%", councilMember.powerBonusPositive));
            powerBonusPositive.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.common_positive)),
                    0, powerBonusPositive.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            final Spannable powerBonusNegative = new SpannableString(String.format("-%.2f%%", councilMember.powerBonusNegative));
            powerBonusNegative.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.common_negative)),
                    0, powerBonusNegative.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            stringBuilder
                    .append(power)
                    .append(": ")
                    .append(String.valueOf(councilMember.power))
                    .append("% (")
                    .append(powerBonusPositive)
                    .append(", ")
                    .append(powerBonusNegative)
                    .append(")\n");

            stringBuilder
                    .append(UiUtils.getQuantityString(
                            getActivity(),
                            R.string.map_council_member_friends_one,
                            R.string.map_council_member_friends_few,
                            R.string.map_council_member_friends_many,
                            councilMember.friends))
                    .append(" / ")
                    .append(UiUtils.getQuantityString(
                            getActivity(),
                            R.string.map_council_member_enemies_one,
                            R.string.map_council_member_enemies_few,
                            R.string.map_council_member_enemies_many,
                            councilMember.enemies));

            UiUtils.setText(councilMemberView.findViewById(R.id.item_council_member_info), stringBuilder);

            councilContainer.addView(councilMemberView);
        }
    }

}
