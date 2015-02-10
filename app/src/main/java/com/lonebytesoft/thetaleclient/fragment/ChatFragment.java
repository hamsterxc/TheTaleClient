package com.lonebytesoft.thetaleclient.fragment;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.StrikethroughSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;

import com.lonebytesoft.thetaleclient.DataViewMode;
import com.lonebytesoft.thetaleclient.R;
import com.lonebytesoft.thetaleclient.TheTaleClientApplication;
import com.lonebytesoft.thetaleclient.api.cache.prerequisite.InfoPrerequisiteRequest;
import com.lonebytesoft.thetaleclient.api.cache.prerequisite.PrerequisiteRequest;
import com.lonebytesoft.thetaleclient.api.model.ChatMessage;
import com.lonebytesoft.thetaleclient.api.response.InfoResponse;
import com.lonebytesoft.thetaleclient.util.ChatManager;
import com.lonebytesoft.thetaleclient.util.PreferencesManager;
import com.lonebytesoft.thetaleclient.util.UiUtils;

import org.xml.sax.XMLReader;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author Hamster
 * @since 24.10.2014
 */
public class ChatFragment extends WrapperFragment {

    private static final DateFormat dateFormatTime = android.text.format.DateFormat.getTimeFormat(TheTaleClientApplication.getContext());
    private static final DateFormat dateFormatDate = android.text.format.DateFormat.getDateFormat(TheTaleClientApplication.getContext());

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
    private View sendContainer;
    private TextView message;
    private View errorSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        scroll = (ScrollView) rootView.findViewById(R.id.chat_scroll);
        outerContainer = rootView.findViewById(R.id.chat_outer_container);
        this.container = (ViewGroup) rootView.findViewById(R.id.chat_container);
        sendContainer = rootView.findViewById(R.id.chat_send_container);
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
                            if(!isAdded()) {
                                return;
                            }

                            message.setText(null);
                            refresh(false);
                        }

                        @Override
                        public void onError() {
                            if(!isAdded()) {
                                return;
                            }

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
            sendContainer.setVisibility(View.GONE);
            mainHandler.removeCallbacks(refreshRunnable);
            new InfoPrerequisiteRequest(new Runnable() {
                @Override
                public void run() {
                    sendContainer.setVisibility(View.VISIBLE);
                    ChatManager.init(PreferencesManager.getAccountName(), new ChatManager.ChatCallback() {
                        @Override
                        public void onSuccess() {
                            if(!isAdded()) {
                                return;
                            }

                            loadMessages(isGlobal);
                            mainHandler.postDelayed(refreshRunnable, REFRESH_TIMEOUT_MILLIS);
                        }

                        @Override
                        public void onError() {
                            if(!isAdded()) {
                                return;
                            }

                            setError(getString(R.string.chat_error));
                        }
                    });
                }
            }, new PrerequisiteRequest.ErrorCallback<InfoResponse>() {
                @Override
                public void processError(InfoResponse response) {
                    sendContainer.setVisibility(View.GONE);
                    loadMessages(isGlobal);
                    mainHandler.postDelayed(refreshRunnable, REFRESH_TIMEOUT_MILLIS);
                }
            }, this).execute();
        } else {
            loadMessages(isGlobal);
        }
    }

    private void loadMessages(final boolean isGlobal) {
        final ChatManager.ChatMessagesCallback callback = new ChatManager.ChatMessagesCallback() {
            @Override
            public void onSuccess(List<ChatMessage> messages) {
                if(!isAdded()) {
                    return;
                }

                if(isGlobal) {
                    container.removeAllViews();
                }
                for(final ChatMessage message : messages) {
                    container.addView(getChatItemView(layoutInflater, container, message));
                }
                if(isGlobal || (scroll.getScrollY() == outerContainer.getHeight() - scroll.getBottom())) {
                    scroll.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            scroll.post(new Runnable() {
                                @Override
                                public void run() {
                                    scroll.fullScroll(View.FOCUS_DOWN);
                                }
                            });
                            UiUtils.removeGlobalLayoutListener(scroll, this);
                        }
                    });
                }

                setMode(DataViewMode.DATA);
            }

            @Override
            public void onError() {
                if(!isAdded()) {
                    return;
                }

                if(isGlobal) {
                    setError(getString(R.string.chat_error));
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
        if(message.isDeleted) {
            textMessage.setText(message.message);
            textMessage.setTypeface(textMessage.getTypeface(), Typeface.ITALIC);
        } else {
            if(message.message.trim().equals(getString(R.string.chat_message_hr))) {
                textMessage.setVisibility(View.GONE);
                view.findViewById(R.id.chat_item_hr).setVisibility(View.VISIBLE);
            } else {
                textMessage.setText(Html.fromHtml(message.message,
                        new Html.ImageGetter() {
                            @Override
                            public Drawable getDrawable(String source) {
                                // not implemented
                                return null;
                            }
                        },
                        new Html.TagHandler() {
                            @Override
                            public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
                                if(tag.equalsIgnoreCase("del")) {
                                    final int len = output.length();
                                    if(opening) {
                                        output.setSpan(new StrikethroughSpan(), len, len, Spannable.SPAN_MARK_MARK);
                                    } else {
                                        final Object span = getSpan(output, StrikethroughSpan.class);
                                        final int where = output.getSpanStart(span);
                                        output.removeSpan(span);
                                        if (where != len) {
                                            output.setSpan(new StrikethroughSpan(), where, len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                        }
                                    }
                                }
                            }

                            private Object getSpan(final Editable text, final Class<?> kind) {
                                final Object[] spans = text.getSpans(0, text.length(), kind);

                                if (spans.length == 0) {
                                    return null;
                                } else {
                                    for(int i = spans.length; i > 0; i--) {
                                        if(text.getSpanFlags(spans[i-1]) == Spannable.SPAN_MARK_MARK) {
                                            return spans[i-1];
                                        }
                                    }
                                    return null;
                                }
                            }
                        }));
                textMessage.setMovementMethod(LinkMovementMethod.getInstance());
            }
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

}
