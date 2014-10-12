package com.lonebytesoft.thetaleclient.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.activity.MainActivity;
import com.lonebytesoft.thetaleclient.util.UiUtils;

/**
 * @author Hamster
 * @since 07.10.2014
 */
public class WrapperFragment extends Fragment {

    private View dataView;
    private View loadingView;
    private View errorView;

    @Override
    public void onResume() {
        super.onResume();
        if(isAdded()) {
            refresh(true);
        }
    }

    protected View wrapView(final LayoutInflater layoutInflater, final View view) {
        final View wrapped = layoutInflater.inflate(R.layout.fragment_wrapper, null);

        dataView = wrapped.findViewById(R.id.fragment_part_data);
        loadingView = wrapped.findViewById(R.id.fragment_part_loading);
        errorView = wrapped.findViewById(R.id.fragment_part_error);

        ((FrameLayout) dataView).addView(view);

        return wrapped;
    }

    public void setMode(final Mode mode) {
        if(!isAdded()) {
            return;
        }

        dataView.setVisibility(mode == Mode.DATA ? View.VISIBLE : View.GONE);
        loadingView.setVisibility(mode == Mode.LOADING ? View.VISIBLE : View.GONE);
        errorView.setVisibility(mode == Mode.ERROR ? View.VISIBLE : View.GONE);

        final Activity activity = getActivity();
        if(activity instanceof MainActivity) {
            switch(mode) {
                case DATA:
                case ERROR:
                    ((MainActivity) activity).onRefreshFinished();
                    break;

                case LOADING:
                    ((MainActivity) activity).onRefreshStarted();
                    break;
            }
        }
    }

    public void setError(final String error) {
        setMode(Mode.ERROR);
        UiUtils.setText(errorView.findViewById(R.id.fragment_part_error_text), error);
    }

    protected void refresh(final boolean showLoading) {
        if(!isAdded()) {
            return;
        }

        if(showLoading) {
            setMode(Mode.LOADING);
        }

        final Activity activity = getActivity();
        if(activity instanceof MainActivity) {
            ((MainActivity) activity).onDataRefresh();
        }
    }

    public enum Mode {
        DATA,
        LOADING,
        ERROR,
    }

}
