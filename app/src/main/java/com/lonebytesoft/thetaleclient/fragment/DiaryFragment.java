package com.lonebytesoft.thetaleclient.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lonebytesoft.thetaleclient.DataViewMode;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.apisdk.ApiCallback;
import com.lonebytesoft.thetaleclient.apisdk.request.GameInfoRequestBuilder;
import com.lonebytesoft.thetaleclient.sdk.AbstractApiResponse;
import com.lonebytesoft.thetaleclient.sdk.model.DiaryEntry;
import com.lonebytesoft.thetaleclient.sdk.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.util.RequestUtils;
import com.lonebytesoft.thetaleclient.util.UiUtils;

/**
 * @author Hamster
 * @since 06.10.2014
 */
public class DiaryFragment extends WrapperFragment {

    private LayoutInflater layoutInflater;

    private View rootView;

    private ViewGroup diaryContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        rootView = inflater.inflate(R.layout.fragment_diary, container, false);

        diaryContainer = (ViewGroup) rootView.findViewById(R.id.diary_container);

        return wrapView(layoutInflater, rootView);
    }

    @Override
    public void refresh(final boolean isGlobal) {
        super.refresh(isGlobal);

        GameInfoRequestBuilder.executeWatching(getActivity(), RequestUtils.wrapCallback(new ApiCallback<GameInfoResponse>() {
            @Override
            public void onSuccess(GameInfoResponse response) {
                UiUtils.updateGlobalInfo(DiaryFragment.this, response);

                diaryContainer.removeAllViews();
                for(int i = response.account.hero.diary.size() - 1; i >= 0; i--) {
                    final DiaryEntry diaryEntry = response.account.hero.diary.get(i);
                    final View diaryEntryView = layoutInflater.inflate(R.layout.item_diary, diaryContainer, false);
                    UiUtils.setText(
                            diaryEntryView.findViewById(R.id.diary_place),
                            diaryEntry.place);
                    UiUtils.setText(
                            diaryEntryView.findViewById(R.id.diary_time),
                            String.format("%s %s", diaryEntry.time, diaryEntry.date));
                    UiUtils.setText(
                            diaryEntryView.findViewById(R.id.diary_text),
                            diaryEntry.text);
                    diaryContainer.addView(diaryEntryView);
                }
                setMode(DataViewMode.DATA);
            }

            @Override
            public void onError(AbstractApiResponse response) {
                UiUtils.updateGlobalInfo(DiaryFragment.this, null);
                setError(response.errorMessage);
            }
        }, this));
    }

}
