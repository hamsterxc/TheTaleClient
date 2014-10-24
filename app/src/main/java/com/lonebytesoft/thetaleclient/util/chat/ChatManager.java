package com.lonebytesoft.thetaleclient.util.chat;

import android.os.AsyncTask;

import com.lonebytesoft.thetaleclient.api.CommonRequest;
import com.lonebytesoft.thetaleclient.api.CommonResponseCallback;
import com.lonebytesoft.thetaleclient.api.HttpMethod;
import com.lonebytesoft.thetaleclient.api.model.ChatMessage;
import com.lonebytesoft.thetaleclient.util.ObjectUtils;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Hamster
 * @since 24.10.2014
 */
public class ChatManager {

    private static final String URL_BASE = "http://embed.tlk.io/";
    private static final String URL_MAIN = URL_BASE + "the-tale";
    private static final String URL_PARTICIPANT = URL_BASE + "api/participant";
    private static final String URL_MESSAGE = URL_BASE + "api/chats/141060/messages";
    private static final String HEADER_CSRF_TOKEN = "X-CSRF-Token";
    private static final String COOKIE_SESSION = "_tlkio_session";

    private static String csrfToken = null;
    private static String session = null;

    private static int lastId = 0;

    public static void init(final String nickname, final ChatCallback callback) {
        csrfToken = null;
        session = null;

        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                final DefaultHttpClient httpClient = new DefaultHttpClient();

                // get main page
                final HttpUriRequest httpRequestMain = new HttpGet(URL_MAIN);
                final OutputStream outputStreamMain = new ByteArrayOutputStream();
                try {
                    httpClient.execute(httpRequestMain).getEntity().writeTo(outputStreamMain);
                } catch (IOException e) {
                    callback.onError();
                    return null;
                }

                final Pattern pattern = Pattern.compile("<meta\\s+content=\"([^\"]*)\"\\s+name=\"csrf-token\"");
                final Matcher matcher = pattern.matcher(outputStreamMain.toString());
                try {
                    outputStreamMain.close();
                } catch (IOException ignored) {
                }
                if(matcher.find()) {
                    csrfToken = matcher.group(1);
                } else {
                    callback.onError();
                    return null;
                }

                session = getSession(httpClient);

                // post nickname
                final HttpPost httpRequestLogin = new HttpPost(URL_PARTICIPANT);
                setParams(httpRequestLogin, session, csrfToken);

                final List<NameValuePair> httpRequestLoginParams = new ArrayList<>(1);
                httpRequestLoginParams.add(new BasicNameValuePair("nickname", nickname));
                try {
                    httpRequestLogin.setEntity(new UrlEncodedFormEntity(httpRequestLoginParams));
                } catch(UnsupportedEncodingException e) {
                    callback.onError();
                    return null;
                }

                try {
                    httpClient.execute(httpRequestLogin);
                } catch (IOException e) {
                    callback.onError();
                    return null;
                }

                session = getSession(httpClient);

                callback.onSuccess();

                return null;
            }
        }.execute();
    }

    public static void post(final String message, final ChatCallback callback) {
        if((csrfToken == null) || (session == null)) {
            throw new IllegalStateException("Must call init() first");
        }

        new AsyncTask<Void, Void, Void>() {
            protected Void doInBackground(Void... params) {
                final DefaultHttpClient httpClient = new DefaultHttpClient();
                final HttpPost httpRequest = new HttpPost(URL_MESSAGE);
                setParams(httpRequest, session, csrfToken);

                final List<NameValuePair> httpRequestParams = new ArrayList<>(1);
                httpRequestParams.add(new BasicNameValuePair("body", message));
                try {
                    httpRequest.setEntity(new UrlEncodedFormEntity(httpRequestParams, "UTF-8"));
                } catch(UnsupportedEncodingException e) {
                    callback.onError();
                }

                final OutputStream outputStream = new ByteArrayOutputStream();
                try {
                    httpClient.execute(httpRequest).getEntity().writeTo(outputStream);
                    final JSONObject json = new JSONObject(outputStream.toString());
                    if(json.has("error")) {
                        callback.onError();
                        return null;
                    }
                } catch (IOException|JSONException e) {
                    callback.onError();
                    return null;
                } finally {
                    try {
                        outputStream.close();
                    } catch (IOException ignored) {
                    }
                }

                session = getSession(httpClient);

                callback.onSuccess();

                return null;
            }
        }.execute();
    }

    public static void getMessages(final ChatMessagesCallback callback) {
        new CommonRequest() {
            protected long getStaleTime() {
                return 0;
            }
        }.execute(URL_MESSAGE, HttpMethod.GET, null, null, new CommonResponseCallback<String, Throwable>() {
            @Override
            public void processResponse(String response) {
                try {
                    final JSONArray messagesJson = new JSONArray(response);
                    final int count = messagesJson.length();
                    final List<ChatMessage> messages = new ArrayList<>(count);
                    for(int i = 0; i < count; i++) {
                        final ChatMessage message = ObjectUtils.getModelFromJson(ChatMessage.class, messagesJson.getJSONObject(i));
                        messages.add(message);
                        if(message.id > lastId) {
                            lastId = message.id;
                        }
                    }
                    callback.onSuccess(messages);
                } catch(JSONException e) {
                    callback.onError();
                }
            }

            @Override
            public void processError(Throwable error) {
                callback.onError();
            }
        });
    }

    public static void getNewMessages(final ChatMessagesCallback callback) {
        final int lastId = ChatManager.lastId;
        getMessages(new ChatMessagesCallback() {
            @Override
            public void onSuccess(List<ChatMessage> messages) {
                final int count = messages.size();
                if(count > 0) {
                    int position = 0;
                    for (; position < count; position++) {
                        if (messages.get(position).id > lastId) {
                            break;
                        }
                    }
                    if(position < count) {
                        callback.onSuccess(messages.subList(position, count));
                    } else {
                        callback.onSuccess(new ArrayList<ChatMessage>(0));
                    }
                } else {
                    callback.onSuccess(new ArrayList<ChatMessage>(0));
                }
            }

            @Override
            public void onError() {
                callback.onError();
            }
        });
    }

    private static void setParams(final HttpPost httpRequest,
                                  final String cookieSession, final String csrfToken) {
        httpRequest.addHeader("Cookie", COOKIE_SESSION + "=" + cookieSession);
        httpRequest.addHeader(HEADER_CSRF_TOKEN, csrfToken);
    }

    private static String getSession(final DefaultHttpClient httpClient) {
        for(final Cookie cookie : httpClient.getCookieStore().getCookies()) {
            if(cookie.getName().equals(COOKIE_SESSION)) {
                return cookie.getValue();
            }
        }
        return null;
    }

    public interface ChatCallback {
        void onSuccess();
        void onError();
    }

    public interface ChatMessagesCallback {
        void onSuccess(List<ChatMessage> messages);
        void onError();
    }

}
