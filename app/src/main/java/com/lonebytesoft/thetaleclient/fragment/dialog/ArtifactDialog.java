package com.lonebytesoft.thetaleclient.fragment.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.api.dictionary.ArtifactType;
import com.lonebytesoft.thetaleclient.api.model.ArtifactInfo;
import com.lonebytesoft.thetaleclient.util.UiUtils;

/**
 * @author Hamster
 * @since 09.10.2014
 */
public class ArtifactDialog extends BaseDialog {

    private static final String PARAM_ARTIFACT_INFO = "PARAM_ARTIFACT_INFO";

    public static ArtifactDialog newInstance(final ArtifactInfo artifactInfo) {
        final ArtifactDialog dialog = new ArtifactDialog();

        Bundle args = new Bundle();
        args.putParcelable(PARAM_ARTIFACT_INFO, artifactInfo);

        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.dialog_content_artifact, container, false);
        final ArtifactInfo artifactInfo = getArguments().getParcelable(PARAM_ARTIFACT_INFO);

        final TextView textArtifactType = (TextView) view.findViewById(R.id.dialog_artifact_type);
        UiUtils.setText(textArtifactType, artifactInfo.type == ArtifactType.JUNK ? artifactInfo.type.getName() : artifactInfo.rarity.getName());
        textArtifactType.setTextColor(getResources().getColor(artifactInfo.rarity.getColorResId()));

        if(artifactInfo.type != ArtifactType.JUNK) {
            UiUtils.setText(view.findViewById(R.id.dialog_artifact_equipment_type),
                    getString(R.string.artifact_equipment_type, artifactInfo.type.getName()));
            UiUtils.setText(view.findViewById(R.id.dialog_artifact_power_physical),
                    getString(R.string.artifact_power_physical, artifactInfo.powerPhysical));
            UiUtils.setText(view.findViewById(R.id.dialog_artifact_power_magical),
                    getString(R.string.artifact_power_magical, artifactInfo.powerMagical));
            UiUtils.setText(view.findViewById(R.id.dialog_artifact_integrity),
                    getString(R.string.artifact_integrity, 100.0 * artifactInfo.integrityCurrent / artifactInfo.integrityTotal, artifactInfo.integrityCurrent, artifactInfo.integrityTotal));
            UiUtils.setText(view.findViewById(R.id.dialog_artifact_rating),
                    getString(R.string.artifact_rating, artifactInfo.rating));
            UiUtils.setText(view.findViewById(R.id.dialog_artifact_effect),
                    artifactInfo.effect.getDescription());
        }

        return wrapView(inflater, view, artifactInfo.name);
    }

}
