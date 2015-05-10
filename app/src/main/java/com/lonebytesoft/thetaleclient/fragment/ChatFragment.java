package com.lonebytesoft.thetaleclient.fragment;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.StrikethroughSpan;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.ArrayList;
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
        }
    };

    private LayoutInflater layoutInflater;
    private View rootView;

    private ListView chatMessagesList;
    private ChatAdapter chatAdapter;

    private View sendContainer;
    private EditText textMessage;
    private View errorSend;
    private View chatSend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        rootView = inflater.inflate(R.layout.fragment_chat, container, false);

        chatMessagesList = (ListView) rootView.findViewById(R.id.chat_list);
        registerForContextMenu(chatMessagesList);
        chatMessagesList.addHeaderView(inflater.inflate(R.layout.item_chat_header, chatMessagesList, false), null, false);
        chatMessagesList.addFooterView(inflater.inflate(R.layout.item_chat_footer, chatMessagesList, false), null, false);

        chatAdapter = new ChatAdapter(inflater);
        chatMessagesList.setAdapter(chatAdapter);

        sendContainer = rootView.findViewById(R.id.chat_send_container);
        errorSend = rootView.findViewById(R.id.chat_error_send);

        chatSend = rootView.findViewById(R.id.chat_send);
        chatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                errorSend.setVisibility(View.GONE);
                final String text = textMessage.getText().toString();
                if(!TextUtils.isEmpty(text)) {
                    chatSend.setEnabled(false);
                    UiUtils.hideKeyboard(getActivity());
                    ChatManager.post(text, new ChatManager.ChatCallback() {
                        @Override
                        public void onSuccess() {
                            if(!isAdded()) {
                                return;
                            }

                            chatSend.setEnabled(true);
                            textMessage.setText(null);

                            mainHandler.removeCallbacks(refreshRunnable);
                            refresh(false);
                        }

                        @Override
                        public void onError() {
                            if(!isAdded()) {
                                return;
                            }

                            chatSend.setEnabled(true);
                            errorSend.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        });

        textMessage = (EditText) rootView.findViewById(R.id.chat_message);
        textMessage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_SEND) {
                    chatSend.performClick();
                    return true;
                } else {
                    return false;
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.chat_item, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final int position = contextMenuInfo.position - chatMessagesList.getHeaderViewsCount();
        if(position < 0) {
            return super.onContextItemSelected(item);
        }
        final ChatMessage chatMessage = (ChatMessage) chatAdapter.getItem(position);
        final String message = Html.fromHtml(chatMessage.message).toString();

        switch(item.getItemId()) {
            case R.id.chat_action_copy:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    ClipboardManager clipboardManager =
                            (ClipboardManager) TheTaleClientApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("", message);
                    clipboardManager.setPrimaryClip(clipData);
                } else {
                    android.text.ClipboardManager clipboardManager =
                            (android.text.ClipboardManager) TheTaleClientApplication.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboardManager.setText(message);
                }
                return true;

            case R.id.chat_action_quote:
                setMessageText(String.format("> %s", message));
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void refresh(final boolean isGlobal) {
        super.refresh(isGlobal);

        UiUtils.updateGlobalInfo(ChatFragment.this, null);

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
                    chatAdapter.getMessagesList().clear();
                }
                chatAdapter.getMessagesList().addAll(messages);
                chatAdapter.notifyDataSetChanged();

                if(isGlobal) {
                    chatMessagesList.post(new Runnable() {
                        @Override
                        public void run() {
                            chatMessagesList.setSelection(chatAdapter.getCount() - 1);
                        }
                    });
                }

                mainHandler.postDelayed(refreshRunnable, REFRESH_TIMEOUT_MILLIS);

                setMode(DataViewMode.DATA);
            }

            @Override
            public void onError() {
                if(!isAdded()) {
                    return;
                }

                if(isGlobal) {
                    setError(getString(R.string.chat_error));
                } else {
                    mainHandler.postDelayed(refreshRunnable, REFRESH_TIMEOUT_MILLIS);
                }
            }
        };

        if(isGlobal) {
            ChatManager.getMessages(callback);
        } else {
            ChatManager.getNewMessages(callback);
        }
    }

    private void setMessageText(final String text) {
        textMessage.setText(text);
        textMessage.requestFocus();
        textMessage.setSelection(textMessage.getText().length());
        UiUtils.showKeyboard(getActivity());
    }

    private class ChatAdapter extends BaseAdapter {

        private final LayoutInflater layoutInflater;
        private final List<ChatMessage> items;

        public ChatAdapter(final LayoutInflater layoutInflater) {
            this.layoutInflater = layoutInflater;
            this.items = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if(convertView == null) {
                convertView = layoutInflater.inflate(R.layout.item_chat_message, parent, false);
                holder = new ViewHolder();
                holder.nickname = (TextView) convertView.findViewById(R.id.chat_item_nickname);
                holder.message = (TextView) convertView.findViewById(R.id.chat_item_message);
                holder.hr = convertView.findViewById(R.id.chat_item_hr);
                holder.time = (TextView) convertView.findViewById(R.id.chat_item_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final ChatMessage chatMessage = (ChatMessage) getItem(position);

            holder.nickname.setText(chatMessage.nickname);
            holder.nickname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String currentMessage = textMessage.getText().toString();
                    setMessageText(String.format("%s%s@%s ",
                            currentMessage,
                            TextUtils.isEmpty(currentMessage) ? "" : " ",
                            chatMessage.nickname));
                }
            });

            if(chatMessage.isDeleted) {
                holder.message.setText(chatMessage.message);
                holder.message.setTypeface(holder.nickname.getTypeface(), Typeface.ITALIC);
            } else {
                if(chatMessage.message.trim().equals(getString(R.string.chat_message_hr))) {
                    holder.message.setVisibility(View.GONE);
                    holder.hr.setVisibility(View.VISIBLE);
                } else {
                    holder.message.setTypeface(holder.nickname.getTypeface(), Typeface.NORMAL);
                    holder.message.setText(Html.fromHtml(chatMessage.message,
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
                    holder.message.setMovementMethod(LinkMovementMethod.getInstance());
                }
            }

            final Calendar calendarCurrent = Calendar.getInstance();
            final Calendar calendarMessage = Calendar.getInstance();
            final Date dateMessage = new Date(((long) chatMessage.time) * 1000);
            calendarMessage.setTime(dateMessage);
            if((calendarCurrent.get(Calendar.YEAR) == calendarMessage.get(Calendar.YEAR)) && (calendarCurrent.get(Calendar.DAY_OF_YEAR) == calendarMessage.get(Calendar.DAY_OF_YEAR))) {
                holder.time.setText(dateFormatTime.format(dateMessage));
            } else {
                holder.time.setText(dateFormatDate.format(dateMessage));
            }

            return convertView;
        }

        public List<ChatMessage> getMessagesList() {
            return items;
        }

        private class ViewHolder {
            public TextView nickname;
            public TextView message;
            public View hr;
            public TextView time;
        }

    }

}
