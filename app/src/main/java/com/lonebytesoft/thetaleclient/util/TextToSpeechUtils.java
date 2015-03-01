package com.lonebytesoft.thetaleclient.util;

import android.content.Context;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Hamster
 * @since 30.11.2014
 */
public class TextToSpeechUtils {

    private static final long PAUSE = 500;

    private static TextToSpeech textToSpeech;

    private static boolean isInitialized = false;
    private static List<String> queue = new ArrayList<>();

    public static void init(final Context context, final InitCallback initCallback) {
        final InitCallback callback;
        if(initCallback == null) {
            callback = new InitCallback() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onInitError() {
                }

                @Override
                public void onLanguageError() {
                }
            };
        } else {
            callback = initCallback;
        }

        if(!isInitialized || (textToSpeech == null)) {
            textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(final int status) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            if (status == TextToSpeech.SUCCESS) {
                                final int setLanguageStatus = textToSpeech.setLanguage(new Locale("ru"));
                                if(setLanguageStatus >= TextToSpeech.LANG_AVAILABLE) {
                                    callback.onSuccess();

                                    isInitialized = true;
                                    for (final String text : queue) {
                                        speakText(text);
                                    }
                                    queue.clear();
                                } else {
                                    callback.onLanguageError();
                                }
                            } else {
                                callback.onInitError();
                            }
                            return null;
                        }
                    }.execute();
                }
            });
        } else {
            callback.onSuccess();
        }
    }

    public static void speak(final String text) {
        if(isInitialized) {
            speakText(text);
        } else {
            queue.add(text);
        }
    }

    private static void speakText(final String text) {
        if(textToSpeech != null) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
            textToSpeech.playSilence(PAUSE, TextToSpeech.QUEUE_ADD, null);
        }
    }

    public static void pause() {
        if(textToSpeech != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    textToSpeech.stop();
                }
            }).start();
        }
        queue.clear();
    }

    public static void destroy() {
        if(textToSpeech != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    textToSpeech.shutdown();
                }
            }).start();
        }
        queue.clear();
        isInitialized = false;
    }

    public interface InitCallback {
        void onSuccess();
        void onInitError();
        void onLanguageError();
    }

}
