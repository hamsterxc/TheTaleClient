package com.lonebytesoft.thetaleclient.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.lonebytesoft.thetaleclient.api.model.ArtifactInfo;
import com.lonebytesoft.thetaleclient.api.model.MightInfo;
import com.lonebytesoft.thetaleclient.api.model.QuestActorInfo;
import com.lonebytesoft.thetaleclient.fragment.dialog.ArtifactDialog;
import com.lonebytesoft.thetaleclient.fragment.dialog.MightDialog;
import com.lonebytesoft.thetaleclient.fragment.dialog.QuestActorDialog;

/**
 * @author Hamster
 * @since 09.10.2014
 */
public class DialogUtils {

    private static final String DIALOG_MIGHT_TAG = "DIALOG_MIGHT_TAG";
    private static final String DIALOG_ARTIFACT_TAG = "DIALOG_ARTIFACT_TAG";
    private static final String DIALOG_QUEST_ACTOR_TAG = "DIALOG_QUEST_ACTOR_TAG";

    private static FragmentTransaction getFragmentTransaction(final FragmentManager fragmentManager) {
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final Fragment previous = fragmentManager.findFragmentByTag(DIALOG_MIGHT_TAG);
        if(previous != null) {
            fragmentTransaction.remove(previous);
        }
        fragmentTransaction.addToBackStack(null);
        return fragmentTransaction;
    }

    public static void showMightDialog(final FragmentManager fragmentManager, final MightInfo mightInfo) {
        MightDialog.newInstance(mightInfo).show(getFragmentTransaction(fragmentManager), DIALOG_MIGHT_TAG);
    }

    public static void showArtifactDialog(final FragmentManager fragmentManager, final ArtifactInfo artifactInfo) {
        ArtifactDialog.newInstance(artifactInfo).show(getFragmentTransaction(fragmentManager), DIALOG_ARTIFACT_TAG);
    }

    public static void showQuestActorDialog(final FragmentManager fragmentManager, final QuestActorInfo questActorInfo) {
        QuestActorDialog.newInstance(questActorInfo).show(fragmentManager, DIALOG_QUEST_ACTOR_TAG);
    }

}
