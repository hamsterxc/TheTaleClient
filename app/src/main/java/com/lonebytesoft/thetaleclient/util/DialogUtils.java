package com.lonebytesoft.thetaleclient.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.api.model.ArtifactInfo;
import com.lonebytesoft.thetaleclient.api.model.CardInfo;
import com.lonebytesoft.thetaleclient.api.model.QuestActorInfo;
import com.lonebytesoft.thetaleclient.fragment.dialog.AboutDialog;
import com.lonebytesoft.thetaleclient.fragment.dialog.ArtifactDialog;
import com.lonebytesoft.thetaleclient.fragment.dialog.CardInfoDialog;
import com.lonebytesoft.thetaleclient.fragment.dialog.CardUseDialog;
import com.lonebytesoft.thetaleclient.fragment.dialog.ChoiceDialog;
import com.lonebytesoft.thetaleclient.fragment.dialog.ConfirmationDialog;
import com.lonebytesoft.thetaleclient.fragment.dialog.MessageDialog;
import com.lonebytesoft.thetaleclient.fragment.dialog.MightDialog;
import com.lonebytesoft.thetaleclient.fragment.dialog.QuestActorDialog;
import com.lonebytesoft.thetaleclient.fragment.dialog.TabbedDialog;
import com.lonebytesoft.thetaleclient.sdk.model.MightInfo;

/**
 * @author Hamster
 * @since 09.10.2014
 */
public class DialogUtils {

    public static final String DIALOG_MIGHT_TAG = "DIALOG_MIGHT_TAG";
    public static final String DIALOG_ARTIFACT_TAG = "DIALOG_ARTIFACT_TAG";
    public static final String DIALOG_QUEST_ACTOR_TAG = "DIALOG_QUEST_ACTOR_TAG";
    public static final String DIALOG_CHOICE_TAG = "DIALOG_CHOICE_TAG";
    public static final String DIALOG_TABBED_TAG = "DIALOG_TABBED_TAG";
    public static final String DIALOG_MESSAGE_TAG = "DIALOG_MESSAGE_TAG";
    public static final String DIALOG_ABOUT_TAG = "DIALOG_ABOUT_TAG";
    public static final String DIALOG_CONFIRMATION_TAG = "DIALOG_CONFIRMATION_TAG";
    public static final String DIALOG_CARD_INFO_TAG = "DIALOG_CARD_INFO_TAG";
    public static final String DIALOG_CARD_USE_TAG = "DIALOG_CARD_USE_TAG";

    private static FragmentTransaction getFragmentTransaction(final FragmentManager fragmentManager, final String tag) {
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final Fragment previous = fragmentManager.findFragmentByTag(tag);
        if(previous != null) {
            fragmentTransaction.remove(previous);
        }
        fragmentTransaction.addToBackStack(null);
        return fragmentTransaction;
    }

    public static void showMightDialog(final FragmentManager fragmentManager, final MightInfo mightInfo) {
        MightDialog.newInstance(mightInfo)
                .show(getFragmentTransaction(fragmentManager, DIALOG_MIGHT_TAG), DIALOG_MIGHT_TAG);
    }

    public static void showArtifactDialog(final FragmentManager fragmentManager, final ArtifactInfo artifactInfo) {
        ArtifactDialog.newInstance(artifactInfo)
                .show(getFragmentTransaction(fragmentManager, DIALOG_ARTIFACT_TAG), DIALOG_ARTIFACT_TAG);
    }

    public static void showQuestActorDialog(final FragmentManager fragmentManager, final QuestActorInfo questActorInfo) {
        QuestActorDialog.newInstance(questActorInfo)
                .show(getFragmentTransaction(fragmentManager, DIALOG_QUEST_ACTOR_TAG), DIALOG_QUEST_ACTOR_TAG);
    }

    public static void showChoiceDialog(final FragmentManager fragmentManager, final String caption,
                                        final String[] choices, final ChoiceDialog.ItemChooseListener listener) {
        final ChoiceDialog dialog = ChoiceDialog.newInstance(caption, choices);
        dialog.setItemChooseListener(listener);
        dialog.show(getFragmentTransaction(fragmentManager, DIALOG_CHOICE_TAG), DIALOG_CHOICE_TAG);
    }

