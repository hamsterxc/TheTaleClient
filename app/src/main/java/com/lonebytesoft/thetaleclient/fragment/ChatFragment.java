package com.lonebytesoft.thetaleclient.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.DataViewMode;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.api.ApiResponseCallback;
import com.lonebytesoft.thetaleclient.api.model.ChatMessage;
import com.lonebytesoft.thetaleclient.api.request.InfoRequest;
import com.lonebytesoft.thetaleclient.api.response.InfoResponse;
import com.lonebytesoft.thetaleclient.util.UiUtils;
import com.lonebytesoft.thetaleclient.util.chat.ChatManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Hamster
 * @since 24.10.2014
 */
public class ChatFragment extends WrapperFragment {

    private static final SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat dateFormatDate = new SimpleDateFormat("dd.MM.yy");

    private static final long REFRESH_TIMEOUT_MILLIS = 5000; // 5 s

    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            refresh(false);
            mainHandler.postDelayed(this, REFRESH_TIMEOUT_MILLIS);
        }
    };

    private LayoutInflater layoutInflater;
    private View rootView;

    private ScrollView scroll;
    private View outerContainer;
    private ViewGroup container;
    private TextView message;
    private View errorSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        scroll = (ScrollView) rootView.findViewById(R.id.chat_scroll);
        outerContainer = rootView.findViewById(R.id.chat_outer_container);
        this.container = (ViewGroup) rootView.findViewById(R.id.chat_container);
        errorSend = rootView.findViewById(R.id.chat_error_send);
        message = (TextView) rootView.findViewById(R.id.chat_message);
        rootView.findViewById(R.id.chat_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorSend.setVisibility(View.GONE);
                final String text = message.getText().toString();
                if(!TextUtils.isEmpty(text)) {
                    UiUtils.hideKeyboard(getActivity());
                    ChatManager.post(text, new ChatManager.ChatCallback() {
                        @Override
                        public void onSuccess() {
                            message.setText(null);
                            refresh(false);
                        }

                        @Override
                        public void onError() {
                            errorSend.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

        return wrapView(layoutInflater, rootView);
    }

    @Override
    public void onPause() {
        super.onPause();

        mainHandler.removeCallbacks(refreshRunnable);
    }

    @Override
    public void refresh(final boolean isGlobal) {
        super.refresh(isGlobal);

        if(isGlobal) {
            mainHandler.removeCallbacks(refreshRunnable);
            new InfoRequest().execute(new ApiResponseCallback<InfoResponse>() {
                @Override
                public void processResponse(InfoResponse response) {
                    ChatManager.init(response.accountName, new ChatManager.ChatCallback() {
                        @Override
                        public void onSuccess() {
                            loadMessages(isGlobal);
                            mainHandler.postDelayed(refreshRunnable, REFRESH_TIMEOUT_MILLIS);
                        }

                        @Override
                        public void onError() {
                            setError();
                        }
                    });
                }

                @Override
                public void processError(InfoResponse response) {
                    setError();
                }
            });
        } else {
            loadMessages(isGlobal);
        }
    }

    private void loadMessages(final boolean isGlobal) {
        final ChatManager.ChatMessagesCallback callback = new ChatManager.ChatMessagesCallback() {
            @Override
            public void onSuccess(List<ChatMessage> messages) {
                if(isGlobal) {
                    container.removeAllViews();
                }
                for(final ChatMessage message : messages) {
                    container.addView(getChatItemView(layoutInflater, container, message));
                }
                if(isGlobal || (scroll.getScrollY() == outerContainer.getHeight() - scroll.getBottom())) {
                    scroll.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll.fullScroll(View.FOCUS_DOWN);
//                            scroll.scrollTo(0, scroll.getBottom());
                        }
                    });
                }

                setMode(DataViewMode.DATA);
            }

            @Override
            public void onError() {
                if(isGlobal) {
                    setError();
                }
            }
        };

        if(isGlobal) {
            ChatManager.getMessages(callback);
        } else {
            ChatManager.getNewMessages(callback);
        }
    }

    private View getChatItemView(final LayoutInflater layoutInflater, final ViewGroup container, final ChatMessage message) {
        final View view = layoutInflater.inflate(R.layout.item_chat_message, container, false);

        ((TextView) view.findViewById(R.id.chat_item_nickname)).setText(message.nickname);

        final TextView textMessage = (TextView) view.findViewById(R.id.chat_item_message);
        textMessage.setText(message.message);
        if(message.isDeleted) {
            textMessage.setTypeface(textMessage.getTypeface(), Typeface.ITALIC);
        }

        final Calendar calendarCurrent = Calendar.getInstance();
        final Calendar calendarMessage = Calendar.getInstance();
        final Date dateMessage = new Date(((long) message.time) * 1000);
        calendarMessage.setTime(dateMessage);
        if((calendarCurrent.get(Calendar.YEAR) == calendarMessage.get(Calendar.YEAR)) && (calendarCurrent.get(Calendar.DAY_OF_YEAR) == calendarMessage.get(Calendar.DAY_OF_YEAR))) {
            ((TextView) view.findViewById(R.id.chat_item_time)).setText(dateFormatTime.format(dateMessage));
        } else {
            ((TextView) view.findViewById(R.id.chat_item_time)).setText(dateFormatDate.format(dateMessage));
        }

        return view;
    }

    private void setError() {
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                setError(getString(R.string.chat_error));
            }
        });
    }

}
