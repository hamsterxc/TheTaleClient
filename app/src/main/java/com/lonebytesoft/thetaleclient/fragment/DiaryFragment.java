package com.lonebytesoft.thetaleclient.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.DataViewMode;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.model.DiaryEntry;
import com.lonebytesoft.thetaleclient.api.request.GameInfoRequest;
import com.lonebytesoft.thetaleclient.api.response.GameInfoResponse;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.RequestUtils;

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

        final ApiResponseCallback<GameInfoResponse> callback = RequestUtils.wrapCallback(new ApiResponseCallback<GameInfoResponse>() {
            @Override
            public void processResponse(GameInfoResponse response) {
                diaryContainer.removeAllViews();
                for(int i = response.account.hero.diary.size() - 1; i > 0; i--) {
                    final DiaryEntry diaryEntry = response.account.hero.diary.get(i);
                    final View diaryEntryView = layoutInflater.inflate(R.layout.item_diary, diaryContainer, false);
                    ((TextView) diaryEntryView.findViewById(R.id.diary_time)).setText(
                            String.format("%s %s", diaryEntry.time, diaryEntry.date));
                    ((TextView) diaryEntryView.findViewById(R.id.diary_text)).setText(diaryEntry.text);
                    diaryContainer.addView(diaryEntryView);
                }
                setMode(DataViewMode.DATA);
            }

            @Override
            public void processError(GameInfoResponse response) {
                setError(response.errorMessage);
            }
        }, this);

        final int watchingAccountId = PreferencesManager.getWatchingAccountId();
        if(watchingAccountId == 0) {
            new GameInfoRequest(true).execute(callback, true);
        } else {
            new GameInfoRequest(true).execute(watchingAccountId, callback, true);
        }
    }

}