    public static void showChoiceDialog(final FragmentManager fragmentManager, final String caption,
                                        final String[] choices, final ChoiceDialog.ItemChooseListener listener,
                                        final int layoutResId, final int listViewResId) {
        final ChoiceDialog dialog = ChoiceDialog.newInstance(caption, choices);
        dialog.setItemChooseListener(listener);
        dialog.setLayout(layoutResId, listViewResId);
        dialog.show(getFragmentTransaction(fragmentManager, DIALOG_CHOICE_TAG), DIALOG_CHOICE_TAG);
    }

    public static void showTabbedDialog(final FragmentManager fragmentManager,
                                        final String caption, final TabbedDialog.TabbedDialogTabsAdapter tabsAdapter) {
        final TabbedDialog dialog = TabbedDialog.newInstance(caption);
        if(tabsAdapter != null) {
            dialog.setTabsAdapter(tabsAdapter);
        }
        dialog.show(getFragmentTransaction(fragmentManager, DIALOG_TABBED_TAG), DIALOG_TABBED_TAG);
    }

    public static void showMessageDialog(final FragmentManager fragmentManager, final String caption, final CharSequence message) {
        MessageDialog.newInstance(caption, message)
                .show(getFragmentTransaction(fragmentManager, DIALOG_MESSAGE_TAG), DIALOG_MESSAGE_TAG);
    }

    public static void showMessageDialog(final FragmentManager fragmentManager,
                                         final String caption, final String message,
                                         final Runnable onCloseListener) {
        showMessageDialog(fragmentManager, caption, message, onCloseListener, onCloseListener);
    }

    public static void showMessageDialog(final FragmentManager fragmentManager,
                                         final String caption, final String message,
                                         final Runnable onOkClickListener, final Runnable onDismissListener) {
        final MessageDialog dialog = MessageDialog.newInstance(caption, message);
        dialog.setOnOkClickListener(onOkClickListener);
        dialog.setOnDismissListener(onDismissListener);
        dialog.show(getFragmentTransaction(fragmentManager, DIALOG_MESSAGE_TAG), DIALOG_MESSAGE_TAG);
    }

    public static void showAboutDialog(final FragmentManager fragmentManager) {
        new AboutDialog().show(getFragmentTransaction(fragmentManager, DIALOG_ABOUT_TAG), DIALOG_ABOUT_TAG);
    }

    public static void showConfirmationDialog(final FragmentManager fragmentManager,
                                              final String caption, final CharSequence message,
                                              final String okCaption, final Runnable onOkListener,
                                              final String cancelCaption, final Runnable onCancelListener,
                                              final Runnable onDismissListener) {
        final ConfirmationDialog dialog = ConfirmationDialog.newInstance(caption, message);
        dialog.setupPositiveButton(okCaption, onOkListener);
        dialog.setupNegativeButton(cancelCaption, onCancelListener);
        dialog.setOnDismissListener(onDismissListener);
        dialog.show(getFragmentTransaction(fragmentManager, DIALOG_CONFIRMATION_TAG), DIALOG_CONFIRMATION_TAG);
    }

    public static void showConfirmationDialog(final FragmentManager fragmentManager,
                                              final String caption, final CharSequence message,
                                              final Runnable onOkListener) {
        showConfirmationDialog(fragmentManager, caption, message, null, onOkListener, null, null, null);
    }

    public static void showCommonErrorDialog(final FragmentManager fragmentManager, final Context context) {
        showMessageDialog(fragmentManager,
                context.getString(R.string.common_dialog_attention_title),
                context.getString(R.string.common_error));
    }

    public static void showCardInfoDialog(final FragmentManager fragmentManager,
                                          final String caption, final CardInfo card,
                                          final Runnable onDismissListener) {
        final CardInfoDialog cardInfoDialog = CardInfoDialog.newInstance(caption, card);
        cardInfoDialog.setOnDismissListener(onDismissListener);
        cardInfoDialog.show(getFragmentTransaction(fragmentManager, DIALOG_CARD_INFO_TAG), DIALOG_CARD_INFO_TAG);
    }

    public static void showCardUseDialog(final FragmentManager fragmentManager,
                                         final String caption, final CardInfo card,
                                         final Runnable onSuccessListener) {
        final CardUseDialog dialog = CardUseDialog.newInstance(caption, card);
        dialog.setOnSuccessListener(onSuccessListener);
        dialog.show(getFragmentTransaction(fragmentManager, DIALOG_CARD_USE_TAG), DIALOG_CARD_USE_TAG);
    }

}
